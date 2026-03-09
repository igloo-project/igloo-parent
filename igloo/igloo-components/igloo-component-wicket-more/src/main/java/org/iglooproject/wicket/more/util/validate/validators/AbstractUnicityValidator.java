package org.iglooproject.wicket.more.util.validate.validators;

import jakarta.annotation.Nonnull;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public abstract class AbstractUnicityValidator<T, V> extends Behavior implements IValidator<V> {

  private static final long serialVersionUID = -7514727402418569911L;

  public static final String MAIN_OBJECT_VARIABLE_NAME = "main";

  public static final String CONFLICTING_OBJECT_VARIABLE_NAME = "conflicting";

  private final IModel<? extends T> mainObjectModel;

  private final String errorKey;

  public AbstractUnicityValidator(IModel<? extends T> mainObjectModel, String errorKey) {
    super();
    this.mainObjectModel = mainObjectModel;
    this.errorKey = errorKey;
  }

  @Override
  public void detach(Component component) {
    super.detach(component);
    mainObjectModel.detach();
  }

  @Override
  public void validate(IValidatable<V> validatable) {
    T mainObject = mainObjectModel.getObject();
    T matchingObject = getByUniqueField(validatable.getValue());
    if (matchingObject != null && !isSame(matchingObject, mainObject)) {
      validatable.error(
          decorate(
              new ValidationError(this)
                  .addKey(errorKey)
                  .setVariable(MAIN_OBJECT_VARIABLE_NAME, mainObject)
                  .setVariable(CONFLICTING_OBJECT_VARIABLE_NAME, matchingObject)));
    }
  }

  protected boolean isSame(@Nonnull T matchingObject, T mainObject) {
    return matchingObject.equals(mainObject);
  }

  protected ValidationError decorate(ValidationError error) {
    // Does nothing by default
    return error;
  }

  protected abstract T getByUniqueField(V value);
}
