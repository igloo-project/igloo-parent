package org.iglooproject.spring.notification.exception;

public class NotificationContentRenderingException extends Exception {

  private static final long serialVersionUID = 2888848743912003211L;

  public NotificationContentRenderingException() {
    super();
  }

  public NotificationContentRenderingException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotificationContentRenderingException(String message) {
    super(message);
  }

  public NotificationContentRenderingException(Throwable cause) {
    super(cause);
  }
}
