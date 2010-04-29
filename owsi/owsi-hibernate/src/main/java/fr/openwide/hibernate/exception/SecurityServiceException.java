package fr.openwide.hibernate.exception;

public class SecurityServiceException extends Exception {

	private static final long serialVersionUID = -3589212911857116468L;

	public SecurityServiceException() {
		super();
	}

	public SecurityServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityServiceException(String message) {
		super(message);
	}

	public SecurityServiceException(Throwable cause) {
		super(cause);
	}

}