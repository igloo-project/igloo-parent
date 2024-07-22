package org.iglooproject.wicket.more.link.descriptor;

/**
 * Thrown when the target of a link (a page, a resource, ...) is invalid.
 *
 * <p>What "invalid" means exactly is unspecified. It may be, for instance, that the target was
 * <code>null</code>.
 */
public class LinkInvalidTargetRuntimeException extends LinkRuntimeException {

  private static final long serialVersionUID = 3688021845631362806L;

  public LinkInvalidTargetRuntimeException(String message) {
    super(message);
  }
}
