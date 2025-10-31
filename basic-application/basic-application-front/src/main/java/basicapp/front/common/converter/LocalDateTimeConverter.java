package basicapp.front.common.converter;

import basicapp.back.util.time.DateTimePattern;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.wicket.Localizer;
import org.iglooproject.commons.util.time.IDateTimePattern;

public class LocalDateTimeConverter
    extends org.apache.wicket.util.convert.converter.LocalDateTimeConverter {

  private static final long serialVersionUID = -4412976316698853032L;

  private static final LocalDateTimeConverter INSTANCE =
      new LocalDateTimeConverter(DateTimePattern.DATETIME);

  public static LocalDateTimeConverter get() {
    return INSTANCE;
  }

  private final IDateTimePattern dateTimePattern;

  public LocalDateTimeConverter(IDateTimePattern dateTimePattern) {
    this.dateTimePattern = Objects.requireNonNull(dateTimePattern);
  }

  @Override
  protected DateTimeFormatter getDateTimeFormatter() {
    return DateTimeFormatter.ofPattern(Localizer.get().getString(dateTimePattern.getKey(), null));
  }
}
