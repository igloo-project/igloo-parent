package org.iglooproject.sass.exceptions;

public class UnknownScssScope extends RuntimeException {
  private static final long serialVersionUID = 4498355316803208541L;

  public UnknownScssScope() {
    super();
  }

  public UnknownScssScope(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public UnknownScssScope(String message, Throwable cause) {
    super(message, cause);
  }

  public UnknownScssScope(String message) {
    super(message);
  }

  public UnknownScssScope(Throwable cause) {
    super(cause);
  }
}
