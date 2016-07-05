package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperThreeChosenParameterMappingState;


public interface IFourParameterMapperThreeChosenParameterMappingState
		<
		InitialState,
		TParam1, TParam2, TParam3, TParam4,
		TChosenParam1, TChosenParam2, TChosenParam3
		>
		extends IParameterMapperThreeChosenParameterMappingState
				<
				InitialState,
				TChosenParam1, TChosenParam2, TChosenParam3
				> {

	IFourParameterMapperFourChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TChosenParam3, TParam1
			> andFirst();

	IFourParameterMapperFourChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TChosenParam3, TParam2
			> andSecond();

	IFourParameterMapperFourChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TChosenParam3, TParam3
			> andThird();

	IFourParameterMapperFourChosenParameterMappingState<
			InitialState,
			TParam1, TParam2, TParam3, TParam4,
			TChosenParam1, TChosenParam2, TChosenParam3, TParam4
			> andFourth();

}
