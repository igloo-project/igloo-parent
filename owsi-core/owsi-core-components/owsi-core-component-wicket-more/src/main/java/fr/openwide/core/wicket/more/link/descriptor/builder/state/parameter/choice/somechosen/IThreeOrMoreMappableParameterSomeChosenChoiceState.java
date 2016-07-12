package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.choice.somechosen;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;

public interface IThreeOrMoreMappableParameterSomeChosenChoiceState extends ITwoOrMoreMappableParameterSomeChosenChoiceState {
	
	/**
	 * Pick the third-declared mappable parameter and add it to the already-chosen parameters.
	 * @return Same as {@link #pickFirst()}, but for the third-declared parameter instead of the first.
	 */
	IChosenParameterState<?, ?, ?, ?, ?> andThird();

}
