package igloo.jwt.exception;

public class InvalidTokenException extends Exception {

  private static final long serialVersionUID = -2563625430996145404L;

  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
