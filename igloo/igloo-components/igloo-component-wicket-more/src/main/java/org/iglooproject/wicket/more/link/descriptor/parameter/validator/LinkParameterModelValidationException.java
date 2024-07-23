package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import java.util.Collection;

public class LinkParameterModelValidationException extends LinkParameterValidationException {

  private static final long serialVersionUID = -4166816084702579360L;

  public LinkParameterModelValidationException(
      Collection<ILinkParameterValidationErrorDescription> errors) {
    super(createMessage(errors));
  }

  private static final String createMessage(
      Collection<ILinkParameterValidationErrorDescription> errors) {
    StringBuilder builder = new StringBuilder();
    builder.append("The parameters model failed validation with the following errors: [");
    boolean first = true;
    for (ILinkParameterValidationErrorDescription error : errors) {
      if (!first) {
        builder.append(", ");
      }
      builder.append(error.getMessage());
      first = false;
    }
    builder.append("]");

    return builder.toString();
  }
}
