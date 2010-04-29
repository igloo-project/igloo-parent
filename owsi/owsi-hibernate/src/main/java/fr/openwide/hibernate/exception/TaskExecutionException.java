package fr.openwide.hibernate.exception;

public class TaskExecutionException extends Exception {

	private static final long serialVersionUID = 7296640778569784773L;

	public TaskExecutionException() {
		super();
	}

	public TaskExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskExecutionException(String message) {
		super(message);
	}

	public TaskExecutionException(Throwable cause) {
		super(cause);
	}

}