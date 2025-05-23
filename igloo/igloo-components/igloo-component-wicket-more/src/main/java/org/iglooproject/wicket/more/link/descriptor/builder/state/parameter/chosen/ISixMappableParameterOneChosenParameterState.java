package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.ISixOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface ISixMappableParameterOneChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
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
        ISixOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  ISixMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  ISixMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  ISixMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  ISixMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  ISixMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();

  @Override
  ISixMappableParameterTwoChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TParam6,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSixth();
}
