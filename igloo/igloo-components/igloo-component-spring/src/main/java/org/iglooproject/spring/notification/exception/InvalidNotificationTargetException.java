package org.iglooproject.spring.notification.exception;

import org.springframework.core.NestedRuntimeException;

public class InvalidNotificationTargetException extends NestedRuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidNotificationTargetException(String msg) {
    super(msg);
  }

  public InvalidNotificationTargetException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
