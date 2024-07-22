package igloo.igloojs.showpassword;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class ShowPasswordJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 230717013908362914L;

  private static final ShowPasswordJavaScriptResourceReference INSTANCE =
      new ShowPasswordJavaScriptResourceReference();

  public static ShowPasswordJavaScriptResourceReference get() {
    return INSTANCE;
  }

  public ShowPasswordJavaScriptResourceReference() {
    super("igloojs/current/dist/show-password.js");
  }
}
