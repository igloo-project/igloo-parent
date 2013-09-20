package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

public class LinkParameterValidationException extends Exception {

	private static final long serialVersionUID = -3047552351766396232L;

	public LinkParameterValidationException() {
		super();
	}

	public LinkParameterValidationException(String message) {
		super(message);
	}

	public LinkParameterValidationException(Throwable cause) {
		super(cause);
	}

	public LinkParameterValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}