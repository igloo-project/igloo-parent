package org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen;

import org.iglooproject.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;

public interface IFourOrMoreMappableParameterSomeChosenChoiceState
    extends IThreeOrMoreMappableParameterSomeChosenChoiceState {

  /**
   * Pick the fourth-declared mappable parameter and add it to the already-chosen parameters.
   *
   * @return Same as {@link #pickFirst()}, but for the fourth-declared parameter instead of the
   *     first.
   */
  IChosenParameterState<?, ?, ?, ?, ?> andFourth();
}
