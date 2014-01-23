package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.extractor.LinkParameterExtractionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterModelValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterSerializedFormValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

public abstract class AbstractCoreExplicitelyParameterizedLinkDescriptor implements ILinkDescriptor {

	private static final long serialVersionUID = 2474433766347554582L;
	
	protected final LinkParametersMapping parametersMapping;
	protected final ILinkParameterValidator parametersValidator;

	public AbstractCoreExplicitelyParameterizedLinkDescriptor(
			LinkParametersMapping parametersMapping,
			ILinkParameterValidator parametersValidator) {
		super();
		Args.notNull(parametersMapping, "parametersModel");
		Args.notNull(parametersValidator, "parametersValidator");
		this.parametersMapping = parametersMapping;
		this.parametersValidator = parametersValidator;
	}

	protected final PageParameters getValidatedParameters() {
		try {
			LinkParameterValidators.checkModel(parametersValidator);
			PageParameters parameters = parametersMapping.getObject();
			LinkParameterValidators.checkSerialized(parameters, parametersValidator);
			return parameters;
		} catch (LinkParameterValidationException e) {
			throw new LinkParameterValidationRuntimeException(e);
		}
	}
	
	@Override
	public void extract(PageParameters parameters)
			throws LinkParameterSerializedFormValidationException, LinkParameterExtractionRuntimeException, LinkParameterModelValidationException {
		LinkParameterValidators.checkSerialized(parameters, parametersValidator);
		parametersMapping.setObject(parameters);
		LinkParameterValidators.checkModel(parametersValidator);
	}

	@Override
	public void detach() {
		parametersMapping.detach();
		parametersValidator.detach();
	}

}