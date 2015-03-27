package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;


public interface IThreeParameterMapperOneChosenParameterMappingState<InitialState, T1, T2, T3, MT1> extends IParameterMapperOneChosenParameterMappingState<InitialState, MT1> {
	
	IThreeParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, MT1, T1> andFirst();

	IThreeParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, MT1, T2> andSecond();

	IThreeParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, T3, MT1, T3> andThird();

}
