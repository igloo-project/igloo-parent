package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;

/**
 * An object that can create a {@link ILinkDescriptor} using one {@link IModel}.
 *
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface IOneParameterLinkDescriptorMapper<L, T1>
    extends ILinkDescriptorMapper<L, IModel<T1>> {

  /** {@inheritDoc} */
  @Override
  L map(IModel<T1> model);

  L setParameter1(final IModel<T1> model1);

  <U1 extends T1> IOneParameterLinkDescriptorMapper<L, U1> castParameter1();

  L ignoreParameter1();
}
