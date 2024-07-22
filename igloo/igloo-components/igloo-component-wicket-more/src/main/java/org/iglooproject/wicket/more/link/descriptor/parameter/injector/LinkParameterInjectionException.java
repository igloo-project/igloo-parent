package org.iglooproject.wicket.more.link.descriptor.parameter.injector;

import org.iglooproject.wicket.more.link.descriptor.LinkException;

public class LinkParameterInjectionException extends LinkException {

  private static final long serialVersionUID = 1755131619089627896L;

  public LinkParameterInjectionException(String message) {
    super(message);
  }

  public LinkParameterInjectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
