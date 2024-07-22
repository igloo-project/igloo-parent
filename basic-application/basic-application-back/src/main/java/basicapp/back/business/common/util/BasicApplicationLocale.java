package basicapp.back.business.common.util;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import org.iglooproject.commons.util.ordering.SerializableCollator;

public final class BasicApplicationLocale {

  public static final Locale ENGLISH = Locale.ENGLISH;
  public static final Locale FRENCH = Locale.FRENCH;

  public static final Locale DEFAULT = FRENCH;

  public static final Map<Locale, Ordering<String>> COMPARATORS =
      ImmutableMap.<Locale, Ordering<String>>builder()
          .put(ENGLISH, initCollator(ENGLISH))
          .put(FRENCH, initCollator(FRENCH))
          .build();

  /** Every possible locale used in this application, sorted by descending order of priority. */
  public static final Collection<Locale> ALL = ImmutableList.of(FRENCH, ENGLISH);

  public static final Ordering<String> initCollator(Locale locale) {
    return new SerializableCollator(locale).nullsFirst();
  }

  public static final Ordering<String> comparator() {
    return comparator(DEFAULT);
  }

  public static final Ordering<String> comparator(Locale locale) {
    Locale localeToUse = locale;

    if (localeToUse == null) {
      localeToUse = DEFAULT;
    }
    return MoreObjects.firstNonNull(COMPARATORS.get(localeToUse), COMPARATORS.get(DEFAULT));
  }

  private BasicApplicationLocale() {}
}
