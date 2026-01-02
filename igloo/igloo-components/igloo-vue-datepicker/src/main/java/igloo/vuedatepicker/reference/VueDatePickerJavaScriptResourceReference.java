package igloo.vuedatepicker.reference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

public class VueDatePickerJavaScriptResourceReference extends WebjarsJavaScriptResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueDatePickerJavaScriptResourceReference INSTANCE =
      new VueDatePickerJavaScriptResourceReference();

  public static VueDatePickerJavaScriptResourceReference get() {
    return INSTANCE;
  }

  public VueDatePickerJavaScriptResourceReference() {
    super("vuepic__vue-datepicker/current/dist/vue-datepicker.iife.js");
  }
}
