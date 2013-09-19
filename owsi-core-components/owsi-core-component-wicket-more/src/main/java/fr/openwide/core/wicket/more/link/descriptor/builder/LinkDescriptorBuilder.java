package fr.openwide.core.wicket.more.link.descriptor.builder;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderParametersStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;

public class LinkDescriptorBuilder implements IBaseState {
	
	public LinkDescriptorBuilder() { }

	@Override
	public <P extends Page> IParameterMappingState<IPageLinkDescriptor> page(Class<P> pageClass) {
		return new CoreLinkDescriptorBuilderParametersStateImpl<IPageLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.page(pageClass)
		);
	}

	@Override
	public IParameterMappingState<IPageLinkDescriptor> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return new CoreLinkDescriptorBuilderParametersStateImpl<IPageLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.page(pageClassModel)
		);
	}

	@Override
	public IParameterMappingState<IResourceLinkDescriptor> resource(ResourceReference resourceReference) {
		return new CoreLinkDescriptorBuilderParametersStateImpl<IResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.resource(resourceReference)
		);
	}

	@Override
	public IParameterMappingState<IResourceLinkDescriptor> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new CoreLinkDescriptorBuilderParametersStateImpl<IResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.resource(resourceReferenceModel)
		);
	}

	@Override
	public IParameterMappingState<IImageResourceLinkDescriptor> imageResource(ResourceReference resourceReference) {
		return new CoreLinkDescriptorBuilderParametersStateImpl<IImageResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.imageResource(resourceReference)
		);
	}

	@Override
	public IParameterMappingState<IImageResourceLinkDescriptor> imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new CoreLinkDescriptorBuilderParametersStateImpl<IImageResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.imageResource(resourceReferenceModel)
		);
	}

}
