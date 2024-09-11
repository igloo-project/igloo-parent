package test;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.iglooproject.spring.config.util.LocaleTimeZoneCharsetCheckerListener;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

/** Il n'est pas possible dans les tests de basculer le charset au runtime. */
class TestLocaleTimezoneCharset {

  @Test
  void test_ok() {
    var environment = new MockEnvironment();
    environment.setProperty("igloo.checks.locale", "fr_FR");
    environment.setProperty("igloo.checks.timezone", "Europe/Paris");
    var checker = new LocaleTimeZoneCharsetCheckerListener(environment);

    Locale.setDefault(Locale.FRANCE);
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    Assertions.assertThatCode(() -> checker.onApplicationEvent(null)).doesNotThrowAnyException();
  }

  @Test
  void test_charset_ok() {
    var environment = new MockEnvironment();
    environment.setProperty("igloo.checks.charset", Charset.defaultCharset().toString());
    var checker = new LocaleTimeZoneCharsetCheckerListener(environment);

    Assertions.assertThatCode(() -> checker.onApplicationEvent(null)).doesNotThrowAnyException();
  }

  @Test
  void test_empty_ignored() {
    var environment = new MockEnvironment();
    environment.setProperty("igloo.checks.locale", "  ");
    environment.setProperty("igloo.checks.timezone", "  ");
    environment.setProperty("igloo.checks.charset", "  ");
    var checker = new LocaleTimeZoneCharsetCheckerListener(environment);

    Assertions.assertThatCode(() -> checker.onApplicationEvent(null)).doesNotThrowAnyException();
  }

  @Test
  void test_locale_failure() {
    var environment = new MockEnvironment();
    environment.setProperty("igloo.checks.locale", "fr_FR");
    var checker = new LocaleTimeZoneCharsetCheckerListener(environment);

    Locale.setDefault(Locale.ENGLISH);
    Assertions.assertThatCode(() -> checker.onApplicationEvent(null))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("-Duser.language")
        .hasMessageContaining("-Duser.country")
        .hasMessageContaining("igloo.checks.locale");
  }

  @Test
  void test_timezone_failure() {
    var environment = new MockEnvironment();
    environment.setProperty("igloo.checks.timezone", "Europe/Paris");
    var checker = new LocaleTimeZoneCharsetCheckerListener(environment);

    TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    Assertions.assertThatCode(() -> checker.onApplicationEvent(null))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("-Duser.timezone")
        .hasMessageContaining("igloo.checks.timezone");
  }

  @Test
  void test_charset_failure() {
    var environment = new MockEnvironment();
    environment.setProperty("igloo.checks.charset", "ISO-8859-1");
    var checker = new LocaleTimeZoneCharsetCheckerListener(environment);

    Assertions.assertThatCode(() -> checker.onApplicationEvent(null))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("-Dfile.encoding")
        .hasMessageContaining("igloo.checks.charset");
  }
}
