package fr.openwide.core.wicket.more.link.descriptor.validator;

import java.util.Collection;

import org.apache.wicket.request.mapper.parameter.PageParameters;


public class ParameterValidationException extends RuntimeException {

	private static final long serialVersionUID = -4166816084702579360L;

	public ParameterValidationException(PageParameters parameters, Collection<IParameterValidationError> errors) {
		super(createMessage(parameters, errors));
	}
	
	private static final String createMessage(PageParameters parameters, Collection<IParameterValidationError> errors) {
		StringBuilder builder = new StringBuilder();
		builder.append("The parameters '").append(parameters).append("' failed validation with the following errors: [");
		boolean first = true;
		for (IParameterValidationError error : errors) {
			if (!first) {
				builder.append(", ");
			}
			builder.append(error.getMessage());
			first = false;
		}
		builder.append("]");
		
		return builder.toString();
	}

}
