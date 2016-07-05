package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common.IParameterMapperThreeChosenParameterMappingState;


public interface IThreeParameterMapperThreeChosenParameterMappingState
		<
		InitialState,
		TParam1, TParam2, TParam3,
		TChosenParam1, TChosenParam2, TChosenParam3
		>
		extends IParameterMapperThreeChosenParameterMappingState
				<
				InitialState,
				TChosenParam1, TChosenParam2, TChosenParam3
				> {

}
