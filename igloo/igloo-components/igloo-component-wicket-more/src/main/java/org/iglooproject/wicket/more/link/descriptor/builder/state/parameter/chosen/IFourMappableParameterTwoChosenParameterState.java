package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IFourOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.ITwoChosenParameterState;

public interface IFourMappableParameterTwoChosenParameterState<
        TInitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
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
        IFourOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IFourMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TChosenParam2,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IFourMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TChosenParam2,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IFourMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TChosenParam2,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  IFourMappableParameterThreeChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TChosenParam2,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();
}
