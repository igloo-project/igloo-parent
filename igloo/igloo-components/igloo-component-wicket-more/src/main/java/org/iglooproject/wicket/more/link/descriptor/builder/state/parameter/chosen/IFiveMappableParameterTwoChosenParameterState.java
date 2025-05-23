package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IFiveOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.ITwoChosenParameterState;

public interface IFiveMappableParameterTwoChosenParameterState<
        TInitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TChosenParam1,
        TChosenParam2,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends ITwoChosenParameterState<
            TInitialState,
            TChosenParam1,
            TChosenParam2,
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult>,
        IFiveOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IFiveMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IFiveMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IFiveMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  IFiveMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  IFiveMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();
}
