package org.iglooproject.wicket.more.css.scss;

import java.util.Locale;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.css.ICssResourceReference;

public class ScssResourceReference extends ResourceReference implements ICssResourceReference {

  private static final long serialVersionUID = -1009491608247244399L;

  public ScssResourceReference(Class<?> scope, String name) {
    this(scope, name, null, null, null);
  }

  public ScssResourceReference(
      Class<?> scope, String name, Locale locale, String style, String variation) {
    super(scope, name, locale, style, variation);
  }

  @Override
  public ScssResource getResource() {
    return new ScssResource(getScope(), getName(), getLocale(), getStyle(), getVariation());
  }
}
