package fr.openwide.core.spring.property.service;

import static fr.openwide.core.spring.property.SpringPropertyIds.AVAILABLE_LOCALES;
import static fr.openwide.core.spring.property.SpringPropertyIds.DEFAULT_LOCALE;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.google.common.base.Converter;
import com.google.common.base.Enums;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.commons.util.functional.converter.StringBigDecimalConverter;
import fr.openwide.core.commons.util.functional.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateTimeConverter;
import fr.openwide.core.commons.util.functional.converter.StringDirectoryFileConverter;
import fr.openwide.core.commons.util.functional.converter.StringLocaleConverter;
import fr.openwide.core.commons.util.functional.converter.StringURIConverter;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.spring.event.PropertyRegistryInitEvent;
import fr.openwide.core.spring.property.SpringPropertyIds;
import fr.openwide.core.spring.property.dao.IImmutablePropertyDao;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.ImmutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.PropertyId;
import fr.openwide.core.spring.property.model.PropertyRegistryKey;

/**
 * Use this service to retrieve registered application properties.
 * It handles both mutable and immutable properties ; immutable properties are retrieved from properties resources files
 * ({@link IImmutablePropertyDao}) and mutable properties are stored in database ({@link IMutablePropertyDao}).
 * @see {@link IPropertyRegistry} to register application properties.
 */
