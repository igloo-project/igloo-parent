package fr.openwide.core.jpa.more.business.property.service;

import java.util.Date;

import com.google.common.base.Converter;
import com.google.common.base.Function;
import com.google.common.base.Supplier;

import fr.openwide.core.commons.util.functional.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateTimeConverter;
import fr.openwide.core.jpa.more.business.property.model.ImmutablePropertyRegistryKey;
import fr.openwide.core.jpa.more.business.property.model.PropertyRegistryKey;

public interface IPropertyRegistry {

	<T> void register(PropertyRegistryKey<T> propertyId, Converter<String, T> converter);

	<T> void register(PropertyRegistryKey<T> propertyId, Converter<String, T> converter, T defaultValue);

	<T> void register(PropertyRegistryKey<T> propertyId, Converter<String, T> converter, Supplier<T> defaultValueSupplier);

	<T> void registerImmutable(ImmutablePropertyRegistryKey<T> propertyId, Function<String, T> function);

	<T> void registerImmutable(ImmutablePropertyRegistryKey<T> propertyId, Function<String, T> function, T defaultValue);

	void registerString(PropertyRegistryKey<String> propertyId);

	void registerString(PropertyRegistryKey<String> propertyId, String defaultValue);

	/**
	 * Property must use the following format : {@link Long#decode(String)}
	 */
	void registerLong(PropertyRegistryKey<Long> propertyId);

	/**
	 * Property must use the following format : {@link Long#decode(String)}
	 */
	void registerLong(PropertyRegistryKey<Long> propertyId, Long defaultValue);

	/**
	 * Property must use the following format : {@link Integer#decode(String)}
	 */
	void registerInteger(PropertyRegistryKey<Integer> propertyId);

	/**
	 * Property must use the following format : {@link Integer#decode(String)}
	 */
	void registerInteger(PropertyRegistryKey<Integer> propertyId, Integer defaultValue);

	/**
	 * Property must use the following format : {@link Float#valueOf(String)}
	 */
	void registerFloat(PropertyRegistryKey<Float> propertyId);

	/**
	 * Property must use the following format : {@link Float#valueOf(String)}
	 */
	void registerFloat(PropertyRegistryKey<Float> propertyId, Float defaultValue);

	/**
	 * Property must use the following format : {@link Double#valueOf(String)}
	 */
	void registerDouble(PropertyRegistryKey<Double> propertyId);

	/**
	 * Property must use the following format : {@link Double#valueOf(String)}
	 */
	void registerDouble(PropertyRegistryKey<Double> propertyId, Double defaultValue);

	/**
	 * Property must use the following format : true/false, 1/0, yes/no, on/off
	 * @see StringBooleanConverter
	 */
	void registerBoolean(PropertyRegistryKey<Boolean> propertyId);

	/**
	 * Property must use the following format : true/false, 1/0, yes/no, on/off
	 * @see StringBooleanConverter
	 */
	void registerBoolean(PropertyRegistryKey<Boolean> propertyId, Boolean defaultValue);

	/**
	 * Property must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(PropertyRegistryKey<Date> propertyId);

	/**
	 * Property must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(PropertyRegistryKey<Date> propertyId, Date defaultValue);

	/**
	 * Property must use the following format : yyyy-MM-dd HH:mm or yyyy-MM-dd HH:mm:ss
	 * @see StringDateTimeConverter
	 */
	void registerDateTime(PropertyRegistryKey<Date> propertyId);

	/**
	 * Property must use the following format : yyyy-MM-dd HH:mm or yyyy-MM-dd HH:mm:ss
	 * @see StringDateTimeConverter
	 */
	void registerDateTime(PropertyRegistryKey<Date> propertyId, Date defaultValue);

}
