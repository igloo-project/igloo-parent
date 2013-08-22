package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

public abstract class AbstractCoreLinkDescriptor implements ILinkDescriptor {

	private static final long serialVersionUID = 2474433766347554582L;
	
	protected final LinkParametersMapping parametersMapping;
	protected final ILinkParameterValidator parametersValidator;

	public AbstractCoreLinkDescriptor(
			LinkParametersMapping parametersMapping,
			ILinkParameterValidator parametersValidator) {
		super();
		Args.notNull(parametersMapping, "parametersModel");
		Args.notNull(parametersValidator, "parametersValidator");
		this.parametersMapping = parametersMapping;
		this.parametersValidator = parametersValidator;
	}

	protected final PageParameters getValidatedParameters() throws LinkParameterValidationException {
		PageParameters parameters = parametersMapping.getObject();
		LinkParameterValidators.check(parameters, parametersValidator);
		return parameters;
	}
	
	@Override
	public void extract(PageParameters parameters) throws LinkParameterValidationException, LinkParameterExtractionRuntimeException {
		LinkParameterValidators.check(parameters, parametersValidator);
		parametersMapping.setObject(parameters);
	}

	@Override
	public void detach() {
		parametersMapping.detach();
	}

}