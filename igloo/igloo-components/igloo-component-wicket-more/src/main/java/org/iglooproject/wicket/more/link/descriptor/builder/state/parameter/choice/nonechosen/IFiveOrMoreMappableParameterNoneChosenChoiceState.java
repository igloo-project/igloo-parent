package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface IFiveOrMoreMappableParameterNoneChosenChoiceState
    extends IFourOrMoreMappableParameterNoneChosenChoiceState {

  /**
   * Pick the fifth-declared mappable parameter.
   *
   * @return Same as {@link #pickFirst()}, but for the fifth-declared parameter instead of the
   *     first.
   */
  IOneChosenParameterState<?, ?, ?, ?, ?> pickFifth();
}
