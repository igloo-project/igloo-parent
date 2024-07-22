package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.conditionalinput;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

public final class ConditionalInputJavaScriptResourceReference
    extends AbstractCoreJQueryPluginResourceReference {

  private static final long serialVersionUID = -8227122372939304387L;

  private static final ConditionalInputJavaScriptResourceReference INSTANCE =
      new ConditionalInputJavaScriptResourceReference();

  private ConditionalInputJavaScriptResourceReference() {
    super(ConditionalInputJavaScriptResourceReference.class, "jquery.conditionalInput.js");
  }

  public static ConditionalInputJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
