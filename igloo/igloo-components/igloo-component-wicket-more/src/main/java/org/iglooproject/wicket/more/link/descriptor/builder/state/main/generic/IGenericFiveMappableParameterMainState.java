package org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic;

import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IFiveMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.IFiveOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFiveMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the
 * generic type parameters are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link
 * TLateTargetDefinitionResourceResult}, etc.) in {@link IFiveMappableParameterMainState}, because
 * it would be even more unreadable than it is now.
 */
public interface IGenericFiveMappableParameterMainState<
        TSelf extends IMainState<TSelf>,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TParam5,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IMainState<TSelf>,
        IFiveOrMoreMappableParameterNoneChosenChoiceState,
        ILateTargetDefinitionTerminalState<
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult> {

  @Override
  IFiveMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFirst();

  @Override
  IFiveMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickSecond();

  @Override
  IFiveMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickThird();

  @Override
  IFiveMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFourth();

  @Override
  IFiveMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam5,
          TParam5,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFifth();
}
