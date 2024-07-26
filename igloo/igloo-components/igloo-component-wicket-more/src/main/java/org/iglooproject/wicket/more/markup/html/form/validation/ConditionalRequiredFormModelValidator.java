package org.iglooproject.wicket.more.markup.html.form.validation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import igloo.wicket.condition.Condition;
import java.util.Collection;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.ValidationError;

public class ConditionalRequiredFormModelValidator implements IFormModelValidator {

  private static final long serialVersionUID = 8629253911243139638L;

  private final Condition condition;

  private final Collection<FormComponent<?>> formComponents;

  private boolean emptyMode;

  private boolean oneRequired;

  private String errorRessourceKey;

  private FormComponent<?> formComponentOnError;

  public ConditionalRequiredFormModelValidator(
      Condition condition,
      FormComponent<?> formComponent,
      FormComponent<?>... otherFormComponents) {
    this(
        condition,
        ImmutableList.<FormComponent<?>>builder()
            .add(formComponent)
            .add(otherFormComponents)
            .build());
  }

  public ConditionalRequiredFormModelValidator(
      Condition condition, Collection<FormComponent<?>> formComponents) {
    super();
    this.condition = condition;
    this.formComponents = ImmutableList.copyOf(formComponents);
  }

  @Override
  public void detach() {
    condition.detach();
  }

  @Override
  public void validate(Form<?> form) {
    if (condition.applies()) {
      for (FormComponent<?> formComponent : formComponents) {
        boolean onError = doCheck(formComponent);
        if (oneRequired && onError) {
          return;
        }
      }
    }
  }

  private boolean doCheck(FormComponent<?> formComponent) {
    boolean onError = false;
    formComponent.setRequired(true);

    if ((emptyMode && formComponent.checkRequired())
        || (!emptyMode && !formComponent.checkRequired())) {
      ValidationError validationError = new ValidationError();
      if (errorRessourceKey != null) {
        validationError.addKey(errorRessourceKey);
      }
      (formComponentOnError != null ? formComponentOnError : formComponent)
          .error(validationError.addKey(emptyMode ? "EmptyRequired" : "Required"));
      onError = true;
    }

    formComponent.setRequired(false);
    return onError;
  }

  public ConditionalRequiredFormModelValidator emptyMode() {
    this.emptyMode = true;
    return this;
  }

  public ConditionalRequiredFormModelValidator oneRequired() {
    this.oneRequired = true;
    return this;
  }

  public ConditionalRequiredFormModelValidator errorRessourceKey(String errorRessourceKey) {
    this.errorRessourceKey = errorRessourceKey;
    return this;
  }

  public ConditionalRequiredFormModelValidator formComponentOnError(
      FormComponent<?> formComponentOnError) {
    this.formComponentOnError = formComponentOnError;
    return this;
  }

  @Override
  public FormComponent<?>[] getDependentFormComponents() {
    return Iterables.toArray(formComponents, FormComponent.class);
  }
}
