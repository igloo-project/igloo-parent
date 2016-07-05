package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperTwoChosenParameterMappingState;



public interface IFourParameterMapperTwoChosenParameterMappingState
		<
		InitialState,
		TParam1, TParam2, TParam3, TParam4,
		TChosenParam1, TChosenParam2
		>
		extends IParameterMapperTwoChosenParameterMappingState
				<
				InitialState,
				TChosenParam1, TChosenParam2
				> {

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TChosenParam2, TParam1> andFirst();

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TChosenParam2, TParam2> andSecond();

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TChosenParam2, TParam3> andThird();

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TChosenParam2, TParam4> andFourth();

}
