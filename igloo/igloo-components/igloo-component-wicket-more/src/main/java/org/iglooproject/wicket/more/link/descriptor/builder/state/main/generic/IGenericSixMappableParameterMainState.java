package org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic;

import org.iglooproject.wicket.more.link.descriptor.builder.state.main.ISixMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.ISixOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ISixMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the
 * generic type parameters are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link
 * TLateTargetDefinitionResourceResult}, etc.) in {@link ISixMappableParameterMainState}, because it
 * would be even more unreadable than it is now.
 */
public interface IGenericSixMappableParameterMainState<
        TSelf extends IMainState<TSelf>,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TParam6,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IMainState<TSelf>,
        ISixOrMoreMappableParameterNoneChosenChoiceState,
        ILateTargetDefinitionTerminalState<
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult> {

  @Override
  ISixMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFirst();

  @Override
  ISixMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickSecond();

  @Override
  ISixMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickThird();

  @Override
  ISixMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFourth();

  @Override
  ISixMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFifth();

  @Override
  ISixMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam6,
          TParam6,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickSixth();
}
