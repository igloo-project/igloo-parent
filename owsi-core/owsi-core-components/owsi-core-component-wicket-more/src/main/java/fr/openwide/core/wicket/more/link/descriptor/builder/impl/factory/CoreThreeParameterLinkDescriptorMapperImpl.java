package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.model.IModel;
import org.javatuples.Triplet;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractThreeParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;

public class CoreThreeParameterLinkDescriptorMapperImpl<L extends ILinkDescriptor, T1, T2, T3>
		extends AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3>
		implements IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> {
	private static final long serialVersionUID = -4881770003726056213L;
	
	private CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory;

	public CoreThreeParameterLinkDescriptorMapperImpl(CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory) {
		this.factory = factory;
	}

	@Override
	public L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3) {
		return factory.create(Triplet.with(model1, model2, model3));
	}

}
