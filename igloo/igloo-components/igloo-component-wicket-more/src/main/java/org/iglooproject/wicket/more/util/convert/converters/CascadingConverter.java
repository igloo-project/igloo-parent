package org.iglooproject.wicket.more.util.convert.converters;

import com.google.common.collect.ImmutableList;
import java.util.Locale;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

public class CascadingConverter<T> implements IConverter<T> {

  private static final long serialVersionUID = 4943347630093315725L;

  private final Iterable<? extends IConverter<? extends T>> preemptiveConverters;

  private final IConverter<T> mainConverter;

  private final Iterable<? extends IConverter<? extends T>> alternativeConverters;

  public CascadingConverter(
      IConverter<T> mainConverter,
      Iterable<? extends IConverter<? extends T>> preemptiveConverters,
      Iterable<? extends IConverter<? extends T>> alternativeConverters) {
    super();
    this.preemptiveConverters = ImmutableList.copyOf(preemptiveConverters);
    this.mainConverter = mainConverter;
    this.alternativeConverters = ImmutableList.copyOf(alternativeConverters);
  }

  @Override
  public T convertToObject(String value, Locale locale) throws ConversionException {
    // First try the preemptive converters
    for (IConverter<? extends T> preemptiveConverter : preemptiveConverters) {
      try {
        return preemptiveConverter.convertToObject(value, locale);
      } catch (ConversionException ignored) {
      }
    }

    // If they didn't work, ignore the errors and try the main converter
    try {
      return mainConverter.convertToObject(value, locale);
    } catch (ConversionException e) {
      // If the main converter didn't work, try an alternative converter
      for (IConverter<? extends T> alternativeConverter : alternativeConverters) {
        try {
          return alternativeConverter.convertToObject(value, locale);
        } catch (ConversionException ignored) {
          e.addSuppressed(ignored);
        }
      }
      // If no alternative converter managed to convert, just throw the original exception.
      throw e;
    }
  }

  @Override
  public String convertToString(T value, Locale locale) {
    // Just use the main converter.
    return mainConverter.convertToString(value, locale);
  }
}
