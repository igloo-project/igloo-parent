package fr.openwide.core.wicket.more.link.descriptor.parameter.validator;

import java.util.Collection;

import org.apache.wicket.request.mapper.parameter.PageParameters;


public class LinkParameterValidationException extends Exception {

	private static final long serialVersionUID = -4166816084702579360L;

	public LinkParameterValidationException(PageParameters parameters, Collection<ILinkParameterValidationError> errors) {
		super(createMessage(parameters, errors));
	}
	
	private static final String createMessage(PageParameters parameters, Collection<ILinkParameterValidationError> errors) {
		StringBuilder builder = new StringBuilder();
		builder.append("The parameters '").append(parameters).append("' failed validation with the following errors: [");
		boolean first = true;
		for (ILinkParameterValidationError error : errors) {
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
