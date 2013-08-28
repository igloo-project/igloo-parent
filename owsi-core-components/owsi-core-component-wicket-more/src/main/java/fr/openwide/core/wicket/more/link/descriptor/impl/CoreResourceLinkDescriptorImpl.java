package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationException;

public class CoreResourceLinkDescriptorImpl extends AbstractCoreLinkDescriptor implements IResourceLinkDescriptor {

	private static final long serialVersionUID = -2046898427977725120L;
	
	private final IModel<? extends ResourceReference> resourceReferenceModel;
	
	public CoreResourceLinkDescriptorImpl(
			IModel<? extends ResourceReference> resourceReferenceModel,
			LinkParametersMapping parametersMapping,
			ILinkParameterValidator validator) {
		super(parametersMapping, validator);
		Args.notNull(resourceReferenceModel, "resourceReferenceModel");
		this.resourceReferenceModel = resourceReferenceModel;
	}
	
	protected ResourceReference getResourceReference() {
		return resourceReferenceModel.getObject();
	}
	
	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return new DynamicBookmarkableResourceLink(wicketId, resourceReferenceModel, parametersMapping, parametersValidator);
	}
	
	@Override
	public String fullUrl() throws LinkParameterValidationException {
		return fullUrl(RequestCycle.get());
	}
	
	@Override
	public String fullUrl(RequestCycle requestCycle) throws LinkParameterValidationException {
		PageParameters parameters = getValidatedParameters();
		
		return requestCycle.getUrlRenderer()
				.renderFullUrl(
						Url.parse(requestCycle.urlFor(getResourceReference(), parameters))
				);
	}
	
	@Override
	public void detach() {
		super.detach();
		resourceReferenceModel.detach();
	}

}
