package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperOneChosenParameterMappingState;


public interface ITwoParameterMapperOneChosenParameterMappingState<InitialState, TParam1, TParam2, TChosenParam1>
		extends IParameterMapperOneChosenParameterMappingState<InitialState, TChosenParam1> {

	ITwoParameterMapperTwoChosenParameterMappingState<InitialState, TParam1, TParam2, TChosenParam1, TParam1>
			andFirst();

	ITwoParameterMapperTwoChosenParameterMappingState<InitialState, TParam1, TParam2, TChosenParam1, TParam2>
			andSecond();

}
