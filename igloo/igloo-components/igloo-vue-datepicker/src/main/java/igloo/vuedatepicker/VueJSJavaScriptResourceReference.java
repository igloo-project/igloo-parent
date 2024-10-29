package igloo.vuedatepicker;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class VueJSJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueJSJavaScriptResourceReference INSTANCE =
      new VueJSJavaScriptResourceReference();

  public static VueJSJavaScriptResourceReference get() {
    return INSTANCE;
  }

  public VueJSJavaScriptResourceReference() {
    super("igloo/vuedatepicker/vue-script.js");
  }
}
