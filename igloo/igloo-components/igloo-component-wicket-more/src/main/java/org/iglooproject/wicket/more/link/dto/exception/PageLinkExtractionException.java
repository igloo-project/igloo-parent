package org.iglooproject.wicket.more.link.dto.exception;

import org.springframework.core.NestedCheckedException;

public class PageLinkExtractionException extends NestedCheckedException {

  private static final long serialVersionUID = 1L;

  public PageLinkExtractionException(String message) {
    super(message);
  }

  public PageLinkExtractionException(String message, Throwable cause) {
    super(message, cause);
  }
}
