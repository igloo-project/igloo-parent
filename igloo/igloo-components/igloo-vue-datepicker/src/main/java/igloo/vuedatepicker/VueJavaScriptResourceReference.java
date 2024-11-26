package igloo.vuedatepicker;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class VueJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueJavaScriptResourceReference INSTANCE =
      new VueJavaScriptResourceReference();

  public static VueJavaScriptResourceReference get() {
    return INSTANCE;
  }

  // TODO RFO .prod ?
  public VueJavaScriptResourceReference() {
    super("vue/current/dist/vue.global.prod.js");
  }
}
