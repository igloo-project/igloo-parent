package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationErrorCollector;

public class CoreLinkDescriptorBuilderMandatoryParameterValidator implements ILinkParameterValidator {
	
	private static final long serialVersionUID = 7015800524943994171L;
	
	private final List<String> parameterNames = Lists.newArrayListWithExpectedSize(1);
	
	public CoreLinkDescriptorBuilderMandatoryParameterValidator(String... parameterNames) {
		this(Lists.newArrayList(parameterNames));
	}

	public CoreLinkDescriptorBuilderMandatoryParameterValidator(List<String> parameterNames) {
		Args.notNull(parameterNames, "parameterNames");
		Args.notEmpty(parameterNames, "parameterNames");
		this.parameterNames.addAll(parameterNames);
	}

	@Override
	public void validate(PageParameters parameters, LinkParameterValidationErrorCollector collector) {
		for (String parameterName : parameterNames) {
			if (parameters.get(parameterName).isNull()) {
				collector.addError(String.format("Mandatory parameter '%s' was null.", parameterName));
			}
		}
	}

}
