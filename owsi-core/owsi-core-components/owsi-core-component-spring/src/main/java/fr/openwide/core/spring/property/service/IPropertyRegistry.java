package fr.openwide.core.spring.property.service;

import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;
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
import fr.openwide.core.commons.util.functional.converter.StringDirectoryFileCreatingConverter;
import fr.openwide.core.commons.util.functional.converter.StringLocaleConverter;
import fr.openwide.core.commons.util.functional.converter.StringURIConverter;
import fr.openwide.core.spring.property.model.IImmutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.IMutablePropertyRegistryKey;
import fr.openwide.core.spring.property.model.IPropertyRegistryKey;

public interface IPropertyRegistry {

	<T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter);

	<T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, T defaultValue);

	<T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, Supplier<? extends T> defaultValueSupplier);

	<T> void register(IImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function);

	<T> void register(IImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function, T defaultValue);

	<T> void register(IImmutablePropertyRegistryKey<T> propertyId, Function<String, ? extends T> function, Supplier<? extends T> defaultValueSupplier);

	void registerString(IPropertyRegistryKey<String> propertyId);

	void registerString(IPropertyRegistryKey<String> propertyId, String defaultValue);

	/**
	 * Property must use the following format : {@link Long#decode(String)}
	 * @see Longs#stringConverter()
	 */
	void registerLong(IPropertyRegistryKey<Long> propertyId);

	/**
	 * Property must use the following format : {@link Long#decode(String)}
	 * @see Longs#stringConverter()
	 */
	void registerLong(IPropertyRegistryKey<Long> propertyId, Long defaultValue);

	/**
	 * Property must use the following format : {@link Integer#decode(String)}
	 * @see Ints#stringConverter()
	 */
	void registerInteger(IPropertyRegistryKey<Integer> propertyId);

	/**
	 * Property must use the following format : {@link Integer#decode(String)}
	 * @see Ints#stringConverter()
	 */
	void registerInteger(IPropertyRegistryKey<Integer> propertyId, Integer defaultValue);

	/**
	 * Property must use the following format : {@link Float#valueOf(String)}
	 * @see Floats#stringConverter()
	 */
	void registerFloat(IPropertyRegistryKey<Float> propertyId);

	/**
	 * Property must use the following format : {@link Float#valueOf(String)}
	 * @see Floats#stringConverter()
	 */
	void registerFloat(IPropertyRegistryKey<Float> propertyId, Float defaultValue);

	/**
	 * Property must use the following format : {@link Double#valueOf(String)}
	 * @see Doubles#stringConverter()
	 */
	void registerDouble(IPropertyRegistryKey<Double> propertyId);

	/**
	 * Property must use the following format : {@link Double#valueOf(String)}
	 * @see Doubles#stringConverter()
	 */
	void registerDouble(IPropertyRegistryKey<Double> propertyId, Double defaultValue);

	/**
	 * Property must use the following format : {@link BigDecimal#BigDecimal(String)}
	 * @see StringBigDecimalConverter
	 */
	void registerBigDecimal(IPropertyRegistryKey<BigDecimal> propertyId);

	/**
	 * Property must use the following format : {@link BigDecimal#BigDecimal(String)}
	 * @see StringBigDecimalConverter
	 */
	void registerBigDecimal(IPropertyRegistryKey<BigDecimal> propertyId, BigDecimal defaultValue);

	/**
	 * Property must use the following format : true/false, 1/0, yes/no, on/off
	 * @see StringBooleanConverter
	 */
	void registerBoolean(IPropertyRegistryKey<Boolean> propertyId);

	/**
	 * Property must use the following format : true/false, 1/0, yes/no, on/off
	 * @see StringBooleanConverter
	 */
	void registerBoolean(IPropertyRegistryKey<Boolean> propertyId, Boolean defaultValue);

	/**
	 * Property must use the following format : any identifier used to declare an enum constant in this type
	 * @see Enums#stringConverter(Class)
	 */
	<E extends Enum<E>> void registerEnum(IPropertyRegistryKey<E> propertyId, Class<E> clazz);

	/**
	 * Property must use the following format : any identifier used to declare an enum constant in this type
	 * @see Enums#stringConverter(Class)
	 */
	<E extends Enum<E>> void registerEnum(IPropertyRegistryKey<E> propertyId, Class<E> clazz, E defaultValue);

	/**
	 * Property must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(IPropertyRegistryKey<Date> propertyId);

	/**
	 * Property and {@code defaultValue} must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(IPropertyRegistryKey<Date> propertyId, String defaultValue);

	/**
	 * Property must use the following format : yyyy-MM-dd
	 * @see StringDateConverter
	 */
	void registerDate(IPropertyRegistryKey<Date> propertyId, Date defaultValue);

	/**
	 * Property must use the following format : yyyy-MM-dd HH:mm or yyyy-MM-dd HH:mm:ss
	 * @see StringDateTimeConverter
	 */
	void registerDateTime(IPropertyRegistryKey<Date> propertyId);

	/**
	 * Property must use the following format : yyyy-MM-dd HH:mm or yyyy-MM-dd HH:mm:ss
	 * @see StringDateTimeConverter
	 */
	void registerDateTime(IPropertyRegistryKey<Date> propertyId, Date defaultValue);

	/**
	 * Property must use the following format : {@link Locale#toLanguageTag()}
	 * @see StringLocaleConverter
	 */
	void registerLocale(IPropertyRegistryKey<Locale> propertyId);

	/**
	 * Property must use the following format : {@link Locale#toLanguageTag()}
	 * @see StringLocaleConverter
	 */
	void registerLocale(IPropertyRegistryKey<Locale> propertyId, Locale defaultValue);

	/**
	 * Property must use the following format : file path
	 * @see StringDirectoryFileCreatingConverter
	 */
	void registerFile(IPropertyRegistryKey<File> propertyId, FileFilter filter);

	/**
	 * Property must use the following format : file path
	 * @see StringDirectoryFileCreatingConverter
	 */
	void registerFile(IPropertyRegistryKey<File> propertyId, FileFilter filter, File defaultValue);

	/**
	 * Property must use the following format : directory path
	 * @see StringDirectoryFileCreatingConverter
	 */
	void registerWriteableDirectoryFile(IPropertyRegistryKey<File> propertyId);

	/**
	 * Property must use the following format : directory path
	 * @see StringDirectoryFileCreatingConverter
	 */
	void registerWriteableDirectoryFile(IPropertyRegistryKey<File> propertyId, File defaultValue);

	/**
	 * Property must use the following format : {@link URI#URI(String)}
	 * @see StringURIConverter
	 */
	void registerURI(IPropertyRegistryKey<URI> propertyId);

	/**
	 * Property must use the following format : {@link URI#URI(String)}
	 * @see StringURIConverter
	 */
	void registerURI(IPropertyRegistryKey<URI> propertyId, URI defaultValue);

	List<IPropertyRegistryKey<?>> listRegistered();

}
