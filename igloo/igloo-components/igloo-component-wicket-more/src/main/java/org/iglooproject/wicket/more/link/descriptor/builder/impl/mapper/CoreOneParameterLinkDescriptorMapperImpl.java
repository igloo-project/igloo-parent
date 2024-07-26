package org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractOneParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import org.javatuples.Unit;

public class CoreOneParameterLinkDescriptorMapperImpl<TLinkDescriptor, TParam1>
    extends AbstractOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>
    implements IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1> {
  private static final long serialVersionUID = 4229547587275320914L;

  private final IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory;

  public CoreOneParameterLinkDescriptorMapperImpl(
      IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
    this.factory = factory;
  }

  @Override
  public TLinkDescriptor map(IModel<TParam1> model) {
    return factory.create(Unit.with(model));
  }

  @Override
  public void detach() {
    super.detach();
    factory.detach();
  }
}
