package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidationException;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidators;

public class CoreResourceLinkDescriptorImpl implements IResourceLinkDescriptor {

	private static final long serialVersionUID = -2046898427977725120L;
	
	private final IModel<? extends ResourceReference> resourceReferenceModel;
	private final PageParametersModel parametersModel;
	private final IParameterValidator validator;

	public CoreResourceLinkDescriptorImpl(
			IModel<? extends ResourceReference> resourceReferenceModel,
			PageParametersModel parametersModel,
			IParameterValidator validator) {
		super();
		Args.notNull(resourceReferenceModel, "resourceReferenceModel");
		Args.notNull(parametersModel, "parametersModel");
		Args.notNull(validator, "validator");
		this.resourceReferenceModel = resourceReferenceModel;
		this.parametersModel = parametersModel;
		this.validator = validator;
	}

	protected ResourceReference getResourceReference() {
		return resourceReferenceModel.getObject();
	}

	protected PageParameters getValidatedParameters() throws ParameterValidationException {
		PageParameters parameters = parametersModel.getObject();
		ParameterValidators.check(parameters, validator);
		return parameters;
	}

	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return new DynamicBookmarkableResourceLink(wicketId, resourceReferenceModel, parametersModel, validator);
	}

	@Override
	public String fullUrl() throws ParameterValidationException {
		PageParameters parameters = getValidatedParameters();
		
		RequestCycle requestCycle = RequestCycle.get();
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(
								requestCycle.urlFor(getResourceReference(), parameters)
						)
				);
	}

	@Override
	public void detach() {
		resourceReferenceModel.detach();
		parametersModel.detach();
	}

}
