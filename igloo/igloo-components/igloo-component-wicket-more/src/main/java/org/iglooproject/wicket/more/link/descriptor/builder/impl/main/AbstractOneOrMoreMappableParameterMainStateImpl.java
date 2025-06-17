package org.iglooproject.wicket.more.link.descriptor.builder.impl.main;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.wicket.util.lang.Args;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.AbstractChosenParameterStateImpl;
import org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter.LinkParameterTypeInformation;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.iglooproject.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import org.iglooproject.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;

abstract class AbstractOneOrMoreMappableParameterMainStateImpl<
        TSelf extends IMainState<TSelf>,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor>
    extends AbstractMainStateImpl<
        TSelf,
        TLateTargetDefinitionPageLinkDescriptor,
        TLateTargetDefinitionResourceLinkDescriptor,
        TLateTargetDefinitionImageResourceLinkDescriptor> {

  protected final List<LinkParameterTypeInformation<?>> dynamicParameterTypes;

  public AbstractOneOrMoreMappableParameterMainStateImpl(
      NoMappableParameterMainStateImpl<
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          previousState,
      LinkParameterTypeInformation<?> addedParameterType) {
    super(previousState);
    this.dynamicParameterTypes = List.of(addedParameterType);
  }

  public AbstractOneOrMoreMappableParameterMainStateImpl(
      AbstractOneOrMoreMappableParameterMainStateImpl<
              ?,
              TLateTargetDefinitionPageLinkDescriptor,
              TLateTargetDefinitionResourceLinkDescriptor,
              TLateTargetDefinitionImageResourceLinkDescriptor>
          previousState,
      LinkParameterTypeInformation<?> addedParameterType,
      int expectedNumberOfParameters) {
    super(previousState);
    this.dynamicParameterTypes =
        ImmutableList.<LinkParameterTypeInformation<?>>builder()
            .addAll(previousState.dynamicParameterTypes)
            .add(addedParameterType)
            .build();
    Args.withinRange(
        expectedNumberOfParameters,
        expectedNumberOfParameters,
        dynamicParameterTypes.size(),
        "dynamicParameterTypes.size()");
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected abstract class AbstractInternalChosenParameterStateImpl<TSelfChosen>
      extends AbstractChosenParameterStateImpl<TSelfChosen, TSelf> {

    @Override
    protected LinkParameterTypeInformation<?> getParameterTypeInformation(int index) {
      return dynamicParameterTypes.get(index);
    }

    @Override
    public IAddedParameterMappingState<TSelf> map(ILinkParameterMappingEntryFactory entryFactory) {
      return AbstractOneOrMoreMappableParameterMainStateImpl.this.doMap(
          entryFactory, getParameterIndices());
    }

    @Override
    public TSelf validator(ILinkParameterValidatorFactory factory) {
      return (TSelf)
          AbstractOneOrMoreMappableParameterMainStateImpl.this.doValidator(
              factory, getParameterIndices());
    }
  }
}
