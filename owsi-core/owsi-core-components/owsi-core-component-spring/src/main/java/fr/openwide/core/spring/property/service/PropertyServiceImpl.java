package fr.openwide.core.spring.property.service;

import static fr.openwide.core.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static fr.openwide.core.spring.property.SpringPropertyIds.DEFAULT_LOCALE;

import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.base.Converter;
import com.google.common.base.Enums;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import fr.openwide.core.commons.util.functional.converter.StringBigDecimalConverter;
import fr.openwide.core.commons.util.functional.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateTimeConverter;
import fr.openwide.core.commons.util.functional.converter.StringDirectoryFileCreatingConverter;
import fr.openwide.core.commons.util.functional.converter.StringFileConverter;
import fr.openwide.core.commons.util.functional.converter.StringLocaleConverter;
import fr.openwide.core.commons.util.functional.converter.StringTimeConverter;
import fr.openwide.core.commons.util.functional.converter.StringURIConverter;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.spring.event.PropertyRegistryInitEvent;
import fr.openwide.core.spring.property.SpringPropertyIds;
import fr.openwide.core.spring.property.dao.IImmutablePropertyDao;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.exception.PropertyServiceDuplicateRegistrationException;
import fr.openwide.core.spring.property.exception.PropertyServiceIncompleteRegistrationException;
import fr.openwide.core.spring.property.model.IImmutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.IMutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.IMutablePropertyValueMap;
import fr.openwide.core.spring.property.model.IPropertyRegistryKey;
import fr.openwide.core.spring.property.model.IPropertyRegistryKeyDeclaration;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.PropertyId;
import fr.openwide.core.spring.property.model.PropertyIdTemplate;

/**
 * Use this service to retrieve registered application properties.
 * It handles both mutable and immutable properties ; immutable properties are retrieved from properties resources files
 * ({@link IImmutablePropertyDao}) and mutable properties are stored in database ({@link IMutablePropertyDao}).
 * @see {@link IPropertyRegistry} to register application properties.
 */
