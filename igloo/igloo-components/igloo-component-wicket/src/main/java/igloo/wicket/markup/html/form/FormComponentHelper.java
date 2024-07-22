package igloo.wicket.markup.html.form;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

public final class FormComponentHelper {

  public static <T extends FormComponent<?>> T setLabel(T component, String labelId) {
    return setLabel(component, new ResourceModel(labelId));
  }

  public static <T extends FormComponent<?>> T setLabel(T component, IModel<String> labelModel) {
    component.setLabel(labelModel);

    return component;
  }

  private FormComponentHelper() {}
}
