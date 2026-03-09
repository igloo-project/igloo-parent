package org.iglooproject.wicket.more.util.validate.validators;

import jakarta.annotation.Nonnull;
import java.util.Collection;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public abstract class AbstractPartialUnicityValidator<T, V> extends Behavior
    implements IValidator<V> {

  private static final long serialVersionUID = -7514727402418569911L;

  public static final String MAIN_OBJECT_VARIABLE_NAME = "main";

  public static final String CONFLICTING_OBJECTS_VARIABLE_NAME = "conflicting";

  private final IModel<? extends T> mainObjectModel;

  private final String errorKey;

  public AbstractPartialUnicityValidator(IModel<? extends T> mainObjectModel, String errorKey) {
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
    Collection<T> matchingObjects = listByUniqueField(validatable.getValue());
    if (matchingObjects != null
        && !matchingObjects.isEmpty()
        && !contains(matchingObjects, mainObject)) {
      validatable.error(
          decorate(
              new ValidationError(this)
                  .addKey(errorKey)
                  .setVariable(MAIN_OBJECT_VARIABLE_NAME, mainObject)
                  .setVariable(CONFLICTING_OBJECTS_VARIABLE_NAME, matchingObjects)));
    }
  }

  protected boolean contains(@Nonnull Collection<T> matchingObjects, T mainObject) {
    return matchingObjects.contains(mainObject);
  }

  protected ValidationError decorate(ValidationError error) {
    // Does nothing by default
    return error;
  }

  protected abstract Collection<T> listByUniqueField(V value);
}
