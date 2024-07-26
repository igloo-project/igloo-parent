package org.iglooproject.wicket.more.markup.html.form;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ComponentModel;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Behavior;

public class LabelPlaceholderBehavior extends Behavior {

  private static final long serialVersionUID = 7392345869664046823L;

  private static final String PLACEHOLDER_ATTRIBUTE = "placeholder";
  private static final String DATA_PLACEHOLDER_ATTRIBUTE = "data-placeholder";

  public LabelPlaceholderBehavior() {
    super();
  }

  @Override
  public void bind(Component component) {
    super.bind(component);

    boolean isSelect2Component = !component.getBehaviors(Select2Behavior.class).isEmpty();
    component.add(
        new AttributeModifier(
            isSelect2Component ? DATA_PLACEHOLDER_ATTRIBUTE : PLACEHOLDER_ATTRIBUTE,
            new LabelPlaceholderModel()));
  }

  private static class LabelPlaceholderModel extends ComponentModel<String> {
    private static final long serialVersionUID = 8627941143273996086L;

    @Override
    protected String getObject(Component component) {
      if (!(component instanceof FormComponent)) {
        throw new IllegalStateException(
            "Behavior "
                + getClass().getName()
                + " can only be added to an instance of a FormComponent");
      }
      FormComponent<?> formComponent = (FormComponent<?>) component;
      IModel<String> labelModel = formComponent.getLabel();
      return labelModel == null ? null : labelModel.getObject();
    }
  }
}
