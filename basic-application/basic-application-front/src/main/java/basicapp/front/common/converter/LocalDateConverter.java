package basicapp.front.common.converter;

import basicapp.back.util.time.DateTimePattern;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.wicket.Localizer;
import org.iglooproject.commons.util.time.IDateTimePattern;

public class LocalDateConverter
    extends org.apache.wicket.util.convert.converter.LocalDateConverter {

  private static final long serialVersionUID = 729923484832534135L;

  private static final LocalDateConverter INSTANCE =
      new LocalDateConverter(DateTimePattern.SHORT_DATE);

  public static LocalDateConverter get() {
    return INSTANCE;
  }

  private final IDateTimePattern dateTimePattern;

  public LocalDateConverter(IDateTimePattern dateTimePattern) {
    this.dateTimePattern = Objects.requireNonNull(dateTimePattern);
  }

  @Override
  protected DateTimeFormatter getDateTimeFormatter() {
    return DateTimeFormatter.ofPattern(Localizer.get().getString(dateTimePattern.getKey(), null));
  }
}
