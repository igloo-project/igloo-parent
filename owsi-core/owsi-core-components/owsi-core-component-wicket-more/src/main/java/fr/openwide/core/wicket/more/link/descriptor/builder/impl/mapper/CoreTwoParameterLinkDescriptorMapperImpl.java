package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractTwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

public class CoreTwoParameterLinkDescriptorMapperImpl<TLinkDescriptor, TParam1, TParam2>
		extends AbstractTwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>
		implements ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2> {
	private static final long serialVersionUID = -4881770003726056213L;

	private final IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory;

	public CoreTwoParameterLinkDescriptorMapperImpl(IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
		this.factory = factory;
	}
	
	@Override
	public TLinkDescriptor map(Pair<? extends IModel<TParam1>, ? extends IModel<TParam2>> param) {
		return factory.create(param);
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}
