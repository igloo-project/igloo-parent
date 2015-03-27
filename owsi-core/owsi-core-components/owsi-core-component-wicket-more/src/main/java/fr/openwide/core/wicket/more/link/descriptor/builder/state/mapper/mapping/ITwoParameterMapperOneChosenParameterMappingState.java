package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;


public interface ITwoParameterMapperOneChosenParameterMappingState<InitialState, T1, T2, MT1> extends IParameterMapperOneChosenParameterMappingState<InitialState, MT1> {

	ITwoParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, MT1, T1> andFirst();

	ITwoParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2, MT1, T2> andSecond();

}
