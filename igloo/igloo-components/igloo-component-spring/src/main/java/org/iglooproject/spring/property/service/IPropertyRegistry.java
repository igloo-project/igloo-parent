package org.iglooproject.spring.property.service;

import com.google.common.base.Converter;
import com.google.common.base.Enums;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.io.File;
import java.io.FileFilter;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.functional.converter.StringBigDecimalConverter;
import org.iglooproject.functional.converter.StringBooleanConverter;
import org.iglooproject.functional.converter.StringDateConverter;
import org.iglooproject.functional.converter.StringDateTimeConverter;
import org.iglooproject.functional.converter.StringDirectoryFileCreatingConverter;
import org.iglooproject.functional.converter.StringLocaleConverter;
import org.iglooproject.functional.converter.StringTimeConverter;
import org.iglooproject.functional.converter.StringURIConverter;
import org.iglooproject.spring.property.model.IImmutablePropertyRegistryKey;
import org.iglooproject.spring.property.model.IMutablePropertyRegistryKey;
import org.iglooproject.spring.property.model.IPropertyRegistryKey;

public interface IPropertyRegistry {

  <T> void register(IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter);

  <T> void register(
      IMutablePropertyRegistryKey<T> propertyId, Converter<String, T> converter, T defaultValue);

  <T> void register(
      IMutablePropertyRegistryKey<T> propertyId,
      Converter<String, T> converter,
      Supplier2<? extends T> defaultValueSupplier);

  <T> void register(
      IImmutablePropertyRegistryKey<T> propertyId, Function2<String, ? extends T> function);

  <T> void register(
      IImmutablePropertyRegistryKey<T> propertyId,
      Function2<String, ? extends T> function,
      T defaultValue);

  <T> void register(
      IImmutablePropertyRegistryKey<T> propertyId,
      Function2<String, ? extends T> function,
      Supplier2<? extends T> defaultValueSupplier);

  void registerString(IPropertyRegistryKey<String> propertyId);

  void registerString(IPropertyRegistryKey<String> propertyId, String defaultValue);

  /**
   * Property must use the following format : {@link Long#decode(String)}
   *
   * @see Longs#stringConverter()
   */
  void registerLong(IPropertyRegistryKey<Long> propertyId);

  /**
   * Property must use the following format : {@link Long#decode(String)}
   *
   * @see Longs#stringConverter()
   */
  void registerLong(IPropertyRegistryKey<Long> propertyId, Long defaultValue);

  /**
   * Property must use the following format : {@link Integer#decode(String)}
   *
   * @see Ints#stringConverter()
   */
  void registerInteger(IPropertyRegistryKey<Integer> propertyId);

  /**
   * Property must use the following format : {@link Integer#decode(String)}
   *
   * @see Ints#stringConverter()
   */
  void registerInteger(IPropertyRegistryKey<Integer> propertyId, Integer defaultValue);

  /**
   * Property must use the following format : {@link Float#valueOf(String)}
   *
   * @see Floats#stringConverter()
   */
  void registerFloat(IPropertyRegistryKey<Float> propertyId);

  /**
   * Property must use the following format : {@link Float#valueOf(String)}
   *
   * @see Floats#stringConverter()
   */
  void registerFloat(IPropertyRegistryKey<Float> propertyId, Float defaultValue);

  /**
   * Property must use the following format : {@link Double#valueOf(String)}
   *
   * @see Doubles#stringConverter()
   */
  void registerDouble(IPropertyRegistryKey<Double> propertyId);

  /**
   * Property must use the following format : {@link Double#valueOf(String)}
   *
   * @see Doubles#stringConverter()
   */
  void registerDouble(IPropertyRegistryKey<Double> propertyId, Double defaultValue);

  /**
   * Property must use the following format : {@link BigDecimal#BigDecimal(String)}
   *
   * @see StringBigDecimalConverter
   */
  void registerBigDecimal(IPropertyRegistryKey<BigDecimal> propertyId);

  /**
   * Property must use the following format : {@link BigDecimal#BigDecimal(String)}
   *
   * @see StringBigDecimalConverter
   */
  void registerBigDecimal(IPropertyRegistryKey<BigDecimal> propertyId, BigDecimal defaultValue);

  /**
   * Property must use the following format : true/false, 1/0, yes/no, on/off
   *
   * @see StringBooleanConverter
   */
  void registerBoolean(IPropertyRegistryKey<Boolean> propertyId);

  /**
   * Property must use the following format : true/false, 1/0, yes/no, on/off
   *
   * @see StringBooleanConverter
   */
  void registerBoolean(IPropertyRegistryKey<Boolean> propertyId, Boolean defaultValue);

