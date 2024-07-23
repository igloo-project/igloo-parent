package org.iglooproject.wicket.more.link.descriptor.parameter.extractor;

import org.iglooproject.wicket.more.link.descriptor.LinkException;

public class LinkParameterExtractionException extends LinkException {

  private static final long serialVersionUID = 1755131619089627896L;

  public LinkParameterExtractionException(String message) {
    super(message);
  }

  public LinkParameterExtractionException(String message, Throwable cause) {
    super(message, cause);
  }
}
