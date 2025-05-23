package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.descriptor.ILinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import org.javatuples.Quartet;
import org.javatuples.Sextet;

/**
 * An object that can create a {@link ILinkDescriptor} using four {@link IModel}s.
 *
 * @see ILinkDescriptorMapper
 * @see IMappableParameterDeclarationState#model(Class)
 */
public interface ISixParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5, T6>
    extends ILinkDescriptorMapper<
        L,
        Sextet<
            ? extends IModel<T1>,
            ? extends IModel<T2>,
            ? extends IModel<T3>,
            ? extends IModel<T4>,
            ? extends IModel<T5>,
            ? extends IModel<T6>>> {

  /**
   * {@inheritDoc}
   *
   * @deprecated Provided in order to implement {@link ILinkDescriptorMapper}. When you're using a
   *     {@link IThreeParameterLinkDescriptorMapper}, please use {@link #map(IModel, IModel, IModel,
   *     IModel, IModel, IModel)} instead.
   */
  @Override
  @Deprecated
  L map(
      Sextet<
              ? extends IModel<T1>,
              ? extends IModel<T2>,
              ? extends IModel<T3>,
              ? extends IModel<T4>,
              ? extends IModel<T5>,
              ? extends IModel<T6>>
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
      IModel<T5> model5,
      IModel<T6> model6);

  IFiveParameterLinkDescriptorMapper<L, T2, T3, T4, T5, T6> setParameter1(final IModel<T1> model1);

  IFiveParameterLinkDescriptorMapper<L, T2, T3, T4, T5, T6> ignoreParameter1();

  IFiveParameterLinkDescriptorMapper<L, T1, T3, T4, T5, T6> setParameter2(final IModel<T2> model2);

  IFiveParameterLinkDescriptorMapper<L, T1, T3, T4, T5, T6> ignoreParameter2();

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T4, T5, T6> setParameter3(final IModel<T3> model3);

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T4, T5, T6> ignoreParameter3();

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T5, T6> setParameter4(final IModel<T4> model4);

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T5, T6> ignoreParameter4();

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T6> setParameter5(final IModel<T5> model5);

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T6> ignoreParameter5();

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5> setParameter6(final IModel<T6> model6);

  IFiveParameterLinkDescriptorMapper<L, T1, T2, T3, T4, T5> ignoreParameter6();
}
