package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.model.IModel;
import org.javatuples.Triplet;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractThreeParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;

public class CoreThreeParameterLinkDescriptorMapperImpl
		<
		TLinkDescriptor extends ILinkDescriptor,
		TParam1, TParam2, TParam3
		>
		extends AbstractThreeParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3>
		implements IThreeParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3> {
	private static final long serialVersionUID = -4881770003726056213L;
	
	private final CoreLinkDescriptorMapperLinkDescriptorFactory<TLinkDescriptor> factory;

	public CoreThreeParameterLinkDescriptorMapperImpl(CoreLinkDescriptorMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
		this.factory = factory;
	}

	@Override
	public TLinkDescriptor map(IModel<TParam1> model1, IModel<TParam2> model2, IModel<TParam3> model3) {
		return factory.create(Triplet.with(model1, model2, model3));
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}
