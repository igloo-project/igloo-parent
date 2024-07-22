package org.iglooproject.wicket.more.link.descriptor.builder.impl.parameter;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import org.javatuples.Tuple;

public abstract class AbstractCoreAddedParameterMapperStateImpl<TNextState, TTuple extends Tuple>
    implements IAddedParameterMappingState<TNextState> {

  private final LinkParameterMappingEntryBuilder<TTuple> parameterEntryBuilder;

  public AbstractCoreAddedParameterMapperStateImpl(
      LinkParameterMappingEntryBuilder<TTuple> parameterEntryBuilder) {
    this.parameterEntryBuilder = parameterEntryBuilder;
  }

  protected abstract TNextState toNextState(
      LinkParameterMappingEntryBuilder<TTuple> parameterEntryBuilder);

  @Override
  public TNextState mandatory() {
    parameterEntryBuilder.setMandatory(true);
    return toNextState(parameterEntryBuilder);
  }

  @Override
  public TNextState optional() {
    parameterEntryBuilder.setMandatory(false);
    return toNextState(parameterEntryBuilder);
  }
}