public class PropertyServiceImpl implements IConfigurablePropertyService, ApplicationEventPublisherAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

	private final Map<IPropertyRegistryKey<?>, Pair<? extends Converter<String, ?>, ? extends Supplier<?>>> propertyInformationMap =
			Maps.newLinkedHashMap();

	@Autowired
	@Lazy // Mutable properties may require a more complex infrastructure, whose setup may require access to immutable properties
	private IMutablePropertyDao mutablePropertyDao;
	
	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	private ApplicationEventPublisher applicationEventPublisher;

	private TransactionTemplate writeTransactionTemplate;

	@Autowired
	@Lazy // Mutable properties may require a more complex infrastructure, whose setup may require access to immutable properties
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute writeTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
	}

	@PostConstruct
	public void init() throws PropertyServiceIncompleteRegistrationException {
		applicationEventPublisher.publishEvent(new PropertyRegistryInitEvent(this));
		checkNoIncompleteRegistration();
	}
	
	private void checkNoIncompleteRegistration() throws PropertyServiceIncompleteRegistrationException {
		SetMultimap<IPropertyRegistryKeyDeclaration, IPropertyRegistryKey<?>> declarationsToRegisteredKeys = LinkedHashMultimap.create();
		for (IPropertyRegistryKey<?> key : propertyInformationMap.keySet()) {
			declarationsToRegisteredKeys.put(key.getDeclaration(), key);
		}

		SetMultimap<IPropertyRegistryKeyDeclaration, IPropertyRegistryKey<?>> declarationsToUnregisteredKeys = LinkedHashMultimap.create();
		for (Map.Entry<IPropertyRegistryKeyDeclaration, Set<IPropertyRegistryKey<?>>> declarationAndRegisteredKeys
				: Multimaps.asMap(declarationsToRegisteredKeys).entrySet()) {
			IPropertyRegistryKeyDeclaration declaration = declarationAndRegisteredKeys.getKey();
			Set<IPropertyRegistryKey<?>> registeredKeys = declarationAndRegisteredKeys.getValue();
			declarationsToUnregisteredKeys.putAll(
					declaration,
					Sets.difference(declaration.getDeclaredKeys(), registeredKeys)
			);
		}
		
		if (!declarationsToUnregisteredKeys.isEmpty()) {
			throw new PropertyServiceIncompleteRegistrationException(
					String.format(
							"The registration of property keys is incomplete."
							+ " Here are the missing keys for each declaration: %s",
							declarationsToUnregisteredKeys
					)
			);
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public <T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter) {
		register(propertyId, converter, (T) null);
	}

	@Override
	public <T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, T defaultValue) {
		register(propertyId, converter, Suppliers.ofInstance(defaultValue));
	}

	@Override
	public <T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, Supplier<? extends T> defaultValueSupplier) {
		registerProperty(propertyId, converter, defaultValueSupplier);
	}

	@Override
	public <T> void register(IImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function) {
		register(propertyId, function, (T) null);
	}

	@Override
	public <T> void register(IImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function, T defaultValue) {
		register(propertyId, function, Suppliers.ofInstance(defaultValue));
	}

	@Override
	public <T> void register(IImmutablePropertyRegistryKey<T> propertyId, final Function<String, ? extends T> function, Supplier<? extends T> defaultValueSupplier) {
		registerProperty(propertyId, new Converter<String, T>() {
			@Override
			protected T doForward(String a) {
				return function.apply(a);
			}
			@Override
			protected String doBackward(T b) {
				throw new IllegalStateException("Unable to update immutable property.");
			}
			
			/**
			 * Workaround sonar/findbugs - https://github.com/google/guava/issues/1858
			 * Guava Converter overrides only equals to add javadoc, but findbugs warns about non coherent equals/hashcode
			 * possible issue.
			 */
			@Override
			public boolean equals(Object object) {
				return super.equals(object);
			}
			
			/**
			 * Workaround sonar/findbugs - see #equals(Object)
			 */
			@Override
			public int hashCode() {
				return super.hashCode();
			}
		}, defaultValueSupplier);
	}

	protected <T> void registerProperty(IPropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter) {
		registerProperty(propertyId, converter, (T) null);
	}

	protected <T> void registerProperty(IPropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter, T defaultValue) {
		registerProperty(propertyId, converter, Suppliers.ofInstance(defaultValue));
	}

	protected <T> void registerProperty(IPropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter, Supplier<? extends T> defaultValueSupplier) {
		Preconditions.checkNotNull(propertyId);
		Preconditions.checkNotNull(converter);
		Preconditions.checkNotNull(defaultValueSupplier);
		
		if (propertyInformationMap.containsKey(propertyId)) {
			throw new PropertyServiceDuplicateRegistrationException(String.format(
					"Property '%1s' has already been registered."
					+ " There might be multiple property IDs in your application that use the same key.",
					propertyId
			));
		}
		
		propertyInformationMap.put(propertyId, Pair.with(converter, defaultValueSupplier));
	}

	@Override
	public void registerString(IPropertyRegistryKey<String> propertyId) {
		registerString(propertyId, null);
	}

	@Override
	public void registerString(IPropertyRegistryKey<String> propertyId, String defaultValue) {
		registerProperty(propertyId, Converter.<String>identity(), defaultValue);
	}

	@Override
	public void registerLong(IPropertyRegistryKey<Long> propertyId) {
		registerLong(propertyId, null);
	}

	@Override
	public void registerLong(IPropertyRegistryKey<Long> propertyId, Long defaultValue) {
		registerProperty(propertyId, Longs.stringConverter(), defaultValue);
	}

	@Override
	public void registerInteger(IPropertyRegistryKey<Integer> propertyId) {
		registerInteger(propertyId, null);
	}

	@Override
	public void registerInteger(IPropertyRegistryKey<Integer> propertyId, Integer defaultValue) {
		registerProperty(propertyId, Ints.stringConverter(), defaultValue);
	}

	@Override
	public void registerFloat(IPropertyRegistryKey<Float> propertyId) {
		registerFloat(propertyId, null);
	}

	@Override
	public void registerFloat(IPropertyRegistryKey<Float> propertyId, Float defaultValue) {
		registerProperty(propertyId, Floats.stringConverter(), defaultValue);
	}

	@Override
	public void registerDouble(IPropertyRegistryKey<Double> propertyId) {
		registerDouble(propertyId, null);
	}

	@Override
	public void registerDouble(IPropertyRegistryKey<Double> propertyId, Double defaultValue) {
		registerProperty(propertyId, Doubles.stringConverter(), defaultValue);
	}

	@Override
	public void registerBigDecimal(IPropertyRegistryKey<BigDecimal> propertyId) {
		registerBigDecimal(propertyId, null);
	}

	@Override
	public void registerBigDecimal(IPropertyRegistryKey<BigDecimal> propertyId, BigDecimal defaultValue) {
		registerProperty(propertyId, StringBigDecimalConverter.get(), defaultValue);
	}

	@Override
	public void registerBoolean(IPropertyRegistryKey<Boolean> propertyId) {
		registerBoolean(propertyId, null);
	}

	@Override
	public void registerBoolean(IPropertyRegistryKey<Boolean> propertyId, Boolean defaultValue) {
		registerProperty(propertyId, StringBooleanConverter.get(), defaultValue);
	}

	@Override
	public <E extends Enum<E>> void registerEnum(IPropertyRegistryKey<E> propertyId, Class<E> clazz) {
		registerEnum(propertyId, clazz, null);
	}

	@Override
	public <E extends Enum<E>> void registerEnum(IPropertyRegistryKey<E> propertyId, Class<E> clazz, E defaultValue) {
		registerProperty(propertyId, Enums.stringConverter(clazz), defaultValue);
	}

	@Override
	public void registerDate(IPropertyRegistryKey<Date> propertyId) {
		registerDate(propertyId, (Date) null);
	}

	@Override
	public void registerDate(IPropertyRegistryKey<Date> propertyId, String defaultValue) {
		registerDate(propertyId, StringDateConverter.get().convert(defaultValue));
	}

	@Override
	public void registerDate(IPropertyRegistryKey<Date> propertyId, Date defaultValue) {
		registerProperty(propertyId, StringDateConverter.get(), defaultValue);
	}

	@Override
	public void registerTime(IPropertyRegistryKey<Date> propertyId) {
		registerTime(propertyId, (Date) null);
	}

	@Override
	public void registerTime(IPropertyRegistryKey<Date> propertyId, String defaultValue) {
		registerTime(propertyId, StringTimeConverter.get().convert(defaultValue));
	}

	@Override
	public void registerTime(IPropertyRegistryKey<Date> propertyId, Date defaultValue) {
		registerProperty(propertyId, StringTimeConverter.get(), defaultValue);
	}

	@Override
	public void registerDateTime(IPropertyRegistryKey<Date> propertyId) {
		registerDateTime(propertyId, null);
	}

	@Override
	public void registerDateTime(IPropertyRegistryKey<Date> propertyId, Date defaultValue) {
		registerProperty(propertyId, StringDateTimeConverter.get(), defaultValue);
	}

	@Override
	public void registerLocale(IPropertyRegistryKey<Locale> propertyId) {
		registerLocale(propertyId, null);
	}

	@Override
	public void registerLocale(IPropertyRegistryKey<Locale> propertyId, Locale defaultValue) {
		registerProperty(propertyId, StringLocaleConverter.get(), defaultValue);
	}
	
	@Override
	public void registerFile(IPropertyRegistryKey<File> propertyId, FileFilter filter) {
		registerFile(propertyId, filter, null);
	}
	
	@Override
	public void registerFile(IPropertyRegistryKey<File> propertyId, final FileFilter filter, final File defaultValue) {
		Preconditions.checkNotNull(filter);
		registerProperty(propertyId, new StringFileConverter(filter), new Supplier<File>() {
			@Override
			public File get() {
				// Make this check *only* if we actually use the default value.
				Preconditions.checkState(
						defaultValue == null || filter.accept(defaultValue),
						"The default value " + defaultValue + " does not match the given file filter " + filter
				);
				return defaultValue;
			}
		});
	}

	@Override
	public void registerWriteableDirectoryFile(IPropertyRegistryKey<File> propertyId) {
		registerWriteableDirectoryFile(propertyId, null);
	}

	@Override
	public void registerWriteableDirectoryFile(IPropertyRegistryKey<File> propertyId, File defaultValue) {
		registerProperty(propertyId, StringDirectoryFileCreatingConverter.get(), defaultValue);
	}

	@Override
	public void registerURI(IPropertyRegistryKey<URI> propertyId) {
		registerURI(propertyId, null);
	}

	@Override
	public void registerURI(IPropertyRegistryKey<URI> propertyId, URI defaultValue) {
		registerProperty(propertyId, StringURIConverter.get(), defaultValue);
	}

	@Override
	public <T> T get(final PropertyId<T> propertyId) {
		Preconditions.checkNotNull(propertyId);
		
		Pair<Converter<String, T>, Supplier<T>> information = getRegistrationInformation(propertyId);
		
		T value = information.getValue0().convert(getAsStringUnsafe(propertyId));
		if (value == null) {
			T defaultValue = information.getValue1().get();
			
			if (defaultValue != null) {
				value = defaultValue;
				LOGGER.debug(String.format("Property '%1s' has no value, fallback on default value.", propertyId));
			} else {
				LOGGER.info(String.format("Property '%1s' has no value and default value is undefined.", propertyId));
			}
		}
		
		return value;
	}

	@Override
	public <T> String getAsString(final PropertyId<T> propertyId) {
		Pair<Converter<String, T>, Supplier<T>> information = getRegistrationInformation(propertyId);
		
		String valueAsString = getAsStringUnsafe(propertyId);
		
		if (valueAsString == null) {
			T defaultValue = information.getValue1().get();
			
			if (defaultValue != null) {
				valueAsString = information.getValue0().reverse().convert(defaultValue);
				LOGGER.debug(String.format("Property '%1s' has no value, fallback on default value.", propertyId));
			} else {
				LOGGER.info(String.format("Property '%1s' has no value and default value is undefined.", propertyId));
			}
		}
		
		return valueAsString;
	}
	
	private String getAsStringUnsafe(PropertyId<?> propertyId) {
		String valueAsString = null;
		if (propertyId instanceof ImmutablePropertyId) {
			valueAsString = immutablePropertyDao.get(propertyId.getKey());
		} else if (propertyId instanceof MutablePropertyId) {
			valueAsString = mutablePropertyDao.getInTransaction(propertyId.getKey());
		} else {
			throw new IllegalStateException(String.format("Unknown type of property : '%1s'.", propertyId));
		}
		return valueAsString;
	}

	@Override
	public <T> void set(final MutablePropertyId<T> propertyId, final T value) throws ServiceException, SecurityServiceException {
		Preconditions.checkNotNull(propertyId);
		
		Pair<Converter<String, T>, Supplier<T>> information = getRegistrationInformation(propertyId);
		
		mutablePropertyDao.setInTransaction(propertyId.getKey(), information.getValue0().reverse().convert(value));
	}

	private <T> void set(IMutablePropertyValueMap.Entry<T> idToValueEntry) throws ServiceException, SecurityServiceException {
		set(idToValueEntry.getKey(), idToValueEntry.getValue());
	}

	@Override
	public void setAll(final IMutablePropertyValueMap valueMap) throws ServiceException, SecurityServiceException {
		Objects.requireNonNull(valueMap);
		writeTransactionTemplate.execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						try {
							for (IMutablePropertyValueMap.Entry<?> idToValueEntry : valueMap) {
								set(idToValueEntry);
							}
						} catch (RuntimeException | ServiceException | SecurityServiceException e) {
							throw new IllegalStateException(String.format("Error while updating properties"), e);
						}
					}
				}
		);
	}

	@Override
	public <T> void setAsString(final MutablePropertyId<T> propertyId, final String valueAsString) throws ServiceException, SecurityServiceException {
		Preconditions.checkNotNull(propertyId);
		getRegistrationInformation(propertyId);
		mutablePropertyDao.setInTransaction(propertyId.getKey(), valueAsString);
	}

	private <T> Pair<Converter<String, T>, Supplier<T>> getRegistrationInformation(PropertyId<T> propertyId) {
		PropertyIdTemplate<T, ?> template = propertyId.getTemplate();
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, Supplier<T>> information = (Pair<Converter<String, T>, Supplier<T>>)
				propertyInformationMap.get(template != null ? template : propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new PropertyServiceIncompleteRegistrationException(
					String.format("The following property was not properly registered: %1s", propertyId)
			);
		}
		
		return information;
	}

	@Override
	public List<IPropertyRegistryKey<?>> listRegistered() {
		return Lists.newArrayList(propertyInformationMap.keySet());
	}

	// TODO PropertyService : remove this method from service
	@Override
	public boolean isConfigurationTypeDevelopment() {
		return SpringPropertyIds.CONFIGURATION_TYPE_DEVELOPMENT.equals(get(SpringPropertyIds.CONFIGURATION_TYPE));
	}

	// TODO PropertyService : remove this method from service
	/**
	 * <p> Le but est de partir d'une locale
	 * quelconque et d'aboutir obligatoirement à une locale provenant de la liste
	 * <i>locale.availableLocales</i>.</p>
	 * 
	 * <p>Le mapping se fait ainsi :
	 * <ul>
	 * <li>si la locale est dans locale.availableLocales, alors on utilise la locale</li>
	 * <li>sinon on vérifié si le <i>Language</i> de la locale correspond à un <i>Language</i>
	 * dans locale.availableLocales ; alors on utilise la locale correspondante
	 * </li>
	 * <li>sinon on utilise <i>locale.default</i></li>
	 * </ul>
	 * </p>
	 * 
	 * <p>Exemple :<br/>
	 * <code>locale.availableLocales=fr en</code><br/>
	 * <code>locale.default=fr</code><br/>
	 * <br/>
	 * Les résultats seront les suivants
	 * <ul>
	 * <li>fr -> fr (correspondance exacte)</li>
	 * <li>fr_FR -> fr (correspondance sur Language)</li>
	 * <li>en -> en (correspondance exacte)</li>
	 * <li>en_US -> en (correspondance sur Language)</li>
	 * <li>ar_SA -> fr (défaut)</li>
	 * </ul>
	 * </p>
	 *
	 * @param locale
	 * @return locale, not null, from locale.availableLocales
	 */
	@Override
	public Locale toAvailableLocale(Locale locale) {
		if (locale != null) {
			Set<Locale> availableLocales = get(AVAILABLE_LOCALES);
			if (availableLocales.contains(locale)) {
				return locale;
			} else {
				for (Locale availableLocale : availableLocales) {
					if (availableLocale.getLanguage().equals(locale.getLanguage())) {
						return availableLocale;
					}
				}
			}
		}
		
		// default locale from configuration
		return get(DEFAULT_LOCALE);
	}

}
