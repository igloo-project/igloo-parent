package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface ITwoOrMoreMappableParameterNoneChosenChoiceState {

  /**
   * Pick the first-declared mappable parameter.
   *
   * @return A {@link IOneChosenParameterState} with the first-declared parameter as the only chosen
   *     parameter, that may optionally allow for picking additional parameters.
   */
  IOneChosenParameterState<?, ?, ?, ?, ?> pickFirst();

  /**
   * Pick the second-declared mappable parameter.
   *
   * @return Same as {@link #pickFirst()}, but for the second-declared parameter instead of the
   *     first.
   */
  IOneChosenParameterState<?, ?, ?, ?, ?> pickSecond();
}
