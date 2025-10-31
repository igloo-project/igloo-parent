package igloo.vuedatepicker.reference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * Use only on development env. If not, please use {@link VueJavaScriptResourceReference} for
 * smaller files and better performance
 */
public class VueJavaScriptDevResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueJavaScriptDevResourceReference INSTANCE =
      new VueJavaScriptDevResourceReference();

  public static VueJavaScriptDevResourceReference get() {
    return INSTANCE;
  }

  public VueJavaScriptDevResourceReference() {
    super("vue/current/dist/vue.global.js");
  }
}
