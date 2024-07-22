package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IThreeOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.ITwoChosenParameterState;

public interface IThreeMappableParameterTwoChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
        TChosenParam1,
        TChosenParam2,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends ITwoChosenParameterState<
            InitialState,
            TChosenParam1,
            TChosenParam2,
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult>,
        IThreeOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IThreeMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TChosenParam1,
          TChosenParam2,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IThreeMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TChosenParam1,
          TChosenParam2,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IThreeMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TChosenParam1,
          TChosenParam2,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();
}
