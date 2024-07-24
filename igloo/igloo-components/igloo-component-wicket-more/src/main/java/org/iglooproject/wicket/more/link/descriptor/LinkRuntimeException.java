package org.iglooproject.wicket.more.link.descriptor;

import org.springframework.core.NestedRuntimeException;

/** Thrown when something goes awry during a link injection or generation. */
public abstract class LinkRuntimeException extends NestedRuntimeException {

  private static final long serialVersionUID = 3688021845631362806L;

  public LinkRuntimeException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public LinkRuntimeException(String msg) {
    super(msg);
  }
}
