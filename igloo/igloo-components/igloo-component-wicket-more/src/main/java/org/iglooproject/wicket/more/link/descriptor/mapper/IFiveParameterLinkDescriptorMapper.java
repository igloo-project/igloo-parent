package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

/**
 * An object that can create a {@link ILinkDescriptor} using four {@link IModel}s.
 *
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5>
    extends ILinkDescriptorMapper<
        L,
        Quintet<
            ? extends IModel<T1>,
            ? extends IModel<T2>,
            ? extends IModel<T3>,
            ? extends IModel<T4>,
            ? extends IModel<T5>>> {

  /**
   * {@inheritDoc}
   *
   * @deprecated Provided in order to implement {@link ILinkDescriptorMapper}. When you're using a
   *     {@link IThreeParameterLinkDescriptorMapper}, please use {@link #map(IModel, IModel, IModel,
   *     IModel, )} instead.
   */
  @Override
  @Deprecated
  L map(
      Quintet<
              ? extends IModel<T1>,
              ? extends IModel<T2>,
              ? extends IModel<T3>,
              ? extends IModel<T4>,
              ? extends IModel<T5>>
          param);

  /**
   * Map the given models to a newly-created {@link ILinkDescriptor}.
   *
   * @see #map(Quartet)
   */
  L map(
      IModel<T1> model1,
      IModel<T2> model2,
      IModel<T3> model3,
      IModel<T4> model4,
      IModel<T5> model5);

  IFourParameterLinkDescriptorMapper<L, T2, T3, T4, T5> setParameter1(final IModel<T1> model1);

  IFourParameterLinkDescriptorMapper<L, T2, T3, T4, T5> ignoreParameter1();

  IFourParameterLinkDescriptorMapper<L, T1, T3, T4, T5> setParameter2(final IModel<T2> model2);

  IFourParameterLinkDescriptorMapper<L, T1, T3, T4, T5> ignoreParameter2();

  IFourParameterLinkDescriptorMapper<L, T1, T2, T4, T5> setParameter3(final IModel<T3> model3);

  IFourParameterLinkDescriptorMapper<L, T1, T2, T4, T5> ignoreParameter3();

  IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T5> setParameter4(final IModel<T4> model4);

  IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T5> ignoreParameter4();

  IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> setParameter5(final IModel<T5> model5);

  IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> ignoreParameter5();
}
