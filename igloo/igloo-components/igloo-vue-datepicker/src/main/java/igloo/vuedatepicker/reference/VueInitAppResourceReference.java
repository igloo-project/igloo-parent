package igloo.vuedatepicker.reference;


import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class VueInitAppResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueInitAppResourceReference INSTANCE = new VueInitAppResourceReference();

  public static VueInitAppResourceReference get() {
    return INSTANCE;
  }

  private VueInitAppResourceReference() {
    super("igloo-vue-datepicker/current/dist/vueInit.js");
  }
}
