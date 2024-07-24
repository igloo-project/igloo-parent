package org.iglooproject.spring.property.exception;

public class PropertyServiceIncompleteRegistrationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PropertyServiceIncompleteRegistrationException(String message) {
    super(message);
  }
}
