package basicapp.back.business.common.model.comparator;

import basicapp.back.business.common.model.embeddable.LocalizedText;
import basicapp.back.business.common.util.BasicApplicationLocale;
import java.util.Comparator;
import java.util.Locale;
import org.iglooproject.jpa.more.business.localization.util.AbstractLocalizedTextComparator;

public class LocalizedTextComparator extends AbstractLocalizedTextComparator<LocalizedText> {

  private static final long serialVersionUID = -1217040817920839219L;

  private static final LocalizedTextComparator INSTANCE =
      new LocalizedTextComparator(
          BasicApplicationLocale.DEFAULT, BasicApplicationLocale.comparator());

  public LocalizedTextComparator(Locale locale) {
    super(locale);
  }

  public LocalizedTextComparator(Locale locale, Comparator<String> comparator) {
    super(locale, comparator);
  }

  public static final LocalizedTextComparator get() {
    return INSTANCE;
  }
}
