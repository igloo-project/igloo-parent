package igloo.vuedatepicker.reference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * Vue runtime build: without the template compiler, unused as components are rendered by render
 * functions. Use {@link VueJavaScriptDevResourceReference} in development for Vue warnings.
 */
public class VueJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueJavaScriptResourceReference INSTANCE =
      new VueJavaScriptResourceReference();

  public static VueJavaScriptResourceReference get() {
    return INSTANCE;
  }

  public VueJavaScriptResourceReference() {
    super("vue/current/dist/vue.runtime.global.prod.js");
  }
}