  /**
   * Property must use the following format : any identifier used to declare an enum constant in
   * this type
   *
   * @see Enums#stringConverter(Class)
   */
  <E extends Enum<E>> void registerEnum(IPropertyRegistryKey<E> propertyId, Class<E> clazz);

  /**
   * Property must use the following format : any identifier used to declare an enum constant in
   * this type
   *
   * @see Enums#stringConverter(Class)
   */
  <E extends Enum<E>> void registerEnum(
      IPropertyRegistryKey<E> propertyId, Class<E> clazz, E defaultValue);

  /**
   * Property must use the following format : yyyy-MM-dd
   *
   * @see StringDateConverter
   */
  void registerDate(IPropertyRegistryKey<Date> propertyId);

  /**
   * Property and {@code defaultValue} must use the following format : yyyy-MM-dd
   *
   * @see StringDateConverter
   */
  void registerDate(IPropertyRegistryKey<Date> propertyId, String defaultValue);

  /**
   * Property must use the following format : yyyy-MM-dd
   *
   * @see StringDateConverter
   */
  void registerDate(IPropertyRegistryKey<Date> propertyId, Date defaultValue);

  /**
   * Property must use the following format : HH:mm or HH:mm:ss
   *
   * @see StringTimeConverter
   */
  void registerTime(IPropertyRegistryKey<Date> propertyId);

  /**
   * Property and {@code defaultValue} must use the following format : HH:mm or HH:mm:ss
   *
   * @see StringTimeConverter
   */
  void registerTime(IPropertyRegistryKey<Date> propertyId, String defaultValue);

  /**
   * Property must use the following format : HH:mm or HH:mm:ss
   *
   * @see StringTimeConverter
   */
  void registerTime(IPropertyRegistryKey<Date> propertyId, Date defaultValue);

  /**
   * Property must use the following format : yyyy-MM-dd HH:mm or yyyy-MM-dd HH:mm:ss
   *
   * @see StringDateTimeConverter
   */
  void registerDateTime(IPropertyRegistryKey<Date> propertyId);

  /**
   * Property must use the following format : yyyy-MM-dd HH:mm or yyyy-MM-dd HH:mm:ss
   *
   * @see StringDateTimeConverter
   */
  void registerDateTime(IPropertyRegistryKey<Date> propertyId, Date defaultValue);

  void registerInstant(IPropertyRegistryKey<Instant> propertyId);

  void registerInstant(IPropertyRegistryKey<Instant> propertyId, Instant defaultValue);

  void registerLocalDate(IPropertyRegistryKey<LocalDate> propertyId);

  void registerLocalDate(IPropertyRegistryKey<LocalDate> propertyId, LocalDate defaultValue);

  void registerLocalDateTime(IPropertyRegistryKey<LocalDateTime> propertyId);

  void registerLocalDateTime(
      IPropertyRegistryKey<LocalDateTime> propertyId, LocalDateTime defaultValue);

  void registerLocalTime(IPropertyRegistryKey<LocalTime> propertyId);

  void registerLocalTime(IPropertyRegistryKey<LocalTime> propertyId, LocalTime defaultValue);

  /**
   * Property must use the following format : {@link Locale#toLanguageTag()}
   *
   * @see StringLocaleConverter
   */
  void registerLocale(IPropertyRegistryKey<Locale> propertyId);

  /**
   * Property must use the following format : {@link Locale#toLanguageTag()}
   *
   * @see StringLocaleConverter
   */
  void registerLocale(IPropertyRegistryKey<Locale> propertyId, Locale defaultValue);

  /**
   * Property must use the following format : file path
   *
   * @see StringDirectoryFileCreatingConverter
   */
  void registerFile(IPropertyRegistryKey<File> propertyId, FileFilter filter);

  /**
   * Property must use the following format : file path
   *
   * @see StringDirectoryFileCreatingConverter
   */
  void registerFile(IPropertyRegistryKey<File> propertyId, FileFilter filter, File defaultValue);

  /**
   * Property must use the following format : directory path
   *
   * @see StringDirectoryFileCreatingConverter
   */
  void registerWriteableDirectoryFile(IPropertyRegistryKey<File> propertyId);

  /**
   * Property must use the following format : directory path
   *
   * @see StringDirectoryFileCreatingConverter
   */
  void registerWriteableDirectoryFile(IPropertyRegistryKey<File> propertyId, File defaultValue);

  /**
   * Property must use the following format : {@link URI#URI(String)}
   *
   * @see StringURIConverter
   */
  void registerURI(IPropertyRegistryKey<URI> propertyId);

  /**
   * Property must use the following format : {@link URI#URI(String)}
   *
   * @see StringURIConverter
   */
  void registerURI(IPropertyRegistryKey<URI> propertyId, URI defaultValue);

  List<IPropertyRegistryKey<?>> listRegistered();
}
