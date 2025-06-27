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
import org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper.CoreFourParameterLinkDescriptorMapperImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IFiveMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IFourMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterFourChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterThreeChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterTwoChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;
import org.javatuples.Tuple;
import org.springframework.core.convert.TypeDescriptor;

final class FourMappableParameterMainStateImpl<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends AbstractOneOrMoreMappableParameterMainStateImpl<
        IFourMappableParameterMainState<
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TLateTargetDefinitionPageLinkDescriptor,
            TLateTargetDefinitionResourceLinkDescriptor,
            TLateTargetDefinitionImageResourceLinkDescriptor>,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    implements IFourMappableParameterMainState<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor> {

  FourMappableParameterMainStateImpl(
      AbstractOneOrMoreMappableParameterMainStateImpl<
              ?,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          previousState,
      LinkParameterTypeInformation<TParam4> addedParameterType) {
    super(previousState, addedParameterType, 4);
  }

  @Override
  public <TParam5>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<TParam5> clazz) {
    return new FiveMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.valueOf(clazz));
  }

  @Override
  public <TParam5 extends Collection<TElement>, TElement>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam5> clazz, Class<TElement> elementType) {
    return new FiveMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.collection(clazz, elementType));
  }

  @Override
  public <TParam5 extends Collection<?>>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(Class<? super TParam5> clazz, TypeDescriptor elementTypeDescriptor) {
    return new FiveMappableParameterMainStateImpl<>(
        this, LinkParameterTypeInformation.collection(clazz, elementTypeDescriptor));
  }

  @Override
  public <TParam5 extends Collection<?>>
      IFiveMappableParameterMainState<
              TParam1,
              TParam2,
              TParam3,
              TParam4,
              TParam5,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          model(
              Class<? super TParam5> clazz,
              TypeDescriptor elementTypeDescriptor,
              SerializableSupplier2<? extends TParam5> emptyCollectionSupplier) {
    return new FiveMappableParameterMainStateImpl<>(
        this,
        LinkParameterTypeInformation.collection(
            clazz, elementTypeDescriptor, emptyCollectionSupplier));
  }

  private <TTarget, TLinkDescriptor>
      IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
          createMapper(
              IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
              IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>
                  pageClassFactory,
              List<Integer> parameterIndices) {
    return new CoreFourParameterLinkDescriptorMapperImpl<>(
        mapperLinkDescriptorFactory(linkDescriptorFactory, pageClassFactory, parameterIndices));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private class FourParameterChosenParameterStateImpl
      extends AbstractInternalChosenParameterStateImpl<FourParameterChosenParameterStateImpl>
      implements IFourMappableParameterOneChosenParameterState,
          IFourMappableParameterTwoChosenParameterState,
          IFourMappableParameterThreeChosenParameterState,
          IFourMappableParameterFourChosenParameterState {

    @Override
    protected FourParameterChosenParameterStateImpl thisAsTSelf() {
      return this;
    }

    @Override
    public IFourParameterLinkDescriptorMapper<
            TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
        page(IDetachableFactory pageClassFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
          pageClassFactory,
          getParameterIndices());
    }

    @Override
    public IFourParameterLinkDescriptorMapper<
            TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
        resource(IDetachableFactory resourceReferenceFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
          resourceReferenceFactory,
          getParameterIndices());
    }

    @Override
    public IFourParameterLinkDescriptorMapper<
            TLateTargetDefinitionImageResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
        imageResource(IDetachableFactory resourceReferenceFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
          resourceReferenceFactory,
          getParameterIndices());
    }
  }

  private FourParameterChosenParameterStateImpl pickNone() {
    return new FourParameterChosenParameterStateImpl();
  }

  @Override
  public FourParameterChosenParameterStateImpl pickFirst() {
    return pickNone().andFirst();
  }

  @Override
  public FourParameterChosenParameterStateImpl pickSecond() {
    return pickNone().andSecond();
  }

  @Override
  public FourParameterChosenParameterStateImpl pickThird() {
    return pickNone().andThird();
  }

  @Override
  public FourParameterChosenParameterStateImpl pickFourth() {
    return pickNone().andFourth();
  }

  @Override
  public IFourParameterLinkDescriptorMapper<
          TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
      page(IModel<? extends Class<? extends Page>> pageClassModel) {
    return pickNone().page(constantModelFactory(pageClassModel));
  }

  @Override
  public IFourParameterLinkDescriptorMapper<
          TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
      resource(IModel<? extends ResourceReference> resourceReferenceModel) {
    return pickNone().resource(constantModelFactory(resourceReferenceModel));
  }

  @Override
  public IFourParameterLinkDescriptorMapper<
          TLateTargetDefinitionImageResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
      imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
    return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
  }

  @Override
  public IFourParameterLinkDescriptorMapper<
          TLateTargetDefinitionPageLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
      page(Class<? extends Page> pageClass) {
    return page(Model.of(pageClass));
  }

  @Override
  public IFourParameterLinkDescriptorMapper<
          TLateTargetDefinitionResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
      resource(ResourceReference resourceReference) {
    return resource(Model.of(resourceReference));
  }

  @Override
  public IFourParameterLinkDescriptorMapper<
          TLateTargetDefinitionImageResourceLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
      imageResource(ResourceReference resourceReference) {
    return imageResource(Model.of(resourceReference));
  }
}
