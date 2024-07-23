package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;

public interface IFourOrMoreMappableParameterNoneChosenChoiceState
    extends IThreeOrMoreMappableParameterNoneChosenChoiceState {

  /**
   * Pick the fourth-declared mappable parameter.
   *
   * @return Same as {@link #pickFirst()}, but for the fourth-declared parameter instead of the
   *     first.
   */
  IOneChosenParameterState<?, ?, ?, ?, ?> pickFourth();
}
