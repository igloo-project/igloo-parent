package fr.openwide.core.jpa.more.business.property.service;

import java.util.Date;
import java.util.Map;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Converter;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import fr.openwide.core.commons.util.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.SerializableSupplier;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.dao.IMutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.model.CompositeProperty;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;

@Service("propertyService")
public class PropertyServiceImpl implements IConfigurablePropertyService {

	private final Map<PropertyId<?>, Pair<? extends Converter<String, ?>, ? extends Supplier<?>>> propertyInformationMap = Maps.newHashMap();

	private final Map<CompositeProperty<?, ?, ?>, Function<? extends Pair<?, ?>, ?>> compositePropertyFunctionMap = Maps.newHashMap();

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	@Override
	public <T, A, B> void register(CompositeProperty<T, A, B> compositeProperty, Function<Pair<A, B>, T> function) {
		Preconditions.checkNotNull(compositeProperty);
		Preconditions.checkNotNull(function);
		compositePropertyFunctionMap.put(compositeProperty, function);
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter) {
		register(propertyId, converter, null);
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter, final T defaultValue) {
		register(propertyId, converter, new SerializableSupplier<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public T get() {
				return defaultValue;
			}
		});
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter, Supplier<T> defaultValueSupplier) {
		Preconditions.checkNotNull(propertyId);
		Preconditions.checkNotNull(converter);
		Preconditions.checkNotNull(defaultValueSupplier);
		propertyInformationMap.put(propertyId, Pair.with(converter, defaultValueSupplier));
	}

	@Override
	public void registerString(PropertyId<String> propertyId) {
		registerString(propertyId, null);
	}

	@Override
	public void registerString(PropertyId<String> propertyId, String defaultValue) {
		register(propertyId, Converter.<String>identity(), defaultValue);
	}

	@Override
	public void registerLong(PropertyId<Long> propertyId) {
		registerLong(propertyId, null);
	}

	@Override
	public void registerLong(PropertyId<Long> propertyId, Long defaultValue) {
		register(propertyId, Longs.stringConverter(), defaultValue);
	}

	@Override
	public void registerInteger(PropertyId<Integer> propertyId) {
		registerInteger(propertyId, null);
	}

	@Override
	public void registerInteger(PropertyId<Integer> propertyId, Integer defaultValue) {
		register(propertyId, Ints.stringConverter(), defaultValue);
	}

	@Override
	public void registerFloat(PropertyId<Float> propertyId) {
		registerFloat(propertyId, null);
	}

	@Override
	public void registerFloat(PropertyId<Float> propertyId, Float defaultValue) {
		register(propertyId, Floats.stringConverter(), defaultValue);
	}

	@Override
	public void registerDouble(PropertyId<Double> propertyId) {
		registerDouble(propertyId, null);
	}

	@Override
	public void registerDouble(PropertyId<Double> propertyId, Double defaultValue) {
		register(propertyId, Doubles.stringConverter(), defaultValue);
	}

	@Override
	public void registerBoolean(PropertyId<Boolean> propertyId) {
		registerBoolean(propertyId, null);
	}

	@Override
	public void registerBoolean(PropertyId<Boolean> propertyId, Boolean defaultValue) {
		register(propertyId, StringBooleanConverter.get(), defaultValue);
	}

	@Override
	public void registerDate(PropertyId<Date> propertyId) {
		registerDate(propertyId, null);
	}

	@Override
	public void registerDate(PropertyId<Date> propertyId, Date defaultValue) {
		register(propertyId, StringDateConverter.get(), defaultValue);
	}

	@Override
	public <T> T get(PropertyId<T> propertyId) {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, Supplier<T>> information = (Pair<Converter<String, T>, Supplier<T>>) propertyInformationMap.get(propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new IllegalStateException("No converter found for the property. Undefined property.");
		}
		
		String valueAsString = null;
		if (propertyId instanceof ImmutablePropertyId) {
			valueAsString = immutablePropertyDao.get(propertyId.getKey());
		} else if (propertyId instanceof MutablePropertyId) {
			valueAsString = mutablePropertyDao.get(propertyId.getKey());
		} else {
			throw new IllegalStateException("Unknown type of property.");
		}
		
		return Optional.fromNullable(information.getValue0().convert(valueAsString)).or(Optional.fromNullable(information.getValue1().get())).orNull();
	}

	@Override
	public <T> void set(MutablePropertyId<T> propertyId, T value) throws ServiceException, SecurityServiceException {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, Supplier<T>> information = (Pair<Converter<String, T>, Supplier<T>>) propertyInformationMap.get(propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new IllegalStateException("No converter found for the property. Undefined property.");
		}
		
		mutablePropertyDao.set(propertyId.getKey(), information.getValue0().reverse().convert(value));
	}

	@Override
	public <T, A, B> T get(CompositeProperty<T, A, B> compositeProperty) {
		@SuppressWarnings("unchecked")
		Function<Pair<A, B>, T> function = (Function<Pair<A, B>, T>) compositePropertyFunctionMap.get(compositeProperty);
		
		if (function == null) {
			throw new IllegalStateException("No converter found for the composite property. Undefined composite property.");
		}
		
		return function.apply(Pair.with(get(compositeProperty.getFirstPropertyId()), get(compositeProperty.getSecondPropertyId())));
	}

}
