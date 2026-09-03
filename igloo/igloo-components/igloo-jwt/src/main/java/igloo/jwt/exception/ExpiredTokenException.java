package igloo.jwt.exception;

public class ExpiredTokenException extends InvalidTokenException {

  private static final long serialVersionUID = -2563625430996145405L;

  public ExpiredTokenException(String message) {
    super(message);
  }

  public ExpiredTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
