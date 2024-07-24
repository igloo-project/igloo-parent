package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;

public class LinkParameterValidationErrorCollector {

  private final Collection<ILinkParameterValidationErrorDescription> errors = Lists.newArrayList();

  public void addError(String message) {
    errors.add(new SimpleParameterValidationErrorDescriptionImpl(message));
  }

  public void addError(ILinkParameterValidationErrorDescription error) {
    errors.add(error);
  }

  public Collection<ILinkParameterValidationErrorDescription> getErrors() {
    return Collections.unmodifiableCollection(errors);
  }

  private static class SimpleParameterValidationErrorDescriptionImpl
      implements ILinkParameterValidationErrorDescription {

    private static final long serialVersionUID = 1L;

    private final String message;

    public SimpleParameterValidationErrorDescriptionImpl(String message) {
      super();
      this.message = message;
    }

    @Override
    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
      return getMessage();
    }

    @Override
    public void detach() {
      // Nothing to do
    }
  }
}
