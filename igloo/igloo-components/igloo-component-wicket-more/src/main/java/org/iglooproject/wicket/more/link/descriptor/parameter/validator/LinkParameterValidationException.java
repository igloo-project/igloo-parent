package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import org.iglooproject.wicket.more.link.descriptor.LinkException;

public class LinkParameterValidationException extends LinkException {

  private static final long serialVersionUID = -3047552351766396232L;

  public LinkParameterValidationException(String message) {
    super(message);
  }

  public LinkParameterValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
