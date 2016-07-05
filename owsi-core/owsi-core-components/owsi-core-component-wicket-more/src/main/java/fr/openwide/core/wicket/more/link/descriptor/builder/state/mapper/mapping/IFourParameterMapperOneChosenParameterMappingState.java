package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;


public interface IFourParameterMapperOneChosenParameterMappingState
		<
		TInitialState,
		TParam1, TParam2, TParam3, TParam4,
		TChosenParam1
		>
		extends IParameterMapperOneChosenParameterMappingState<TInitialState, TChosenParam1> {

	IFourParameterMapperTwoChosenParameterMappingState<TInitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TParam1> andFirst();

	IFourParameterMapperTwoChosenParameterMappingState<TInitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TParam2> andSecond();

	IFourParameterMapperTwoChosenParameterMappingState<TInitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TParam3> andThird();

	IFourParameterMapperTwoChosenParameterMappingState<TInitialState, TParam1, TParam2, TParam3, TParam4, TChosenParam1, TParam4> andFourth();

}
