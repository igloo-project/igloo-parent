package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperThreeChosenParameterMappingState;


public interface IFourParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, MT3> extends IParameterMapperThreeChosenParameterMappingState<InitialState, MT1, MT2, MT3> {

	IFourParameterMapperFourChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, MT3, T1> andFirst();

	IFourParameterMapperFourChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, MT3, T2> andSecond();

	IFourParameterMapperFourChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, MT3, T3> andThird();

	IFourParameterMapperFourChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, MT2, MT3, T4> andFourth();

}
