package org.iglooproject.wicket.more.link.descriptor.parameter.injector;

import org.iglooproject.wicket.more.link.descriptor.LinkRuntimeException;

public class LinkParameterInjectionRuntimeException extends LinkRuntimeException {

  private static final long serialVersionUID = 7659297942305885540L;

  public LinkParameterInjectionRuntimeException(LinkParameterInjectionException cause) {
    super("An error occurred during parameter injection", cause);
  }
}
