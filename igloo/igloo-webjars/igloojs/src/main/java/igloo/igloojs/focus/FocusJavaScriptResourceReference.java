package igloo.igloojs.focus;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class FocusJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1882027963958939537L;

  private static final FocusJavaScriptResourceReference INSTANCE =
      new FocusJavaScriptResourceReference();

  public static FocusJavaScriptResourceReference get() {
    return INSTANCE;
  }

  public FocusJavaScriptResourceReference() {
    super("igloojs/current/dist/focus.js");
  }
}
