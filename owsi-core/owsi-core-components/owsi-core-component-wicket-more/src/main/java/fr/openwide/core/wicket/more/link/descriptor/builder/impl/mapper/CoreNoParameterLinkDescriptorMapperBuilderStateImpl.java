package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IOneParameterMapperState;

public class CoreNoParameterLinkDescriptorMapperBuilderStateImpl<TLinkDescriptor extends ILinkDescriptor>
		extends AbstractCoreLinkDescriptorMapperBuilderStateImpl<TLinkDescriptor, TLinkDescriptor>
		implements INoParameterMapperState<TLinkDescriptor> {
	
	public CoreNoParameterLinkDescriptorMapperBuilderStateImpl(
			CoreLinkDescriptorBuilderFactory<TLinkDescriptor> linkDescriptorFactory) {
		super(linkDescriptorFactory);
	}
	
	@Override
	protected IBuilderFactory<TLinkDescriptor> getFactory() {
		return linkDescriptorFactory;
	}

	@Override
	public <T1> IOneParameterMapperState<TLinkDescriptor, T1> model(Class<? super T1> clazz) {
		return new CoreOneParameterLinkDescriptorMapperBuilderStateImpl<TLinkDescriptor, T1>(linkDescriptorFactory, clazz);
	}

}
