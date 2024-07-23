package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * An object that can create a {@link ILinkDescriptor} using three {@link IModel}s.
 *
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface IThreeParameterLinkDescriptorMapper<L, T1, T2, T3>
    extends ILinkDescriptorMapper<
        L, Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T3>>> {

  /**
   * {@inheritDoc}
   *
   * @deprecated Provided in order to implement {@link ILinkDescriptorMapper}. When you're using a
   *     {@link IThreeParameterLinkDescriptorMapper}, please use {@link #map(IModel, IModel,
   *     IModel)} instead.
   */
  @Override
  @Deprecated
  L map(Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T3>> param);

  /**
   * Map the given models to a newly-created {@link ILinkDescriptor}.
   *
   * @see #map(Triplet)
   */
  L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3);

  ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final IModel<T1> model1);

  ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(
      SerializableFunction2<Pair<T2, T3>, T1> function);

  <U1 extends T1> IThreeParameterLinkDescriptorMapper<L, U1, T2, T3> castParameter1();

  ITwoParameterLinkDescriptorMapper<L, T2, T3> ignoreParameter1();

  ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final IModel<T2> model2);

  ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(
      SerializableFunction2<Pair<T1, T3>, T2> function);

  <U2 extends T2> IThreeParameterLinkDescriptorMapper<L, T1, U2, T3> castParameter2();

  ITwoParameterLinkDescriptorMapper<L, T1, T3> ignoreParameter2();

  ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final IModel<T3> model3);

  ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(
      SerializableFunction2<Pair<T1, T2>, T3> function);

  <U3 extends T3> IThreeParameterLinkDescriptorMapper<L, T1, T2, U3> castParameter3();

  ITwoParameterLinkDescriptorMapper<L, T1, T2> ignoreParameter3();
}
