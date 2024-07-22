package org.iglooproject.wicket.more.link.descriptor.builder.state.main.generic;

import org.iglooproject.wicket.more.link.descriptor.builder.state.main.ITwoMappableParameterMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.ITwoOrMoreMappableParameterNoneChosenChoiceState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.ITwoMappableParameterOneChosenParameterState;
import org.iglooproject.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the
 * generic type parameters are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link
 * TLateTargetDefinitionResourceResult}, etc.) in {@link ITwoMappableParameterMainState}, because it
 * would be even more unreadable than it is now.
 */
public interface IGenericTwoMappableParameterMainState<
        TSelf extends IMainState<TSelf>,
        TParam1,
        TParam2,
        TLateTargetDefinitionPageResult,
        TLateTargetDefinitionResourceResult,
        TLateTargetDefinitionImageResourceResult>
    extends IMainState<TSelf>,
        ITwoOrMoreMappableParameterNoneChosenChoiceState,
        ILateTargetDefinitionTerminalState<
            TLateTargetDefinitionPageResult,
            TLateTargetDefinitionResourceResult,
            TLateTargetDefinitionImageResourceResult> {

  @Override
  ITwoMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam1,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickFirst();

  @Override
  ITwoMappableParameterOneChosenParameterState<
          TSelf,
          TParam1,
          TParam2,
          TParam2,
          TLateTargetDefinitionPageResult,
          TLateTargetDefinitionResourceResult,
          TLateTargetDefinitionImageResourceResult>
      pickSecond();
}
