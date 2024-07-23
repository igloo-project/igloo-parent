package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.datepickersync;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

public final class DatePickerSyncResourceReference
    extends AbstractCoreJQueryPluginResourceReference {

  private static final long serialVersionUID = 3479345888453278033L;

  private static final DatePickerSyncResourceReference INSTANCE =
      new DatePickerSyncResourceReference();

  public DatePickerSyncResourceReference() {
    super(DatePickerSyncResourceReference.class, "jquery.datePickerSync.js");
  }

  public static DatePickerSyncResourceReference get() {
    return INSTANCE;
  }
}
