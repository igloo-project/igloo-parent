package org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderMapperLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.mapper.AbstractSixParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.link.descriptor.mapper.ISixParameterLinkDescriptorMapper;
import org.javatuples.Sextet;

public class CoreSixParameterLinkDescriptorMapperImpl<
        TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5, TParam6>
    extends AbstractSixParameterLinkDescriptorMapper<
        TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5, TParam6>
    implements ISixParameterLinkDescriptorMapper<
        TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5, TParam6> {
  private static final long serialVersionUID = 988165758736038961L;

  private final IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory;

  public CoreSixParameterLinkDescriptorMapperImpl(
      IBuilderMapperLinkDescriptorFactory<TLinkDescriptor> factory) {
    this.factory = factory;
  }

  @Override
  public TLinkDescriptor map(
      Sextet<
              ? extends IModel<TParam1>,
              ? extends IModel<TParam2>,
              ? extends IModel<TParam3>,
              ? extends IModel<TParam4>,
              ? extends IModel<TParam5>,
              ? extends IModel<TParam6>>
          param) {
    return factory.create(param);
  }

  @Override
  public void detach() {
    super.detach();
    factory.detach();
  }
}
