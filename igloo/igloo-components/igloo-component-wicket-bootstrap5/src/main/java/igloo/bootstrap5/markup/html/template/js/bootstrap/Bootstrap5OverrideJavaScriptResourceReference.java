package igloo.bootstrap5.markup.html.template.js.bootstrap;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class Bootstrap5OverrideJavaScriptResourceReference
    extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final Bootstrap5OverrideJavaScriptResourceReference INSTANCE =
      new Bootstrap5OverrideJavaScriptResourceReference();

  private Bootstrap5OverrideJavaScriptResourceReference() {
    super("bootstrap5-override/current/js/dist/bootstrap.js");
  }

  public static Bootstrap5OverrideJavaScriptResourceReference get() {
    return INSTANCE;
  }
}
