package org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic;

import org.iglooproject.wicket.more.link.descriptor.builder.state.main.IThreeMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.IThreeOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.IThreeMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the
 * generic type parameters are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link
 * TLateTargetDefinitionResourceResult}, etc.) in {@link IThreeMappableParameterMainState}, because
 * it would be even more unreadable than it is now.
 */
public interface IGenericThreeMappableParameterMainState<
        TSelf extends IMainState<TSelf>,
        TParam1,
        TParam2,
        TParam3,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IMainState<TSelf>,
        IThreeOrMoreMappableParameterNoneChosenChoiceState,
        ILateTargetDefinitionTerminalState<
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult> {

  @Override
  IThreeMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFirst();

  @Override
  IThreeMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickSecond();

  @Override
  IThreeMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam3,
          TParam3,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickThird();
}
