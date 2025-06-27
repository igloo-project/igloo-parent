package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IFiveOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface IFiveMappableParameterOneChosenParameterState<
        TInitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TChosenParam1,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IOneChosenParameterState<
            TInitialState,
            TChosenParam1,
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult>,
        IFiveOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IFiveMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IFiveMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IFiveMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  IFiveMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  IFiveMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();
}
