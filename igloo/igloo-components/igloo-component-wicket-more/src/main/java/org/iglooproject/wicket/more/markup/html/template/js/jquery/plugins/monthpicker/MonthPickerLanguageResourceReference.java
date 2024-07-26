package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.monthpicker;

import java.util.Locale;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.wicketstuff.wiquery.ui.datepicker.DatePickerLanguageResourceReference.DatePickerLanguages;

public final class MonthPickerLanguageResourceReference extends JavaScriptResourceReference {

  private static final long serialVersionUID = 163876806827272480L;

  protected MonthPickerLanguageResourceReference(Locale locale, String filename) {
    super(MonthPickerLanguageResourceReference.class, filename, locale, null, null);
  }

  public static MonthPickerLanguageResourceReference get(Locale locale) {
    DatePickerLanguages dpl = DatePickerLanguages.getDatePickerLanguages(locale);

    if (dpl != null) {
      return new MonthPickerLanguageResourceReference(locale, getJsFileName(dpl));
    }

    return null;
  }

  public static String getJsFileName(DatePickerLanguages dpl) {
    if (dpl == null) {
      return null;
    }

    Locale locale = dpl.getLocale();
    String country = locale.getCountry();
    String variant = locale.getVariant();
    StringBuilder js = new StringBuilder();

    js.append("i18n/jquery.ui.monthpicker.");
    js.append(locale.getLanguage());

    if (country != null && country.trim().length() > 0) {
      js.append("-").append(country);

      if (variant != null && variant.trim().length() > 0) {
        js.append("-").append(variant);
      }
    }

    js.append(".js");

    return js.toString();
  }
}
