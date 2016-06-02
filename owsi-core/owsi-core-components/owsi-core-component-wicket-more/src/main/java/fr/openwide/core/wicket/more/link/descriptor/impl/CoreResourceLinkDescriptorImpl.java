package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;
import fr.openwide.core.wicket.more.util.model.Models;

public class CoreResourceLinkDescriptorImpl extends AbstractCoreExplicitelyParameterizedLinkDescriptor implements IResourceLinkDescriptor {

	private static final long serialVersionUID = -2046898427977725120L;
	
	protected final IModel<? extends ResourceReference> resourceReferenceModel;
	
	public CoreResourceLinkDescriptorImpl(
			IModel<? extends ResourceReference> resourceReferenceModel,
			LinkParametersMapping parametersMapping,
			ILinkParameterValidator validator) {
		super(parametersMapping, validator);
		Args.notNull(resourceReferenceModel, "resourceReferenceModel");
		this.resourceReferenceModel = resourceReferenceModel;
	}
	
	@Override
	public IResourceLinkDescriptor wrap(Component component) {
		return new CoreResourceLinkDescriptorImpl(
				Models.wrap(resourceReferenceModel, component),
				parametersMapping.wrapOnAssignment(component),
				parametersValidator
		);
	}
	
	protected ResourceReference getResourceReference() {
		return resourceReferenceModel.getObject();
	}
	
	@Override
	public AbstractDynamicBookmarkableLink link(String wicketId) {
		return new DynamicBookmarkableResourceLink(wicketId, this);
	}
	
	@Override
	public String url() throws LinkInvalidTargetRuntimeException, LinkParameterInjectionRuntimeException,
			LinkParameterValidationRuntimeException {
		return url(RequestCycle.get());
	}
	
	@Override
	public String url(RequestCycle requestCycle) throws LinkInvalidTargetRuntimeException,
			LinkParameterInjectionRuntimeException, LinkParameterValidationRuntimeException {
		PageParameters parameters = getValidatedParameters();
		ResourceReference resourceReference = getResourceReference();
		if (resourceReference.canBeRegistered()) {
			Application.get().getResourceReferenceRegistry().registerResourceReference(resourceReference);
		}
		return requestCycle.urlFor(resourceReference, parameters).toString();
	}
	
	@Override
	public boolean isAccessible() {
		return getResourceReference() != null && super.isAccessible();
	}
	
	@Override
	public void detach() {
		super.detach();
		resourceReferenceModel.detach();
	}

}
