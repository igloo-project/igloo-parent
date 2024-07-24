package org.iglooproject.rest.jersey2.util.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"cause", "stackTrace", "localizedMessage"})
public class RemoteApiException extends RuntimeException {

  private static final long serialVersionUID = 2172390724041827232L;

  private int code;

  private String message;

  public RemoteApiException() {}

  public RemoteApiException(IRemoteApiError remoteApiError) {
    this(remoteApiError, null);
  }

  public RemoteApiException(IRemoteApiError remoteApiError, Throwable cause) {
    super(cause);

    this.code = remoteApiError.getCode();
    this.message = remoteApiError.getMessage();
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
