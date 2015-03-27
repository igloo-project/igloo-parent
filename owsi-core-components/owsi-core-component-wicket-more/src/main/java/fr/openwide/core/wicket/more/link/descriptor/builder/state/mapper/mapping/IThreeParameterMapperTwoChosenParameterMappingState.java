package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperTwoChosenParameterMappingState;


public interface IThreeParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, MT1, MT2>
		extends IParameterMapperTwoChosenParameterMappingState<InitialState, MT1, MT2> {

	IThreeParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, MT1, MT2, T1> andFirst();

	IThreeParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, MT1, MT2, T2> andSecond();

	IThreeParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, MT1, MT2, T3> andThird();

}
