package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractFourParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

public class CoreFourParameterLinkDescriptorMapperImpl
		<
		TLinkDescriptor extends ILinkDescriptor,
		TParam1, TParam2, TParam3, TParam4
		>
		extends AbstractFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
		implements IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4> {
	private static final long serialVersionUID = -4881770003726056213L;
	
	private final CoreLinkDescriptorMapperLinkDescriptorFactory<TLinkDescriptor> factory;

	public CoreFourParameterLinkDescriptorMapperImpl(CoreLinkDescriptorMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
		this.factory = factory;
	}

	@Override
	public TLinkDescriptor map(IModel<TParam1> model1, IModel<TParam2> model2, IModel<TParam3> model3, IModel<TParam4> model4) {
		return factory.create(Quartet.with(model1, model2, model3, model4));
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}
