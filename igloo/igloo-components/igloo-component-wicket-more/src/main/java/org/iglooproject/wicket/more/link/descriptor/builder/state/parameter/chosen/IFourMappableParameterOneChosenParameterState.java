package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IFourOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface IFourMappableParameterOneChosenParameterState<
        TInitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
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
        IFourOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IFourMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IFourMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IFourMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  IFourMappableParameterTwoChosenParameterState<
          TInitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TChosenParam1,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();
}
