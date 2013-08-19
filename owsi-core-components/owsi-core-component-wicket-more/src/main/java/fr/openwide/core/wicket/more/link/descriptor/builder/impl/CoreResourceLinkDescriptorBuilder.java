package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.springframework.util.Assert;

import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseResourceState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IResourceParametersState;
import fr.openwide.core.wicket.more.link.descriptor.impl.CoreResourceLinkDescriptorImpl;
import fr.openwide.core.wicket.more.link.descriptor.impl.PageParametersModel;

public class CoreResourceLinkDescriptorBuilder
		extends AbstractCoreLinkDescriptorBuilderParametersStateImpl<IResourceLinkDescriptor>
		implements IBaseResourceState, IResourceParametersState {
	
	private IModel<? extends ResourceReference> resourceReferenceModel = null;

	@Override
	public IResourceParametersState resource(ResourceReference resourceReference) {
		return resource(new Model<ResourceReference>(resourceReference));
	}

	@Override
	public IResourceParametersState resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		Assert.isNull(resourceReferenceModel, "Cannot set the ResourceReference twice");
		this.resourceReferenceModel = resourceReferenceModel;
		return this;
	}

	@Override
	protected IResourceLinkDescriptor build(PageParametersModel parametersModel) {
		return new CoreResourceLinkDescriptorImpl(resourceReferenceModel, parametersModel);
	}

}
