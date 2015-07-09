package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;


public interface IFourParameterMapperOneChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1> extends IParameterMapperOneChosenParameterMappingState<InitialState, MT1> {

	IFourParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, T1> andFirst();

	IFourParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, T2> andSecond();

	IFourParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, T3> andThird();

	IFourParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, T4, MT1, T4> andFourth();

}
