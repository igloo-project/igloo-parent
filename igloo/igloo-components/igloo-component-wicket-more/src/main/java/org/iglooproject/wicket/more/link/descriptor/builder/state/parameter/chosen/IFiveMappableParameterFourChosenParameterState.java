package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IFiveOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IFourChosenParameterState;

public interface IFiveMappableParameterFourChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TChosenParam1,
        TChosenParam2,
        TChosenParam3,
        TChosenParam4,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IFourChosenParameterState<
            InitialState,
            TChosenParam1,
            TChosenParam2,
            TChosenParam3,
            TChosenParam4,
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult>,
        IFiveOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IFiveMappableParameterFiveChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TChosenParam4,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IFiveMappableParameterFiveChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TChosenParam4,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IFiveMappableParameterFiveChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TChosenParam4,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  IFiveMappableParameterFiveChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TChosenParam4,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  IFiveMappableParameterFiveChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TChosenParam4,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();
}
