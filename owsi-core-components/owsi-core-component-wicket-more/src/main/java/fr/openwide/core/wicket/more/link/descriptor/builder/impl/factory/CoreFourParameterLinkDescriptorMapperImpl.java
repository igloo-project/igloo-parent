package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractFourParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

public class CoreFourParameterLinkDescriptorMapperImpl<L extends ILinkDescriptor, T1, T2, T3, T4>
		extends AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4>
		implements IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> {
	private static final long serialVersionUID = -4881770003726056213L;
	
	private CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory;

	public CoreFourParameterLinkDescriptorMapperImpl(CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory) {
		this.factory = factory;
	}

	@Override
	public L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3, IModel<T4> model4) {
		return factory.create(Quartet.with(model1, model2, model3, model4));
	}

}
