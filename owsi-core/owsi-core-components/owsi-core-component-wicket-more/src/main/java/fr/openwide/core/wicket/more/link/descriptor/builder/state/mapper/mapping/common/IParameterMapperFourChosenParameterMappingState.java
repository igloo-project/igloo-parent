package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;

public interface IParameterMapperFourChosenParameterMappingState<
		TInitialState,
		TChosenParam1, TChosenParam2, TChosenParam3, TChosenParam4
		>
		extends IParameterMapperChosenParameterMappingState
				<
				TInitialState,
				Quartet<IModel<TChosenParam1>, IModel<TChosenParam2>, IModel<TChosenParam3>, IModel<TChosenParam4>>
				> {

}