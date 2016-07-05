package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperFourChosenParameterMappingState;


public interface IFourParameterMapperFourChosenParameterMappingState
		<
		InitialState,
		TParam1, TParam2, TParam3, TParam4,
		TChosenParam1, TChosenParam2, TChosenParam3, TChosenParam4
		>
		extends IParameterMapperFourChosenParameterMappingState
				<
				InitialState,
				TChosenParam1, TChosenParam2, TChosenParam3, TChosenParam4
				> {

}
