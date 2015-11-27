package fr.openwide.core.jpa.more.business.property.service;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.base.Converter;
import com.google.common.base.Enums;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import fr.openwide.core.commons.util.functional.SerializableSupplier;
import fr.openwide.core.commons.util.functional.converter.StringBigDecimalConverter;
import fr.openwide.core.commons.util.functional.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateTimeConverter;
import fr.openwide.core.commons.util.functional.converter.StringDirectoryFileConverter;
import fr.openwide.core.commons.util.functional.converter.StringURIConverter;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.jpa.more.business.parameter.service.IAbstractParameterService;
import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.dao.IMutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyRegistryKey;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyRegistryKey;
import fr.openwide.core.jpa.more.config.spring.event.PropertyServiceInitEvent;
import fr.openwide.core.spring.config.CoreConfigurer;

/**
 * Use this service to retrieve application properties instead of using {@link CoreConfigurer} or {@link IAbstractParameterService}.
 * It handles both mutable and immutable properties ; immutable properties are retrieved from properties resources files
 * ({@link IImmutablePropertyDao}) and mutable properties are related to {@link Parameter} ({@link IMutablePropertyDao}).
 * @see {@link IPropertyRegistry} to register application properties.
 */
@Service("propertyService")
public class PropertyServiceImpl implements IConfigurablePropertyService, ApplicationEventPublisherAware {

	private final Map<PropertyRegistryKey<?>, Pair<? extends Converter<String, ?>, ? extends Supplier<?>>> propertyInformationMap = Maps.newHashMap();

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;
	
	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	private ApplicationEventPublisher applicationEventPublisher;

	private TransactionTemplate readOnlyTransactionTemplate;

	private TransactionTemplate writeTransactionTemplate;

	@PostConstruct
	public void init() {
		applicationEventPublisher.publishEvent(new PropertyServiceInitEvent(this));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public <T> void register(PropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter) {
		register(propertyId, converter, (T) null);
	}

	@Override
	public <T> void register(PropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter, final T defaultValue) {
		register(propertyId, converter, new SerializableSupplier<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public T get() {
				return defaultValue;
			}
		});
	}

	@Override
	public <T> void registerImmutable(ImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function) {
		registerImmutable(propertyId, function, null);
	}

	@Override
	public <T> void registerImmutable(ImmutablePropertyRegistryKey<T> propertyId, final Function<String, ? extends T> function, final T defaultValue) {
		register(
				propertyId,
				new Converter<String, T>() {
					@Override
					protected T doForward(String a) {
						return function.apply(a);
					}
					@Override
					protected String doBackward(T b) {
						throw new IllegalStateException("Unable to update immutable property.");
					}
				},
				defaultValue
		);
	}

	@Override
	public <T> void register(PropertyRegistryKey<T> propertyId, Converter<String, ? extends T> converter, Supplier<? extends T> defaultValueSupplier) {
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
		register(propertyId, Converter.<String>identity(), defaultValue);
	}

	@Override
	public void registerLong(PropertyRegistryKey<Long> propertyId) {
		registerLong(propertyId, null);
	}

	@Override
	public void registerLong(PropertyRegistryKey<Long> propertyId, Long defaultValue) {
		register(propertyId, Longs.stringConverter(), defaultValue);
	}

	@Override
	public void registerInteger(PropertyRegistryKey<Integer> propertyId) {
		registerInteger(propertyId, null);
	}

	@Override
	public void registerInteger(PropertyRegistryKey<Integer> propertyId, Integer defaultValue) {
		register(propertyId, Ints.stringConverter(), defaultValue);
	}

	@Override
	public void registerFloat(PropertyRegistryKey<Float> propertyId) {
		registerFloat(propertyId, null);
	}

	@Override
	public void registerFloat(PropertyRegistryKey<Float> propertyId, Float defaultValue) {
		register(propertyId, Floats.stringConverter(), defaultValue);
	}

	@Override
	public void registerDouble(PropertyRegistryKey<Double> propertyId) {
		registerDouble(propertyId, null);
	}

