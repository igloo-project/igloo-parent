package fr.openwide.core.spring.property.service;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.Locale;

import com.google.common.base.Converter;
import com.google.common.base.Enums;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import fr.openwide.core.commons.util.functional.converter.StringBigDecimalConverter;
import fr.openwide.core.commons.util.functional.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateTimeConverter;
import fr.openwide.core.commons.util.functional.converter.StringDirectoryFileConverter;
import fr.openwide.core.commons.util.functional.converter.StringLocaleConverter;
import fr.openwide.core.commons.util.functional.converter.StringURIConverter;
import fr.openwide.core.spring.property.model.ImmutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.MutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.PropertyRegistryKey;

public interface IPropertyRegistry {

	<T> void register(MutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter);

	<T> void register(MutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, T defaultValue);

	<T> void register(MutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, Supplier<? extends T> defaultValueSupplier);

	<T> void register(ImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function);

	<T> void register(ImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function, T defaultValue);

	<T> void register(ImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function, Supplier<? extends T> defaultValueSupplier);

	void registerString(PropertyRegistryKey<String> propertyId);

	void registerString(PropertyRegistryKey<String> propertyId, String defaultValue);

	/**
	 * Property must use the following format : {@link Long#decode(String)}
	 * @see Longs#stringConverter()
	 */
	void registerLong(PropertyRegistryKey<Long> propertyId);

	/**
	 * Property must use the following format : {@link Long#decode(String)}
	 * @see Longs#stringConverter()
	 */
	void registerLong(PropertyRegistryKey<Long> propertyId, Long defaultValue);

	/**
	 * Property must use the following format : {@link Integer#decode(String)}
	 * @see Ints#stringConverter()
	 */
	void registerInteger(PropertyRegistryKey<Integer> propertyId);

	/**
	 * Property must use the following format : {@link Integer#decode(String)}
	 * @see Ints#stringConverter()
	 */
	void registerInteger(PropertyRegistryKey<Integer> propertyId, Integer defaultValue);

	/**
	 * Property must use the following format : {@link Float#valueOf(String)}
	 * @see Floats#stringConverter()
	 */
	void registerFloat(PropertyRegistryKey<Float> propertyId);

	/**
	 * Property must use the following format : {@link Float#valueOf(String)}
	 * @see Floats#stringConverter()
	 */
	void registerFloat(PropertyRegistryKey<Float> propertyId, Float defaultValue);

	/**
	 * Property must use the following format : {@link Double#valueOf(String)}
	 * @see Doubles#stringConverter()
	 */
	void registerDouble(PropertyRegistryKey<Double> propertyId);

	/**
	 * Property must use the following format : {@link Double#valueOf(String)}
	 * @see Doubles#stringConverter()
	 */
	void registerDouble(PropertyRegistryKey<Double> propertyId, Double defaultValue);

	/**
	 * Property must use the following format : {@link BigDecimal#BigDecimal(String)}
	 * @see StringBigDecimalConverter
	 */
	void registerBigDecimal(PropertyRegistryKey<BigDecimal> propertyId);

	/**
	 * Property must use the following format : {@link BigDecimal#BigDecimal(String)}
	 * @see StringBigDecimalConverter
	 */
	void registerBigDecimal(PropertyRegistryKey<BigDecimal> propertyId, BigDecimal defaultValue);

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
	 * Property must use the following format : any identifier used to declare an enum constant in this type
	 * @see Enums#stringConverter(Class)
	 */
	<E extends Enum<E>> void registerEnum(PropertyRegistryKey<E> propertyId, Class<E> clazz);

	/**
	 * Property must use the following format : any identifier used to declare an enum constant in this type
	 * @see Enums#stringConverter(Class)
	 */
	<E extends Enum<E>> void registerEnum(PropertyRegistryKey<E> propertyId, Class<E> clazz, E defaultValue);

	/**
	 * Property must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(PropertyRegistryKey<Date> propertyId);

	/**
	 * Property and {@code defaultValue} must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(PropertyRegistryKey<Date> propertyId, String defaultValue);

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

	/**
	 * Property must use the following format : {@link Locale#toLanguageTag()}
	 * @see StringLocaleConverter
	 */
	void registerLocale(PropertyRegistryKey<Locale> propertyId);

	/**
	 * Property must use the following format : {@link Locale#toLanguageTag()}
	 * @see StringLocaleConverter
	 */
	void registerLocale(PropertyRegistryKey<Locale> propertyId, Locale defaultValue);

	/**
	 * Property must use the following format : directory path
	 * @see StringDirectoryFileConverter
	 */
	void registerDirectoryFile(PropertyRegistryKey<File> propertyId);

	/**
	 * Property must use the following format : directory path
	 * @see StringDirectoryFileConverter
	 */
	void registerDirectoryFile(PropertyRegistryKey<File> propertyId, File defaultValue);

	/**
	 * Property must use the following format : {@link URI#URI(String)}
	 * @see StringURIConverter
	 */
	void registerURI(PropertyRegistryKey<URI> propertyId);

	/**
	 * Property must use the following format : {@link URI#URI(String)}
	 * @see StringURIConverter
	 */
	void registerURI(PropertyRegistryKey<URI> propertyId, URI defaultValue);

}
