package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

public interface IParameterMapperTwoChosenParameterMappingState<TInitialState, TChosenParam1, TChosenParam2>
		extends IParameterMapperChosenParameterMappingState
				<
				TInitialState,
				Pair<IModel<TChosenParam1>, IModel<TChosenParam2>>
				> {

}