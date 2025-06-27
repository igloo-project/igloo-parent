package org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractFiveParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.IFiveParameterLinkDescriptorMapper;
import org.javatuples.Quintet;

public class CoreFiveParameterLinkDescriptorMapperImpl<
        TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
    extends AbstractFiveParameterLinkDescriptorMapper<
        TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
    implements IFiveParameterLinkDescriptorMapper<
        TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5> {
  private static final long serialVersionUID = 2175256293862302287L;

  private final IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory;

  public CoreFiveParameterLinkDescriptorMapperImpl(
      IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
    this.factory = factory;
  }

  @Override
  public TLinkDescriptor map(
      Quintet<
              ? extends IModel<TParam1>,
              ? extends IModel<TParam2>,
              ? extends IModel<TParam3>,
              ? extends IModel<TParam4>,
              ? extends IModel<TParam5>>
          param) {
    return factory.create(param);
  }

  @Override
  public void detach() {
    super.detach();
    factory.detach();
  }
}
