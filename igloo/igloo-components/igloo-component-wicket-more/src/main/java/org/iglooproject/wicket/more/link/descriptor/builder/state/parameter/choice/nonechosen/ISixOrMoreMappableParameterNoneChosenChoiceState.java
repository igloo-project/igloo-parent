package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface ISixOrMoreMappableParameterNoneChosenChoiceState
    extends IFiveOrMoreMappableParameterNoneChosenChoiceState {

  /**
   * Pick the sixth-declared mappable parameter.
   *
   * @return Same as {@link #pickFirst()}, but for the sixth-declared parameter instead of the
   *     first.
   */
  IOneChosenParameterState<?, ?, ?, ?, ?> pickSixth();
}
