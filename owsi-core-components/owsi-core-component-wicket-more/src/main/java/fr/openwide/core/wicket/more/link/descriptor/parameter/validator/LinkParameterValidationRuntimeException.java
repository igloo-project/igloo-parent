package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

public class LinkParameterValidationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -4166816084702579360L;

	public LinkParameterValidationRuntimeException(LinkParameterValidationException cause) {
		super("An error occurred during parameter validation", cause);
	}

}
