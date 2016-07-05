package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperTwoChosenParameterMappingState;


public interface IThreeParameterMapperTwoChosenParameterMappingState
		<
		InitialState,
		TParam1, TParam2, TParam3,
		TChosenParam1, TChosenParam2
		>
		extends IParameterMapperTwoChosenParameterMappingState<InitialState, TChosenParam1, TChosenParam2> {

	IThreeParameterMapperThreeChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3,
			TChosenParam1, TChosenParam2, TParam1
			> andFirst();

	IThreeParameterMapperThreeChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3,
			TChosenParam1, TChosenParam2, TParam2
			> andSecond();

	IThreeParameterMapperThreeChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3,
			TChosenParam1, TChosenParam2, TParam3
			> andThird();

}
