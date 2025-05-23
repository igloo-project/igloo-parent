package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.IFiveOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IThreeChosenParameterState;

public interface IFiveMappableParameterThreeChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TChosenParam1,
        TChosenParam2,
        TChosenParam3,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IThreeChosenParameterState<
            InitialState,
            TChosenParam1,
            TChosenParam2,
            TChosenParam3,
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult>,
        IFiveOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  IFiveMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  IFiveMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  IFiveMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  IFiveMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  IFiveMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();
}
