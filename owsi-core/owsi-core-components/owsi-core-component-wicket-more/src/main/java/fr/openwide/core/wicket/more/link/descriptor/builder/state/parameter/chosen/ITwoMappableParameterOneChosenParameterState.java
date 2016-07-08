package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;


public interface ITwoMappableParameterOneChosenParameterState
		<
		InitialState,
		TParam1, TParam2,
		TChosenParam1,
		TLateTargetDefinitionPageResult,
		TLateTargetDefinitionResourceResult,
		TLateTargetDefinitionImageResourceResult
		>
		extends IOneChosenParameterState
						<
						InitialState,
						TChosenParam1,
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
						> {

	ITwoMappableParameterTwoChosenParameterState<
			InitialState,
			TParam1, TParam2,
			TChosenParam1, TParam1,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andFirst();

	ITwoMappableParameterTwoChosenParameterState<
			InitialState,
			TParam1, TParam2,
			TChosenParam1, TParam2,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andSecond();

}
