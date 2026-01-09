package igloo.vuedatepicker.reference;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;

public class VueDatePickerCssResourceReference extends WebjarsCssResourceReference {

  private static final long serialVersionUID = 1L;

  private static final VueDatePickerCssResourceReference INSTANCE =
      new VueDatePickerCssResourceReference();

  public static VueDatePickerCssResourceReference get() {
    return INSTANCE;
  }

  public VueDatePickerCssResourceReference() {
    super("igloo-vue-datepicker/current/dist/vueInit.css");
  }
}