public class PropertyServiceImpl implements IConfigurablePropertyService, ApplicationEventPublisherAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

	private final Map<PropertyRegistryKey<?>, Pair<? extends Converter<String, ?>, ? extends Supplier<?>>> propertyInformationMap = Maps.newHashMap();

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;
	
	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	private ApplicationEventPublisher applicationEventPublisher;

	@PostConstruct
	public void init() {
		applicationEventPublisher.publishEvent(new PropertyRegistryInitEvent(this));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public <T> void register(MutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter) {
		register(propertyId, converter, (T) null);
	}

	@Override
	public <T> void register(MutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, T defaultValue) {
		register(propertyId, converter, Suppliers2.constant(defaultValue));
	}

	@Override
	public <T> void register(MutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, Supplier<? extends T> defaultValueSupplier) {
		registerProperty(propertyId, converter, defaultValueSupplier);
	}

	@Override
	public <T> void register(ImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function) {
		register(propertyId, function, (T) null);
	}

	@Override
	public <T> void register(ImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function, T defaultValue) {
		register(propertyId, function, Suppliers2.constant(defaultValue));
	}

	@Override
	public <T> void register(ImmutablePropertyRegistryKey<T> propertyId, final Function<String, ? extends T> function, Supplier<? extends T> defaultValueSupplier) {
		registerProperty(propertyId, new Converter<String, T>() {
			@Override
			protected T doForward(String a) {
				return function.apply(a);
			}
			@Override
			protected String doBackward(T b) {
				throw new IllegalStateException("Unable to update immutable property.");
			}
		}, defaultValueSupplier);
	}

	protected <T> void registerProperty(PropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter) {
		registerProperty(propertyId, converter, (T) null);
	}

	protected <T> void registerProperty(PropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter, T defaultValue) {
		registerProperty(propertyId, converter, Suppliers2.constant(defaultValue));
	}

	protected <T> void registerProperty(PropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter, Supplier<? extends T> defaultValueSupplier) {
		Preconditions.checkNotNull(propertyId);
		Preconditions.checkNotNull(converter);
		Preconditions.checkNotNull(defaultValueSupplier);
		
		if (propertyInformationMap.get(propertyId) != null) {
			throw new IllegalStateException(String.format("Property '%1s' already registered.", propertyId));
		}
		
		propertyInformationMap.put(propertyId, Pair.with(converter, defaultValueSupplier));
	}

	@Override
	public void registerString(PropertyRegistryKey<String> propertyId) {
		registerString(propertyId, null);
	}

	@Override
	public void registerString(PropertyRegistryKey<String> propertyId, String defaultValue) {
		registerProperty(propertyId, Converter.<String>identity(), defaultValue);
	}

	@Override
	public void registerLong(PropertyRegistryKey<Long> propertyId) {
		registerLong(propertyId, null);
	}

	@Override
	public void registerLong(PropertyRegistryKey<Long> propertyId, Long defaultValue) {
		registerProperty(propertyId, Longs.stringConverter(), defaultValue);
	}

	@Override
	public void registerInteger(PropertyRegistryKey<Integer> propertyId) {
		registerInteger(propertyId, null);
	}

	@Override
	public void registerInteger(PropertyRegistryKey<Integer> propertyId, Integer defaultValue) {
		registerProperty(propertyId, Ints.stringConverter(), defaultValue);
	}

	@Override
	public void registerFloat(PropertyRegistryKey<Float> propertyId) {
		registerFloat(propertyId, null);
	}

	@Override
	public void registerFloat(PropertyRegistryKey<Float> propertyId, Float defaultValue) {
		registerProperty(propertyId, Floats.stringConverter(), defaultValue);
	}

	@Override
	public void registerDouble(PropertyRegistryKey<Double> propertyId) {
		registerDouble(propertyId, null);
	}

	@Override
	public void registerDouble(PropertyRegistryKey<Double> propertyId, Double defaultValue) {
		registerProperty(propertyId, Doubles.stringConverter(), defaultValue);
	}

	@Override
	public void registerBigDecimal(PropertyRegistryKey<BigDecimal> propertyId) {
		registerBigDecimal(propertyId, null);
	}

	@Override
	public void registerBigDecimal(PropertyRegistryKey<BigDecimal> propertyId, BigDecimal defaultValue) {
		registerProperty(propertyId, StringBigDecimalConverter.get(), defaultValue);
	}

	@Override
	public void registerBoolean(PropertyRegistryKey<Boolean> propertyId) {
		registerBoolean(propertyId, null);
	}

	@Override
	public void registerBoolean(PropertyRegistryKey<Boolean> propertyId, Boolean defaultValue) {
		registerProperty(propertyId, StringBooleanConverter.get(), defaultValue);
	}

	@Override
	public <E extends Enum<E>> void registerEnum(PropertyRegistryKey<E> propertyId, Class<E> clazz) {
		registerEnum(propertyId, clazz, null);
	}

	@Override
	public <E extends Enum<E>> void registerEnum(PropertyRegistryKey<E> propertyId, Class<E> clazz, E defaultValue) {
		registerProperty(propertyId, Enums.stringConverter(clazz), defaultValue);
	}

	@Override
	public void registerDate(PropertyRegistryKey<Date> propertyId) {
		registerDate(propertyId, (Date) null);
	}

	@Override
	public void registerDate(PropertyRegistryKey<Date> propertyId, String defaultValue) {
		registerDate(propertyId, StringDateConverter.get().convert(defaultValue));
	}

	@Override
	public void registerDate(PropertyRegistryKey<Date> propertyId, Date defaultValue) {
		registerProperty(propertyId, StringDateConverter.get(), defaultValue);
	}

	@Override
	public void registerDateTime(PropertyRegistryKey<Date> propertyId) {
		registerDateTime(propertyId, null);
	}

	@Override
	public void registerDateTime(PropertyRegistryKey<Date> propertyId, Date defaultValue) {
		registerProperty(propertyId, StringDateTimeConverter.get(), defaultValue);
	}

	@Override
	public void registerLocale(PropertyRegistryKey<Locale> propertyId) {
		registerLocale(propertyId, null);
	}

	@Override
	public void registerLocale(PropertyRegistryKey<Locale> propertyId, Locale defaultValue) {
		registerProperty(propertyId, StringLocaleConverter.get(), defaultValue);
	}

	@Override
	public void registerDirectoryFile(PropertyRegistryKey<File> propertyId) {
		registerDirectoryFile(propertyId, null);
	}

	@Override
	public void registerDirectoryFile(PropertyRegistryKey<File> propertyId, File defaultValue) {
		registerProperty(propertyId, StringDirectoryFileConverter.get(), defaultValue);
	}

	@Override
	public void registerURI(PropertyRegistryKey<URI> propertyId) {
		registerURI(propertyId, null);
	}

	@Override
	public void registerURI(PropertyRegistryKey<URI> propertyId, URI defaultValue) {
		registerProperty(propertyId, StringURIConverter.get(), defaultValue);
	}

	@Override
	public <T> T get(final PropertyId<T> propertyId) {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, Supplier<T>> information = (Pair<Converter<String, T>, Supplier<T>>) propertyInformationMap.get(propertyId.getTemplate() != null ? propertyId.getTemplate() : propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new IllegalStateException(String.format("No converter found for the property '%1s'. Undefined property.", propertyId));
		}
		
		T value = information.getValue0().convert(getAsString(propertyId));
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
		
		@SuppressWarnings("unchecked")
		final
		Pair<Converter<String, T>, Supplier<T>> information = (Pair<Converter<String, T>, Supplier<T>>) propertyInformationMap.get(propertyId.getTemplate() != null ? propertyId.getTemplate() : propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new IllegalStateException("No converter found for the property. Undefined property.");
		}
		
		mutablePropertyDao.setInTransaction(propertyId.getKey(), information.getValue0().reverse().convert(value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PropertyId<?>> listRegisteredPropertyIds() {
		return (List<PropertyId<?>>) (List<?>) Lists.newArrayList(Iterables.filter(propertyInformationMap.keySet(), PropertyId.class));
	}

	@Override
	public void clean() {
		mutablePropertyDao.cleanInTransaction();
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
