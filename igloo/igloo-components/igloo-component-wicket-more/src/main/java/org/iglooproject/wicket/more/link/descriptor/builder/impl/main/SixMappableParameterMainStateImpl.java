package org.iglooproject.wicket.more.link.descriptor.builder.impl.main;

import igloo.wicket.factory.IDetachableFactory;
import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.factory.IBuilderLinkDescriptorFactory;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.mapper.CoreSixParameterLinkDescriptorMapperImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.ISixMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterFiveChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterFourChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterSixChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterThreeChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterTwoChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.mapper.ISixParameterLinkDescriptorMapper;
import org.javatuples.Tuple;

final class SixMappableParameterMainStateImpl<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends AbstractOneOrMoreMappableParameterMainStateImpl<
        ISixMappableParameterMainState<
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            TParam6,
            TLateTargetDefinitionPageLinkDescriptor,
            TLateTargetDefinitionResourceLinkDescriptor,
            TLateTargetDefinitionImageResourceLinkDescriptor>,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    implements ISixMappableParameterMainState<
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor> {

  SixMappableParameterMainStateImpl(
      AbstractOneOrMoreMappableParameterMainStateImpl<
              ?,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          previousState,
      LinkParameterTypeInformation<TParam6> addedParameterType) {
    super(previousState, addedParameterType, 6);
  }

  private <TTarget, TLinkDescriptor>
      ISixParameterLinkDescriptorMapper<
              TLinkDescriptor, TParam1, TParam2, TParam3, TParam4, TParam5, TParam6>
          createMapper(
              IBuilderLinkDescriptorFactory<TTarget, TLinkDescriptor> linkDescriptorFactory,
              IDetachableFactory<? extends Tuple, ? extends IModel<? extends TTarget>>
                  pageClassFactory,
              List<Integer> parameterIndices) {
    return new CoreSixParameterLinkDescriptorMapperImpl<>(
        mapperLinkDescriptorFactory(linkDescriptorFactory, pageClassFactory, parameterIndices));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private class SixParameterChosenParameterStateImpl
      extends AbstractInternalChosenParameterStateImpl<SixParameterChosenParameterStateImpl>
      implements ISixMappableParameterOneChosenParameterState,
          ISixMappableParameterTwoChosenParameterState,
          ISixMappableParameterThreeChosenParameterState,
          ISixMappableParameterFourChosenParameterState,
          ISixMappableParameterFiveChosenParameterState,
          ISixMappableParameterSixChosenParameterState {

    @Override
    protected SixParameterChosenParameterStateImpl thisAsTSelf() {
      return this;
    }

    @Override
    public ISixParameterLinkDescriptorMapper<
            TLateTargetDefinitionPageLinkDescriptor,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            TParam6>
        page(IDetachableFactory pageClassFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionPageLinkDescriptorFactory(),
          pageClassFactory,
          getParameterIndices());
    }

    @Override
    public ISixParameterLinkDescriptorMapper<
            TLateTargetDefinitionResourceLinkDescriptor,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            TParam6>
        resource(IDetachableFactory resourceReferenceFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionResourceLinkDescriptorFactory(),
          resourceReferenceFactory,
          getParameterIndices());
    }

    @Override
    public ISixParameterLinkDescriptorMapper<
            TLateTargetDefinitionImageResourceLinkDescriptor,
            TParam1,
            TParam2,
            TParam3,
            TParam4,
            TParam5,
            TParam6>
        imageResource(IDetachableFactory resourceReferenceFactory) {
      return createMapper(
          getTargetFactories().getLateTargetDefinitionImageResourceLinkDescriptorFactory(),
          resourceReferenceFactory,
          getParameterIndices());
    }
  }

  private SixParameterChosenParameterStateImpl pickNone() {
    return new SixParameterChosenParameterStateImpl();
  }

  @Override
  public SixParameterChosenParameterStateImpl pickFirst() {
    return pickNone().andFirst();
  }

  @Override
  public SixParameterChosenParameterStateImpl pickSecond() {
    return pickNone().andSecond();
  }

  @Override
  public SixParameterChosenParameterStateImpl pickThird() {
    return pickNone().andThird();
  }

  @Override
  public SixParameterChosenParameterStateImpl pickFourth() {
    return pickNone().andFourth();
  }

  @Override
  public SixParameterChosenParameterStateImpl pickFifth() {
    return pickNone().andFifth();
  }

  @Override
  public SixParameterChosenParameterStateImpl pickSixth() {
    return pickNone().andSixth();
  }

  @Override
  public ISixParameterLinkDescriptorMapper<
          TLateTargetDefinitionPageLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6>
      page(IModel<? extends Class<? extends Page>> pageClassModel) {
    return pickNone().page(constantModelFactory(pageClassModel));
  }

  @Override
  public ISixParameterLinkDescriptorMapper<
          TLateTargetDefinitionResourceLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6>
      resource(IModel<? extends ResourceReference> resourceReferenceModel) {
    return pickNone().resource(constantModelFactory(resourceReferenceModel));
  }

  @Override
  public ISixParameterLinkDescriptorMapper<
          TLateTargetDefinitionImageResourceLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6>
      imageResource(IModel<? extends ResourceReference> resourceReferenceModel) {
    return pickNone().imageResource(constantModelFactory(resourceReferenceModel));
  }

  @Override
  public ISixParameterLinkDescriptorMapper<
          TLateTargetDefinitionPageLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6>
      page(Class<? extends Page> pageClass) {
    return page(Model.of(pageClass));
  }

  @Override
  public ISixParameterLinkDescriptorMapper<
          TLateTargetDefinitionResourceLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6>
      resource(ResourceReference resourceReference) {
    return resource(Model.of(resourceReference));
  }

  @Override
  public ISixParameterLinkDescriptorMapper<
          TLateTargetDefinitionImageResourceLinkDescriptor,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6>
      imageResource(ResourceReference resourceReference) {
    return imageResource(Model.of(resourceReference));
  }
}
