package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;

public interface ITwoOrMoreMappableParameterSomeChosenChoiceState {

  /**
   * Pick the first-declared mappable parameter and add it to the already-chosen parameters.
   *
   * @return A {@link IChosenParameterState} with the first-declared parameter as the last-chosen
   *     parameter, that may optionally allow for picking additional parameters.
   */
  IChosenParameterState<?, ?, ?, ?, ?> andFirst();

  /**
   * Pick the second-declared mappable parameter and add it to the already-chosen parameters.
   *
   * @return Same as {@link #pickFirst()}, but for the second-declared parameter instead of the
   *     first.
   */
  IChosenParameterState<?, ?, ?, ?, ?> andSecond();
}
