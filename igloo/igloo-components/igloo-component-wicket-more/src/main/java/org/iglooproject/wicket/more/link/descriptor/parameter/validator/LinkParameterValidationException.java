package org.iglooproject.wicket.more.link.descriptor.parameter.validator;

import org.iglooproject.wicket.more.link.descriptor.LinkException;

public class LinkParameterValidationException extends LinkException {

	private static final long serialVersionUID = -3047552351766396232L;

	/**
	 * @deprecated Use {@link #LinkParameterValidationException(String)} or
	 * {@link #LinkParameterValidationException(String, Throwable)} and provide a meaningful message.
	 */
	@Deprecated
	public LinkParameterValidationException() {
		super(null);
	}

	public LinkParameterValidationException(String message) {
		super(message);
	}
	
	/**
	 * @deprecated Use {@link #LinkParameterValidationException(String, Throwable)} and provide a meaningful message.
	 */
	@Deprecated
	public LinkParameterValidationException(Throwable cause) {
		super(null, cause);
	}

	public LinkParameterValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}