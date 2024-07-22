package org.iglooproject.rest.jersey2.util.exception;

public interface IRemoteApiError {

  int getCode();

  String getMessage();
}
