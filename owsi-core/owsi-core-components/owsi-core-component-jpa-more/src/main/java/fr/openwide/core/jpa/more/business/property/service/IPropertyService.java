package fr.openwide.core.jpa.more.business.property.service;

import com.google.common.base.Converter;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.more.business.property.model.MutablePropertyId;
import fr.openwide.core.jpa.more.business.property.model.PropertyId;

public interface IPropertyService extends ITransactionalAspectAwareService {

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter, String defaultValue);

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter);

	void register(PropertyId<String> propertyId, String defaultValueAsString);

	void register(PropertyId<String> propertyId);

	<T, P extends PropertyId<T>> T get(P propertyId);

	<T> void set(MutablePropertyId<T> propertyId, T value);


}
