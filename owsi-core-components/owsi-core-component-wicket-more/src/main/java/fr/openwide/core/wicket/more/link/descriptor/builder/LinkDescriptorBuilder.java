package fr.openwide.core.wicket.more.link.descriptor.builder;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

import fr.openwide.core.wicket.more.link.descriptor.IImageResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.IResourceLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderPageInstanceStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper.CoreNoParameterLinkDescriptorMapperBuilderStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IPageInstanceState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.link.model.PageModel;

public class LinkDescriptorBuilder implements IBaseState {
	
	public LinkDescriptorBuilder() { }

	@Override
	public <P extends Page> INoParameterMapperState<IPageLinkDescriptor> page(Class<P> pageClass) {
		return new CoreNoParameterLinkDescriptorMapperBuilderStateImpl<IPageLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.page(pageClass)
		);
	}

	@Override
	public INoParameterMapperState<IPageLinkDescriptor> page(IModel<? extends Class<? extends Page>> pageClassModel) {
		return new CoreNoParameterLinkDescriptorMapperBuilderStateImpl<IPageLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.page(pageClassModel)
		);
	}
	
	@Override
	public IPageInstanceState<IPageLinkGenerator> pageInstance(Page page) {
		return pageInstance(PageModel.of(page));
	}
	
	@Override
	public IPageInstanceState<IPageLinkGenerator> pageInstance(IModel<? extends Page> pageInstanceModel) {
		return new CoreLinkDescriptorBuilderPageInstanceStateImpl(pageInstanceModel);
	}

	@Override
	public INoParameterMapperState<IResourceLinkDescriptor> resource(ResourceReference resourceReference) {
		return new CoreNoParameterLinkDescriptorMapperBuilderStateImpl<IResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.resource(resourceReference)
		);
	}

	@Override
	public INoParameterMapperState<IResourceLinkDescriptor> resource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new CoreNoParameterLinkDescriptorMapperBuilderStateImpl<IResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.resource(resourceReferenceModel)
		);
	}

	@Override
	public INoParameterMapperState<IImageResourceLinkDescriptor> imageResource(ResourceReference resourceReference) {
		return new CoreNoParameterLinkDescriptorMapperBuilderStateImpl<IImageResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.imageResource(resourceReference)
		);
	}

	@Override
	public INoParameterMapperState<IImageResourceLinkDescriptor> imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
		return new CoreNoParameterLinkDescriptorMapperBuilderStateImpl<IImageResourceLinkDescriptor>(
				CoreLinkDescriptorBuilderFactory.imageResource(resourceReferenceModel)
		);
	}

}
