package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen.ISixOrMoreMappableParameterSomeChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IThreeChosenParameterState;

public interface ISixMappableParameterThreeChosenParameterState<
        InitialState,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
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
        ISixOrMoreMappableParameterSomeChosenChoiceState {

  @Override
  ISixMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFirst();

  @Override
  ISixMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSecond();

  @Override
  ISixMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andThird();

  @Override
  ISixMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFourth();

  @Override
  ISixMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andFifth();

  @Override
  ISixMappableParameterFourChosenParameterState<
          InitialState,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TChosenParam1,
          TChosenParam2,
          TChosenParam3,
          TParam6,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      andSixth();
}
