package fr.openwide.core.jpa.more.business.property.service;

import java.util.Date;

import com.google.common.base.Converter;
import com.google.common.base.Supplier;

import fr.openwide.core.jpa.more.business.property.model.PropertyId;

public interface IPropertyRegistry {

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter);

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter, T defaultValue);

	<T> void register(PropertyId<T> propertyId, Converter<String, T> converter, Supplier<T> defaultValueSupplier);

	void registerString(PropertyId<String> propertyId);

	void registerString(PropertyId<String> propertyId, String defaultValue);

	void registerLong(PropertyId<Long> propertyId);

	void registerLong(PropertyId<Long> propertyId, Long defaultValue);

	void registerInteger(PropertyId<Integer> propertyId);

	void registerInteger(PropertyId<Integer> propertyId, Integer defaultValue);

	void registerFloat(PropertyId<Float> propertyId);

	void registerFloat(PropertyId<Float> propertyId, Float defaultValue);

	void registerDouble(PropertyId<Double> propertyId);

	void registerDouble(PropertyId<Double> propertyId, Double defaultValue);

	void registerBoolean(PropertyId<Boolean> propertyId);

	void registerBoolean(PropertyId<Boolean> propertyId, Boolean defaultValue);

	void registerDate(PropertyId<Date> propertyId);

	void registerDate(PropertyId<Date> propertyId, Date defaultValue);

}
