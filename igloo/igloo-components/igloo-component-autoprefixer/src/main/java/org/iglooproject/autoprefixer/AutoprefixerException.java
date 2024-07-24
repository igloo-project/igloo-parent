package org.iglooproject.autoprefixer;

public class AutoprefixerException extends Exception {

  private static final long serialVersionUID = 1649594871459248111L;

  public AutoprefixerException() {
    super();
  }

  public AutoprefixerException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public AutoprefixerException(String message, Throwable cause) {
    super(message, cause);
  }

  public AutoprefixerException(String message) {
    super(message);
  }

  public AutoprefixerException(Throwable cause) {
    super(cause);
  }
}
