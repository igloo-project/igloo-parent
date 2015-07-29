package fr.openwide.core.wicket.more.link.descriptor.builder.impl.factory;

import org.apache.wicket.model.IModel;
import org.javatuples.Unit;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

public class CoreOneParameterLinkDescriptorMapperImpl<L extends ILinkDescriptor, T1>
		extends AbstractOneParameterLinkDescriptorMapper<L, T1>
		implements IOneParameterLinkDescriptorMapper<L, T1> {
	private static final long serialVersionUID = 4229547587275320914L;
	
	private final CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory;

	public CoreOneParameterLinkDescriptorMapperImpl(CoreLinkDescriptorMapperLinkDescriptorFactory<L> factory) {
		this.factory = factory;
	}

	@Override
	public L map(IModel<T1> model) {
		return factory.create(Unit.with(model));
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}
