package fr.openwide.core.jpa.more.business.property.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.dao.IMutablePropertyDao;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;

@Service("propertyService")
public class PropertyServiceImpl implements IPropertyService {

	private final Map<PropertyId<?>, Converter<String, ?>> converterMap = Maps.newHashMap();

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	@Override
	public void register(PropertyId<String> propertyId, String defaultValueAsString) {
		register(propertyId, Converter.<String>identity(), defaultValueAsString);
	}

	@Override
	public void register(PropertyId<String> propertyId) {
		register(propertyId, Converter.<String>identity());
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter, String defaultValueAsString) {
		register(propertyId, converter);
	}

	@Override
	public <T> void register(PropertyId<T> propertyId, Converter<String, T> converter) {
		Preconditions.checkNotNull(propertyId);
		Preconditions.checkNotNull(converter);
		converterMap.put(propertyId, converter);
	}

	@Override
	public <T, P extends PropertyId<T>> T get(P propertyId) {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Converter<String, T> converter = (Converter<String, T>) converterMap.get(propertyId);
		
		if (converter == null) {
			throw new IllegalStateException("Aucun converter renseigné pour la propriété. Propriété non définie.");
		}
		
		String valueAsString = null;
		if (propertyId instanceof ImmutablePropertyId) {
			valueAsString = immutablePropertyDao.get(propertyId.getKey());
		} else if (propertyId instanceof MutablePropertyId) {
			valueAsString = mutablePropertyDao.get(propertyId.getKey());
		} else {
			throw new IllegalStateException("Type de propriété inconnu.");
		}
		
		return converter.convert(valueAsString);
	}

	@Override
	public <T> void set(MutablePropertyId<T> propertyId, T value) {
		Preconditions.checkNotNull(propertyId);
		
		@SuppressWarnings("unchecked")
		Converter<String, T> converter = (Converter<String, T>) converterMap.get(propertyId);
		
		if (converter == null) {
			throw new IllegalStateException("Aucun converter renseigné pour la propriété. Propriété non définie.");
		}
		
		mutablePropertyDao.set(propertyId.getKey(), converter.reverse().convert(value));
	}

}
