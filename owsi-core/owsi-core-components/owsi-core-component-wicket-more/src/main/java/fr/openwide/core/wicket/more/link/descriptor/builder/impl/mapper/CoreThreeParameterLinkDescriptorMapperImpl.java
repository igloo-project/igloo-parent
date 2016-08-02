package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import org.apache.wicket.model.IModel;
import org.javatuples.Triplet;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractThreeParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;

public class CoreThreeParameterLinkDescriptorMapperImpl
		<
		TLinkDescriptor,
		TParam1, TParam2, TParam3
		>
		extends AbstractThreeParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3>
		implements IThreeParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3> {
	private static final long serialVersionUID = -4881770003726056213L;

	private final IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory;

	public CoreThreeParameterLinkDescriptorMapperImpl(IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
		this.factory = factory;
	}
	
	@Override
	public TLinkDescriptor map(
			Triplet<? extends IModel<TParam1>, ? extends IModel<TParam2>, ? extends IModel<TParam3>> param) {
		return factory.create(param);
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}
