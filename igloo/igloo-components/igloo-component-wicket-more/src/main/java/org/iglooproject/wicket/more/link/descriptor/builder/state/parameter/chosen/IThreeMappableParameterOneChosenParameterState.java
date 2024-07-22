package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IThreeOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface IThreeMappableParameterOneChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
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
        IThreeOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IThreeMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TChosenParam1,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IThreeMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TChosenParam1,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IThreeMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TChosenParam1,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();
}
