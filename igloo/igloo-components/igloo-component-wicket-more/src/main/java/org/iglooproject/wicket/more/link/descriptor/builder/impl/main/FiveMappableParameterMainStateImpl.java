package org.iglooproject.wicket.more.link.descriptor.builder.impl.main;

import igloo.wicket.factory.IDetachableFactory;
import java.util.Collection;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper.CoreFiveParameterLinkDescriptorMapperImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IFiveMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.ISixMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFiveMappableParameterFiveChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFiveMappableParameterFourChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFiveMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFiveMappableParameterThreeChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFiveMappableParameterTwoChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IFiveParameterLinkDescriptorMapper;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;

final class FiveMappableParameterMainStateImpl<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends AbstractOneOrMoreMappableParameterMainStateImpl<
        IFiveMappableParameterMainState<
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            TLateTargetDefinitionPageLinkDescriptor,
            TLateTargetDefinitionResourceLinkDescriptor,
            TLateTargetDefinitionImageResourceLinkDescriptor>,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    implements IFiveMappableParameterMainState<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor> {

  FiveMappableParameterMainStateImpl(
      AbstractOneOrMoreMappableParameterMainStateImpl<
              ?,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          previousState,
      LinkParameterTypeInformation<TParam5> addedParameterType) {
    super(previousState, addedParameterType, 5);
  }

  @Override
  public <TParam6>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam6> clazz) {
    return new SixMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.valueOf(clazz));
  }

  @Override
  public <TParam6 extends Collection<TElement>, TElement>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam6> clazz, Class<TElement> elementType) {
    return new SixMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.collection(clazz, elementType));
  }

  @Override
  public <TParam6 extends Collection<?>>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam6> clazz, TypeDescriptor elementTypeDescriptor) {
    return new SixMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor));
  }

  @Override
  public <TParam6 extends Collection<?>>
      ISixMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TParam6,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam6> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam6> emptyCollectionSupplier) {
    return new SixMappableParameterMainStateImpl<>(
        this,
        LinkParameterTypeInformation.collection(
            clazz, elementTypeDescriptor, emptyCollectionSupplier));
  }

  private <TTarget, TLinkDescriptor>
      IFiveParameterLinkDescriptorMapper<
              TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
          createMapper(
              IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
              IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>
                  pageClassFactory,
              List<Integer> parameterIndices) {
    return new CoreFiveParameterLinkDescriptorMapperImpl<>(
        mapperLinkDescriptorFactory(linkDescriptorFactory, pageClassFactory, parameterIndices));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private class FiveParameterChosenParameterStateImpl
      extends AbstractInternalChosenParameterStateImpl<FiveParameterChosenParameterStateImpl>
      implements IFiveMappableParameterOneChosenParameterState,
          IFiveMappableParameterTwoChosenParameterState,
          IFiveMappableParameterThreeChosenParameterState,
          IFiveMappableParameterFourChosenParameterState,
          IFiveMappableParameterFiveChosenParameterState {

    @Override
    protected FiveParameterChosenParameterStateImpl thisAsTSelf() {
      return this;
    }

    @Override
    public IFiveParameterLinkDescriptorMapper<
            TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
        page(IDetachableFactory pageClassFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
          pageClassFactory,
          getParameterIndices());
    }

    @Override
    public IFiveParameterLinkDescriptorMapper<
            TLateTargetDefinitionResourceLinkDescriptor,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5>
        resource(IDetachableFactory resourceReferenceFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
          resourceReferenceFactory,
          getParameterIndices());
    }

    @Override
    public IFiveParameterLinkDescriptorMapper<
            TLateTargetDefinitionImageResourceLinkDescriptor,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5>
        imageResource(IDetachableFactory resourceReferenceFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
          resourceReferenceFactory,
          getParameterIndices());
    }
  }

  private FiveParameterChosenParameterStateImpl pickNone() {
    return new FiveParameterChosenParameterStateImpl();
  }

  @Override
  public FiveParameterChosenParameterStateImpl pickFirst() {
    return pickNone().andFirst();
  }

  @Override
  public FiveParameterChosenParameterStateImpl pickSecond() {
    return pickNone().andSecond();
  }

  @Override
  public FiveParameterChosenParameterStateImpl pickThird() {
    return pickNone().andThird();
  }

  @Override
  public FiveParameterChosenParameterStateImpl pickFourth() {
    return pickNone().andFourth();
  }

  @Override
  public FiveParameterChosenParameterStateImpl pickFifth() {
    return pickNone().andFifth();
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<
          TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
      page(IModel<? extends Class<? extends Page>> pageClassModel) {
    return pickNone().page(constantModelFactory(pageClassModel));
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<
          TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
      resource(IModel<? extends ResourceReference> resourceReferenceModel) {
    return pickNone().resource(constantModelFactory(resourceReferenceModel));
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<
          TLateTargetDefinitionImageResourceLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5>
      imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
    return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<
          TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
      page(Class<? extends Page> pageClass) {
    return page(Model.of(pageClass));
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<
          TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5>
      resource(ResourceReference resourceReference) {
    return resource(Model.of(resourceReference));
  }

  @Override
  public IFiveParameterLinkDescriptorMapper<
          TLateTargetDefinitionImageResourceLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5>
      imageResource(ResourceReference resourceReference) {
    return imageResource(Model.of(resourceReference));
  }
}
