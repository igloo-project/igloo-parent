package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.CoreLinkDescriptorBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.impl.IBuilderFactory;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.INoParameterMapperState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.IOneParameterMapperState;

public class CoreNoParameterLinkDescriptorMapperBuilderStateImpl<L extends ILinkDescriptor>
		extends AbstractCoreLinkDescriptorMapperBuilderStateImpl<L, L>
		implements INoParameterMapperState<L> {
	
	public CoreNoParameterLinkDescriptorMapperBuilderStateImpl(CoreLinkDescriptorBuilderFactory<L> linkDescriptorFactory) {
		super(linkDescriptorFactory);
	}
	
	@Override
	protected IBuilderFactory<L> getFactory() {
		return linkDescriptorFactory;
	}
	
	@Override
	public <T1> IOneParameterMapperState<L, T1> addDynamicParameter(Class<? super T1> clazz) {
		return new CoreOneParameterLinkDescriptorMapperBuilderStateImpl<L, T1>(linkDescriptorFactory, entryBuilders, dynamicParameterTypes, clazz);
	}

}
