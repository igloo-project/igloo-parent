package fr.openwide.core.jpa.more.business.property.service;

import java.util.Map;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Converter;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.dao.IMutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;

@Service("propertyService")
public class PropertyServiceImpl implements IConfigurablePropertyService {

	private final Map<PropertyId<?>, Pair<? extends Converter<String, ?>, ?>> propertyInformationMap = Maps.newHashMap();

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	@Override
	public void register(PropertyId<String> propertyId) {
		register(propertyId, Converter.<String>identity());
	}

	@Override
	public void register(PropertyId<String> propertyId, String defaultValue) {
		register(propertyId, Converter.<String>identity(), defaultValue);
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter) {
		register(propertyId, converter, null);
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter, final T defaultValue) {
		Preconditions.checkNotNull(propertyId);
		Preconditions.checkNotNull(converter);
		propertyInformationMap.put(propertyId, Pair.with(converter, defaultValue));
	}

	@Override
	public <T> T get(PropertyId<T> propertyId) {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, T> information = (Pair<Converter<String, T>, T>) propertyInformationMap.get(propertyId);
		
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
		
		return Optional.fromNullable(information.getValue0().convert(valueAsString)).or(Optional.fromNullable(information.getValue1())).orNull();
	}

	@Override
	public <T> void set(MutablePropertyId<T> propertyId, T value) throws ServiceException, SecurityServiceException {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Pair<Converter<String, T>, T> information = (Pair<Converter<String, T>, T>) propertyInformationMap.get(propertyId);
		
		if (information == null || information.getValue0() == null) {
			throw new IllegalStateException("No converter found for the property. Undefined property.");
		}
		
		mutablePropertyDao.set(propertyId.getKey(), information.getValue0().reverse().convert(value));
	}

}
