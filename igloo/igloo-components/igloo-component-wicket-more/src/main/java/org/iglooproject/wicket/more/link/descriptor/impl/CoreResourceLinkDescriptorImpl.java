package org.iglooproject.wicket.more.link.descriptor.impl;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.ImmutableList;

import igloo.wicket.model.Models;

import org.iglooproject.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import org.iglooproject.wicket.more.link.descriptor.IResourceLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.parameter.injector.LinkParameterInjectionRuntimeException;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

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
	
	protected ResourceReference getValidResourceReference() throws LinkInvalidTargetRuntimeException {
		ResourceReference resourceReference = resourceReferenceModel.getObject();
		if (resourceReference == null) {
			throw new LinkInvalidTargetRuntimeException("The target resourceReference of this ILinkDescriptor was null");
		}
		return resourceReference;
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
		ResourceReference resourceReference = getValidResourceReference();
		if (resourceReference.canBeRegistered()) {
			Application.get().getResourceReferenceRegistry().registerResourceReference(resourceReference);
		}
		return requestCycle.urlFor(resourceReference, parameters).toString();
	}
	
	@Override
	public boolean isAccessible() {
		ResourceReference resourceReference = resourceReferenceModel.getObject();
		return resourceReference != null && super.isAccessible();
	}

	@Override
	public ILinkGenerator chain(ILinkGenerator other) {
		return new ChainedLinkGeneratorImpl(ImmutableList.of(this, other));
	}
	
	@Override
	public void detach() {
		super.detach();
		resourceReferenceModel.detach();
	}

}
