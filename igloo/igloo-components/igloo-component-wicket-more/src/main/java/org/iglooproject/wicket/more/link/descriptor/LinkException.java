package org.iglooproject.wicket.more.link.descriptor;

import org.springframework.core.NestedCheckedException;

/** Thrown when something goes awry during a link injection or generation. */
public abstract class LinkException extends NestedCheckedException {

  private static final long serialVersionUID = 3688021845631362806L;

  public LinkException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public LinkException(String msg) {
    super(msg);
  }
}
