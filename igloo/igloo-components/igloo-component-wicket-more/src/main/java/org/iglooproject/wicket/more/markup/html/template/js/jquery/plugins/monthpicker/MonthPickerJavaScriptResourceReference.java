package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.monthpicker;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;
import java.util.List;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;

public final class MonthPickerJavaScriptResourceReference
    extends AbstractCoreJQueryPluginResourceReference {

  private static final long serialVersionUID = -1451228678288396852L;

  private static final MonthPickerJavaScriptResourceReference INSTANCE =
      new MonthPickerJavaScriptResourceReference();

  private MonthPickerJavaScriptResourceReference() {
    super(MonthPickerJavaScriptResourceReference.class, "jquery.ui.monthpicker.js");
  }

  @Override
  public List<HeaderItem> getDependencies() {
    List<HeaderItem> dependencies = super.getDependencies();
    dependencies.add(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get()));
    return dependencies;
  }

  public static MonthPickerJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
