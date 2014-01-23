package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

public abstract class AbstractCoreExplicitelyParameterizedDynamicBookmarkableLink extends AbstractDynamicBookmarkableLink {
	private static final long serialVersionUID = 6850747758117881107L;
	
	private final IModel<PageParameters> parametersMapping;
	private final ILinkParameterValidator parametersValidator;

	public AbstractCoreExplicitelyParameterizedDynamicBookmarkableLink(String wicketId,
			IModel<PageParameters> parametersMapping, ILinkParameterValidator parametersValidator) {
		super(wicketId);
		Args.notNull(parametersMapping, "parametersMapping");
		Args.notNull(parametersValidator, "parametersValidator");
		this.parametersMapping = wrap(parametersMapping);
		this.parametersValidator = parametersValidator;
	}

	protected abstract CharSequence getRelativeURL(PageParameters parameters) throws LinkInvalidTargetRuntimeException;

	private PageParameters getParameters() {
		return parametersMapping.getObject();
	}
	
	@Override
	protected boolean isValid() {
		if (LinkParameterValidators.isModelValid(parametersValidator)) {
			PageParameters parameters = getParameters();
			if (LinkParameterValidators.isSerializedValid(parameters, parametersValidator)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected CharSequence getRelativeURL() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException {
				PageParameters parameters;
				
				try {
					LinkParameterValidators.checkModel(parametersValidator);
					parameters = getParameters();
					LinkParameterValidators.checkSerialized(parameters, parametersValidator);
				} catch(LinkParameterValidationException e) {
					throw new LinkParameterValidationRuntimeException(e);
				}
				
				return getRelativeURL(parameters);
			}

	@Override
	protected void onDetach() {
		super.onDetach();
		parametersMapping.detach();
		parametersValidator.detach();
	}

}
