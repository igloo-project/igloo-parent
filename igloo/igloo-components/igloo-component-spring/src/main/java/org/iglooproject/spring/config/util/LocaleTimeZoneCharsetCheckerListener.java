package org.iglooproject.spring.config.util;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TimeZone;
import org.iglooproject.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

public class LocaleTimeZoneCharsetCheckerListener
    implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LocaleTimeZoneCharsetCheckerListener.class);

  private final Environment environment;

  public LocaleTimeZoneCharsetCheckerListener(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    Locale defaultLocale = Locale.getDefault();
    TimeZone defaultTimeZone = TimeZone.getDefault();
    Charset defaultCharset = Charset.defaultCharset();

    String expectedLocale = environment.getProperty("igloo.checks.locale");
    String expectedTimeZone = environment.getProperty("igloo.checks.timezone");
    String expectedCharset = environment.getProperty("igloo.checks.charset");

    if (expectedLocale != null && StringUtils.hasText(expectedLocale)) {
      LOGGER.info("Checking locale {} with expected value {}", defaultLocale, expectedLocale);
      if (!defaultLocale.toString().equalsIgnoreCase(expectedLocale)) {
        throw new IllegalStateException(
            String.format(
                "Unexpected default locale %s (expected %s); setup with -Duser.language= and -Duser.country or igloo.checks.locale",
                defaultLocale.toString(), expectedLocale));
      }
    }
    if (expectedTimeZone != null && StringUtils.hasText(expectedTimeZone)) {
      LOGGER.info(
          "Checking timezone {} with expected value {}", defaultTimeZone.getID(), expectedTimeZone);
      if (!defaultTimeZone.getID().equalsIgnoreCase(expectedTimeZone)) {
        throw new IllegalStateException(
            String.format(
                "Unexpected default timezone %s (expected %s); setup with user -Duser.timezone= or igloo.checks.timezone",
                defaultTimeZone.getID(), expectedTimeZone));
      }
    }
    if (expectedCharset != null && StringUtils.hasText(expectedCharset)) {
      LOGGER.info("Checking charset {} with expected value {}", defaultCharset, expectedCharset);
      if (!defaultCharset.toString().equalsIgnoreCase(expectedCharset)) {
        throw new IllegalStateException(
            String.format(
                "Unexpected default charset %s (expected %s); setup with user -Dfile.encoding= or igloo.checks.charset",
                defaultCharset.toString(), expectedCharset));
      }
    }
  }
}
