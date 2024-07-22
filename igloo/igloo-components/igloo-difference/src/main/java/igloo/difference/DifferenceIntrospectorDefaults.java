package igloo.difference;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class DifferenceIntrospectorDefaults {
  private DifferenceIntrospectorDefaults() {}

  public static final Set<Class<?>> SIMPLE_TYPES;

  static {
    SIMPLE_TYPES =
        Set.of(
            // partly built from de.danielbechler.util.Classes
            BigDecimal.class,
            BigInteger.class,
            CharSequence.class,
            Calendar.class,
            Date.class,
            Enum.class,
            Long.class,
            Class.class,
            URI.class,
            URL.class,
            UUID.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Float.class,
            Double.class,
            Void.class,
            Locale.class,
            LocalDateTime.class,
            LocalDate.class,
            Instant.class);
  }

  public static void ignoreCommonFields(DifferenceIntrospector introspector) {
    introspector.getIgnoredFields().add("id");
    introspector.getIgnoredFields().add("asReference");
    introspector.getIgnoredFields().add("hashCodeBinding");
    introspector.getIgnoredFields().add("toStringBinding");
    introspector.getIgnoredFields().add("isNew");
  }
}
