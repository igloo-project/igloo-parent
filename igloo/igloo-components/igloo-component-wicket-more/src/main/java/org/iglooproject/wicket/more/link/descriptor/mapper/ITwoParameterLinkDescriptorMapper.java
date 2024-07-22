package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.javatuples.Pair;

/**
 * An object that can create a {@link ILinkDescriptor} using two {@link IModel}s.
 *
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface ITwoParameterLinkDescriptorMapper<L, T1, T2>
    extends ILinkDescriptorMapper<L, Pair<? extends IModel<T1>, ? extends IModel<T2>>> {

  /**
   * {@inheritDoc}
   *
   * @deprecated Provided in order to implement {@link ILinkDescriptorMapper}. When you're using a
   *     {@link ITwoParameterLinkDescriptorMapper}, please use {@link #map(IModel, IModel)} instead.
   */
  @Override
  @Deprecated
  L map(Pair<? extends IModel<T1>, ? extends IModel<T2>> param);

  /**
   * Map the given models to a newly-created {@link ILinkDescriptor}.
   *
   * @see #map(Pair)
   */
  L map(IModel<T1> model1, IModel<T2> model2);

  IOneParameterLinkDescriptorMapper<L, T2> setParameter1(final IModel<T1> model1);

  IOneParameterLinkDescriptorMapper<L, T2> setParameter1(SerializableFunction2<T2, T1> function);

  <U1 extends T1> ITwoParameterLinkDescriptorMapper<L, U1, T2> castParameter1();

  IOneParameterLinkDescriptorMapper<L, T2> ignoreParameter1();

  IOneParameterLinkDescriptorMapper<L, T1> setParameter2(final IModel<T2> model2);

  IOneParameterLinkDescriptorMapper<L, T1> setParameter2(SerializableFunction2<T1, T2> function);

  <U2 extends T2> ITwoParameterLinkDescriptorMapper<L, T1, U2> castParameter2();

  IOneParameterLinkDescriptorMapper<L, T1> ignoreParameter2();
}
