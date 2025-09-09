package basicapp.back.business.notification.service.exception;

public class NotificationException extends Exception {
  private static final long serialVersionUID = 1L;

  public NotificationException(String errorExceptionMessage, Exception e) {
    super(errorExceptionMessage, e);
  }
}
