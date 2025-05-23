package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.ISixOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.ITwoChosenParameterState;

public interface ISixMappableParameterTwoChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
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
        ISixOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  ISixMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  ISixMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  ISixMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  ISixMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  ISixMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();

  @Override
  ISixMappableParameterThreeChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TParam6,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSixth();
}
