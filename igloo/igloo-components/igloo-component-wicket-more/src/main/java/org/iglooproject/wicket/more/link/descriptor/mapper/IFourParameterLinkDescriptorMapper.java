package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.javatuples.Quartet;

/**
 * An object that can create a {@link ILinkDescriptor} using four {@link IModel}s.
 *
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4>
    extends ILinkDescriptorMapper<
        L,
        Quartet<
            ? extends IModel<T1>,
            ? extends IModel<T2>,
            ? extends IModel<T3>,
            ? extends IModel<T4>>> {

  /**
   * {@inheritDoc}
   *
   * @deprecated Provided in order to implement {@link ILinkDescriptorMapper}. When you're using a
   *     {@link IThreeParameterLinkDescriptorMapper}, please use {@link #map(IModel, IModel,
   *     IModel)} instead.
   */
  @Override
  @Deprecated
  L map(
      Quartet<
              ? extends IModel<T1>,
              ? extends IModel<T2>,
              ? extends IModel<T3>,
              ? extends IModel<T4>>
          param);

  /**
   * Map the given models to a newly-created {@link ILinkDescriptor}.
   *
   * @see #map(Quartet)
   */
  L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3, IModel<T4> model4);

  IThreeParameterLinkDescriptorMapper<L, T2, T3, T4> setParameter1(final IModel<T1> model1);

  IThreeParameterLinkDescriptorMapper<L, T2, T3, T4> ignoreParameter1();

  IThreeParameterLinkDescriptorMapper<L, T1, T3, T4> setParameter2(final IModel<T2> model2);

  IThreeParameterLinkDescriptorMapper<L, T1, T3, T4> ignoreParameter2();

  IThreeParameterLinkDescriptorMapper<L, T1, T2, T4> setParameter3(final IModel<T3> model3);

  IThreeParameterLinkDescriptorMapper<L, T1, T2, T4> ignoreParameter3();

  IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> setParameter4(final IModel<T4> model4);

  IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> ignoreParameter4();
}
