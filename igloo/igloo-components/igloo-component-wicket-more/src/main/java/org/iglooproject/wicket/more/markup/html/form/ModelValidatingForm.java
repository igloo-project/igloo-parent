package org.iglooproject.wicket.more.markup.html.form;

import com.google.common.collect.Lists;
import igloo.wicket.model.Detachables;
import java.util.List;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.more.markup.html.form.validation.IFormModelValidator;

public class ModelValidatingForm<E> extends Form<E> {

  private static final long serialVersionUID = 214102105248521648L;

  private final List<IFormModelValidator> formModelValidators = Lists.newArrayList();

  public ModelValidatingForm(String id) {
    super(id);
  }

  public ModelValidatingForm(String id, IModel<E> model) {
    super(id, model);
  }

  @Override
  protected void onValidateModelObjects() {
    super.onValidateModelObjects();

    for (IFormModelValidator formModelValidator : formModelValidators) {
      validateFormModelValidator(formModelValidator);
    }
  }

  private void validateFormModelValidator(IFormModelValidator validator) {
    Args.notNull(validator, "validator");

    final FormComponent<?>[] dependents = validator.getDependentFormComponents();

    boolean validate = true;

    if (dependents != null) {
      for (final FormComponent<?> dependent : dependents) {
        if (!dependent.isValid() || !dependent.isVisibleInHierarchy()) {
          validate = false;
          break;
        }
      }
    }

    if (validate) {
      validator.validate(this);
    }
  }

  public ModelValidatingForm<E> add(
      IFormModelValidator firstFormModelValidator,
      IFormModelValidator... otherFormModelValidators) {
    formModelValidators.addAll(Lists.asList(firstFormModelValidator, otherFormModelValidators));
    return this;
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    Detachables.detach(formModelValidators);
  }
}
