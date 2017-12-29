package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.link.descriptor.factory.LinkGeneratorFactory;
import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;

@SuppressWarnings("deprecation")
public class LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter<T>
		extends AbstractOneParameterLinkDescriptorMapper<ILinkGenerator, T> {
	private static final long serialVersionUID = -8490229055203194996L;
	
	private final LinkGeneratorFactory<T> factory;
	
	public LinkGeneratorFactoryToOneParameterLinkDescriptorMapperAdapter(LinkGeneratorFactory<T> factory) {
		super();
		this.factory = factory;
	}

	@Override
	public ILinkGenerator map(IModel<T> model) {
		return factory.create(model);
	}
}