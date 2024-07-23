package org.iglooproject.spring.property.exception;

public class PropertyServiceDuplicateRegistrationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PropertyServiceDuplicateRegistrationException(String message) {
    super(message);
  }
}
