package basicapp.front.common.converter;

import basicapp.back.util.time.DateTimePattern;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.wicket.Localizer;
import org.iglooproject.commons.util.time.IDateTimePattern;

public class LocalTimeConverter
    extends org.apache.wicket.util.convert.converter.LocalTimeConverter {

  private static final long serialVersionUID = -7041118942951785903L;

  private static final LocalTimeConverter INSTANCE = new LocalTimeConverter(DateTimePattern.TIME);

  public static LocalTimeConverter get() {
    return INSTANCE;
  }

  private final IDateTimePattern dateTimePattern;

  public LocalTimeConverter(IDateTimePattern dateTimePattern) {
    this.dateTimePattern = Objects.requireNonNull(dateTimePattern);
  }

  @Override
  protected DateTimeFormatter getDateTimeFormatter() {
    return DateTimeFormatter.ofPattern(Localizer.get().getString(dateTimePattern.getKey(), null));
  }
}
