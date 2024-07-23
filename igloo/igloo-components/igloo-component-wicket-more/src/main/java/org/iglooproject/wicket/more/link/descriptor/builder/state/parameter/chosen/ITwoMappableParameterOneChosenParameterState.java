package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.ITwoOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface ITwoMappableParameterOneChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TChosenParam1,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IOneChosenParameterState<
            InitialState,
            TChosenParam1,
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult>,
        ITwoOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  ITwoMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TChosenParam1,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  ITwoMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TChosenParam1,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();
}
