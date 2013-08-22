package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationErrorCollector;

public class CoreLinkDescriptorBuilderMandatoryParameterValidator implements ILinkParameterValidator {
	
	private static final long serialVersionUID = 7015800524943994171L;
	
	private final String parameterName;

	public CoreLinkDescriptorBuilderMandatoryParameterValidator(String parameterName) {
		Args.notNull(parameterName, "parameterName");
		this.parameterName = parameterName;
	}

	@Override
	public void validate(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
		if (parameters.get(parameterName).isNull()) {
			collector.addError(String.format("Mandatory parameter '%s' was null.", parameterName));
		}
	}

}
