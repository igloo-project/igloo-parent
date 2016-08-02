package fr.openwide.core.wicket.more.link.descriptor.builder.impl.mapper;

import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractFourParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

public class CoreFourParameterLinkDescriptorMapperImpl
		<
		TLinkDescriptor,
		TParam1, TParam2, TParam3, TParam4
		>
		extends AbstractFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
		implements IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4> {
	private static final long serialVersionUID = -4881770003726056213L;
	
	private final IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory;

	public CoreFourParameterLinkDescriptorMapperImpl(IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
		this.factory = factory;
	}
	
	@Override
	public TLinkDescriptor map(
			Quartet<? extends IModel<TParam1>, ? extends IModel<TParam2>, ? extends IModel<TParam3>, ? extends IModel<TParam4>> param) {
		return factory.create(param);
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}