	@Override
	public void registerDouble(PropertyRegistryKey<Double> propertyId, Double defaultValue) {
		register(propertyId, Doubles.stringConverter(), defaultValue);
	}

	@Override
	public void registerBigDecimal(PropertyRegistryKey<BigDecimal> propertyId) {
		registerBigDecimal(propertyId, null);
	}

	@Override
	public void registerBigDecimal(PropertyRegistryKey<BigDecimal> propertyId, BigDecimal defaultValue) {
		register(propertyId, StringBigDecimalConverter.get(), defaultValue);
	}

	@Override
	public void registerBoolean(PropertyRegistryKey<Boolean> propertyId) {
		registerBoolean(propertyId, null);
	}

	@Override
	public void registerBoolean(PropertyRegistryKey<Boolean> propertyId, Boolean defaultValue) {
		register(propertyId, StringBooleanConverter.get(), defaultValue);
	}

	@Override
	public <E extends Enum<E>> void registerEnum(PropertyRegistryKey<E> propertyId, Class<E> clazz) {
		registerEnum(propertyId, clazz, null);
	}

	@Override
	public <E extends Enum<E>> void registerEnum(PropertyRegistryKey<E> propertyId, Class<E> clazz, E defaultValue) {
		register(propertyId, Enums.stringConverter(clazz), defaultValue);
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
		register(propertyId, StringDateConverter.get(), defaultValue);
	}

	@Override
	public void registerDateTime(PropertyRegistryKey<Date> propertyId) {
		registerDateTime(propertyId, null);
	}

	@Override
	public void registerDateTime(PropertyRegistryKey<Date> propertyId, Date defaultValue) {
		register(propertyId, StringDateTimeConverter.get(), defaultValue);
	}

	@Override
	public void registerDirectoryFile(PropertyRegistryKey<File> propertyId) {
		registerDirectoryFile(propertyId, null);
	}

	@Override
	public void registerDirectoryFile(PropertyRegistryKey<File> propertyId, File defaultValue) {
		register(propertyId, StringDirectoryFileConverter.get(), defaultValue);
	}

	@Override
	public void registerURI(PropertyRegistryKey<URI> propertyId) {
		registerURI(propertyId, null);
	}

	@Override
	public void registerURI(PropertyRegistryKey<URI> propertyId, URI defaultValue) {
		register(propertyId, StringURIConverter.get(), defaultValue);
	}

	@Override
	public <T> T get(final PropertyId<T> propertyId) {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, Supplier<T>> information = (Pair<Converter<String, T>, Supplier<T>>) propertyInformationMap.get(propertyId.getTemplate() != null ? propertyId.getTemplate() : propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new IllegalStateException(String.format("No converter found for the property '%1s'. Undefined property.", propertyId));
		}
		
		String valueAsString = null;
		if (propertyId instanceof ImmutablePropertyId) {
			valueAsString = immutablePropertyDao.get(propertyId.getKey());
		} else if (propertyId instanceof MutablePropertyId) {
			valueAsString = readOnlyTransactionTemplate.execute(new TransactionCallback<String>() {
				@Override
				public String doInTransaction(TransactionStatus status) {
					return mutablePropertyDao.get(propertyId.getKey());
				}
			});
		} else {
			throw new IllegalStateException(String.format("Unknown type of property : '%1s'.", propertyId));
		}
		
		return Optional.fromNullable(information.getValue0().convert(valueAsString)).or(Optional.fromNullable(information.getValue1().get())).orNull();
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
		
		writeTransactionTemplate.execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						try {
							mutablePropertyDao.set(propertyId.getKey(), information.getValue0().reverse().convert(value));
							return;
						} catch (Exception e) {
							throw new IllegalStateException(String.format("Error while updating property '%1s'.", propertyId), e);
						}
						
					}
				}
		);
	}

	@Autowired
	public void setPlatformTransactionManager(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute readOnlyTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		readOnlyTransactionAttribute.setReadOnly(true);
		readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);
		
		DefaultTransactionAttribute writeTransactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
	}

}
