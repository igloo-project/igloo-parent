package fr.openwide.core.jpa.more.business.property.service;

import com.google.common.base.Converter;

import fr.openwide.core.jpa.more.business.property.model.PropertyId;

public interface IPropertyRegistry {

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter, String defaultValue);

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter);

	void register(PropertyId<String> propertyId, String defaultValueAsString);

	void register(PropertyId<String> propertyId);

}
