package igloo.bootstrap.popper;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public final class Popper1JavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1762476460042247594L;

  private static final Popper1JavaScriptResourceReference INSTANCE =
      new Popper1JavaScriptResourceReference();

  private Popper1JavaScriptResourceReference() {
    super("popper.js/current/dist/umd/popper.js");
  }

  public static Popper1JavaScriptResourceReference get() {
    return INSTANCE;
  }
}
