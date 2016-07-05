package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;


public interface IThreeParameterMapperOneChosenParameterMappingState
		<
		InitialState,
		TParam1, TParam2, TParam3,
		TChosenParam1
		>
		extends IParameterMapperOneChosenParameterMappingState<InitialState, TChosenParam1> {
	
	IThreeParameterMapperTwoChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3,
			TChosenParam1, TParam1
			> andFirst();

	IThreeParameterMapperTwoChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3,
			TChosenParam1, TParam2
			> andSecond();

	IThreeParameterMapperTwoChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3,
			TChosenParam1, TParam3
			> andThird();

}
