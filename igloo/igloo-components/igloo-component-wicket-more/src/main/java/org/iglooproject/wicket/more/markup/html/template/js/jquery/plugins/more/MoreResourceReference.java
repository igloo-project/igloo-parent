package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.more;

import igloo.jquery.util.AbstractCoreJQueryPluginResourceReference;

public final class MoreResourceReference extends AbstractCoreJQueryPluginResourceReference {

  private static final long serialVersionUID = -7241670799446070904L;

  private static final MoreResourceReference INSTANCE = new MoreResourceReference();

  public MoreResourceReference() {
    super(MoreResourceReference.class, "jquery.more.js");
  }

  public static MoreResourceReference get() {
    return INSTANCE;
  }
}
