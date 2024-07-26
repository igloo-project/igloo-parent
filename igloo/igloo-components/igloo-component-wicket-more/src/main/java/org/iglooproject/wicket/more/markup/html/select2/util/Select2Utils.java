package org.iglooproject.wicket.more.markup.html.select2.util;

import org.wicketstuff.select2.Settings;

public final class Select2Utils {

  private static final int DEFAULT_MINIMUM_RESULTS_FOR_SEARCH = 6;

  private static final int AJAX_DEFAULT_MINIMUM_INPUT_LENGTH = 2;

  public static void setDefaultSettings(Settings settings) {
    settings.setWidth("style");
    settings.setMinimumResultsForSearch(DEFAULT_MINIMUM_RESULTS_FOR_SEARCH);
    settings.setPlaceholder("");
    settings.setAllowClear(true);
    settings.setCloseOnSelect(true);
    settings.setTheme("bootstrap");
    settings.setContainerCssClass(":all:");
  }

  public static void setRequiredSettings(Settings settings) {
    if (settings.getPlaceholder() == null) {
      settings.setPlaceholder("");
    }
    settings.setAllowClear(false);
  }

  public static void setOptionalSettings(Settings settings) {
    settings.setAllowClear(true);
  }

  public static void setDefaultAjaxSettings(Settings settings) {
    setDefaultSettings(settings);
    settings.setMinimumInputLength(AJAX_DEFAULT_MINIMUM_INPUT_LENGTH);
  }

  public static void disableSearch(Settings settings) {
    settings.setMinimumResultsForSearch(Integer.MAX_VALUE);
  }

  private Select2Utils() {}
}
