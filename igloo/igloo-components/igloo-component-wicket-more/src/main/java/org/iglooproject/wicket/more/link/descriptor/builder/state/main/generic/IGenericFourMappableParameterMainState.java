package org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic;

import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IFourMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.IFourOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the
 * generic type parameters are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link
 * TLateTargetDefinitionResourceResult}, etc.) in {@link IFourMappableParameterMainState}, because
 * it would be even more unreadable than it is now.
 */
public interface IGenericFourMappableParameterMainState<
        TSelf extends IMainState<TSelf>,
        TParam1,
        TParam2,
        TParam3,
        TParam4,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IMainState<TSelf>,
        IFourOrMoreMappableParameterNoneChosenChoiceState,
        ILateTargetDefinitionTerminalState<
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult> {

  @Override
  IFourMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFirst();

  @Override
  IFourMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickSecond();

  @Override
  IFourMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickThird();

  @Override
  IFourMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam4,
          TParam4,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFourth();
}
