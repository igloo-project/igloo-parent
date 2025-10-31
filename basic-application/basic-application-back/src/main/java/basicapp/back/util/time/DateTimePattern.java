package basicapp.back.util.time;

import java.util.Objects;
import org.iglooproject.commons.util.time.IDateTimePattern;

public enum DateTimePattern implements IDateTimePattern {
  DATE("datetime.pattern.date"),
  DATE_SHORT("datetime.pattern.date.short"),
  DATETIME("datetime.pattern.dateTime"),
  DATETIME_SHORT("datetime.pattern.dateTime.short"),
  YEAR_MONTH("datetime.pattern.monthYear"),
  YEAR("datetime.pattern.year"),
  TIME("datetime.pattern.time");

  private String key;

  private DateTimePattern(String key) {
    this.key = Objects.requireNonNull(key);
  }

  @Override
  public String getKey() {
    return key;
  }
}
