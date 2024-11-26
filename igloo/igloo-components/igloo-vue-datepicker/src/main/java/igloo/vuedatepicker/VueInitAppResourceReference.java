package igloo.vuedatepicker;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class VueInitAppResourceReference extends JavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueInitAppResourceReference INSTANCE = new VueInitAppResourceReference();

  public static VueInitAppResourceReference get() {
    return INSTANCE;
  }

  public VueInitAppResourceReference() {
    super(VueInitAppResourceReference.class, "vueInit.js");
  }
}
