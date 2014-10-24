package fr.openwide.core.showcase.core.business.task.model;

public class MyBusinessException extends Exception {

	private static final long serialVersionUID = 4102871393698562457L;
	
	public MyBusinessException(String message) {
		super(message);
	}
	
	public MyBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
