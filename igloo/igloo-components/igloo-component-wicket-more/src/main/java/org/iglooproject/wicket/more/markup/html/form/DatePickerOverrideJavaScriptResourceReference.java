package org.iglooproject.wicket.more.markup.html.form;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

/**
 * Override jQuery UI Datepicker JS: override "Today" button behavior to select the current date and
 * close the datepicker on click.
 */
public final class DatePickerOverrideJavaScriptResourceReference
    extends AbstractCoreJQueryPluginResourceReference {

  private static final long serialVersionUID = -1451228678288396852L;

  private static final DatePickerOverrideJavaScriptResourceReference INSTANCE =
      new DatePickerOverrideJavaScriptResourceReference();

  private DatePickerOverrideJavaScriptResourceReference() {
    super(DatePickerOverrideJavaScriptResourceReference.class, "jquery.datePickerOverride.js");
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.add(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get()));
    return dependencies;
  }

  public static DatePickerOverrideJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
