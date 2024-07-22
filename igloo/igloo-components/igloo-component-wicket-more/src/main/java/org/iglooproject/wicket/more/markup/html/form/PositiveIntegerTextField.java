package org.iglooproject.wicket.more.markup.html.form;

import igloo.wicket.markup.html.form.FormComponentHelper;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.RangeValidator;

public class PositiveIntegerTextField extends TextField<Integer> {
  private static final long serialVersionUID = -3071860178961793589L;

  private static IValidator<Integer> minimumValidator = RangeValidator.minimum(0);

  public PositiveIntegerTextField(String id, IModel<Integer> model, String fieldName) {
    super(id, model, Integer.class);
    add(minimumValidator);
    FormComponentHelper.setLabel(this, fieldName);
  }
}
