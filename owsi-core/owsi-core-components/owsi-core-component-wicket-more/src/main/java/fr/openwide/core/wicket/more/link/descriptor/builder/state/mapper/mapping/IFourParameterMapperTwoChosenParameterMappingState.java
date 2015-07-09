package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperTwoChosenParameterMappingState;



public interface IFourParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2> extends IParameterMapperTwoChosenParameterMappingState<InitialState, MT1, MT2> {

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, T1> andFirst();

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, T2> andSecond();

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, T3> andThird();

	IFourParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, T4> andFourth();

}
