package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface IThreeOrMoreMappableParameterNoneChosenChoiceState
    extends ITwoOrMoreMappableParameterNoneChosenChoiceState {

  /**
   * Pick the third-declared mappable parameter.
   *
   * @return Same as {@link #pickFirst()}, but for the third-declared parameter instead of the
   *     first.
   */
  IOneChosenParameterState<?, ?, ?, ?, ?> pickThird();
}
