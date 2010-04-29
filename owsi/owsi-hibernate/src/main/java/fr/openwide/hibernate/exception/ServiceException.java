package fr.openwide.hibernate.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -6854945379036729034L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

}