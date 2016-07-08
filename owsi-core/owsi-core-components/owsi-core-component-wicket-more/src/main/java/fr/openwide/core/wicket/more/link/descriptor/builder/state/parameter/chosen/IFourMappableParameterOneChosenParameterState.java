package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;


public interface IFourMappableParameterOneChosenParameterState
		<
		TInitialState,
		TParam1, TParam2, TParam3, TParam4,
		TChosenParam1,
		TLateTargetDefinitionPageResult,
		TLateTargetDefinitionResourceResult,
		TLateTargetDefinitionImageResourceResult
		>
		extends IOneChosenParameterState
						<
						TInitialState,
						TChosenParam1,
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
						> {

	IFourMappableParameterTwoChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TParam1,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andFirst();

	IFourMappableParameterTwoChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TParam2,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andSecond();

	IFourMappableParameterTwoChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TParam3,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andThird();

	IFourMappableParameterTwoChosenParameterState<
			TInitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TParam4,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> andFourth();

}
