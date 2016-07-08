package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.ITwoChosenParameterState;

public interface IFourMappableParameterTwoChosenParameterState
		<
		TInitialState,
		TParam1, TParam2, TParam3, TParam4,
		TChosenParam1, TChosenParam2,
		TLateTargetDefinitionPageResult,
		TLateTargetDefinitionResourceResult,
		TLateTargetDefinitionImageResourceResult
		>
		extends ITwoChosenParameterState
						<
						TInitialState,
						TChosenParam1, TChosenParam2,
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
						> {

	IFourMappableParameterThreeChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TParam1,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andFirst();

	IFourMappableParameterThreeChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TParam2,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andSecond();

	IFourMappableParameterThreeChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TParam3,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andThird();

	IFourMappableParameterThreeChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TParam4,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andFourth();

}
