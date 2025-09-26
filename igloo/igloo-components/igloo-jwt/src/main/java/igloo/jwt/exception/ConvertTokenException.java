package igloo.jwt.exception;

public class ConvertTokenException extends Exception {

  private static final long serialVersionUID = -2563625430996145404L;

  public ConvertTokenException(String message) {
    super(message);
  }

  public ConvertTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
