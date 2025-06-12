package org.igloo.storage.integration.wicket.exception;

public abstract class AbstractWicketStorageException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  protected AbstractWicketStorageException(String message, Throwable cause) {
    super(message, cause);
  }

  protected AbstractWicketStorageException(String message) {
    super(message);
  }
}
