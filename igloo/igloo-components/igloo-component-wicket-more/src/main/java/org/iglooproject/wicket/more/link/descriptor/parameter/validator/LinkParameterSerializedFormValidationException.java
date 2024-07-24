package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import java.util.Collection;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LinkParameterSerializedFormValidationException
    extends LinkParameterValidationException {

  private static final long serialVersionUID = -4166816084702579360L;

  public LinkParameterSerializedFormValidationException(
      PageParameters parameters, Collection<ILinkParameterValidationErrorDescription> errors) {
    super(createMessage(parameters, errors));
  }

  private static final String createMessage(
      PageParameters parameters, Collection<ILinkParameterValidationErrorDescription> errors) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("The parameters '")
        .append(parameters)
        .append("' failed validation with the following errors: [");
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
