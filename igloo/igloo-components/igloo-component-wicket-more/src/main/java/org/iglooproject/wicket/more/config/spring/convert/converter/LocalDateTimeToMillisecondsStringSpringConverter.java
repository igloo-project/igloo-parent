package org.iglooproject.wicket.more.config.spring.convert.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.core.convert.converter.Converter;

public class LocalDateTimeToMillisecondsStringSpringConverter
    implements Converter<LocalDateTime, String> {

  private final ZoneId zone;

  public LocalDateTimeToMillisecondsStringSpringConverter(ZoneId zone) {
    this.zone = zone;
  }

  @Override
  public String convert(LocalDateTime source) {
    if (source == null) {
      return null;
    }

    return String.valueOf(source.atZone(zone).toInstant().toEpochMilli());
  }
}
