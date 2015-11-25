package fr.openwide.core.jpa.more.business.property.service;

import com.google.common.base.Converter;

import fr.openwide.core.jpa.more.business.property.model.PropertyId;

public interface IPropertyRegistry {

	void register(PropertyId<String> propertyId, String defaultValue);

	void register(PropertyId<String> propertyId);

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter);

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter, T defaultValue);

}
