package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractTwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

public class CoreTwoParameterLinkDescriptorMapperImpl<L extends ILinkDescriptor, T1, T2>
		extends AbstractTwoParameterLinkDescriptorMapper<L, T1, T2>
		implements ITwoParameterLinkDescriptorMapper<L, T1, T2> {
	private static final long serialVersionUID = -4881770003726056213L;
	
	private CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory;

	public CoreTwoParameterLinkDescriptorMapperImpl(CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory) {
		this.factory = factory;
	}

	@Override
	public L map(IModel<T1> model1, IModel<T2> model2) {
		return factory.create(Pair.with(model1, model2));
	}

}
