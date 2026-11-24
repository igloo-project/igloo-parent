package igloo.vuedatepicker.reference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * Vue runtime build with development warnings. Use only in development: {@link
 * VueJavaScriptResourceReference} is smaller and faster.
 */
public class VueJavaScriptDevResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueJavaScriptDevResourceReference INSTANCE =
      new VueJavaScriptDevResourceReference();

  public static VueJavaScriptDevResourceReference get() {
    return INSTANCE;
  }

  public VueJavaScriptDevResourceReference() {
    super("vue/current/dist/vue.runtime.global.js");
  }
}
