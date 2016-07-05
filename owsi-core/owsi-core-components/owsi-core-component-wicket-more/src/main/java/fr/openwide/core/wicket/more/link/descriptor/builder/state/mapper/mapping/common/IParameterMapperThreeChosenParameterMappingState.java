package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Triplet;

public interface IParameterMapperThreeChosenParameterMappingState
		<
		TInitialState,
		TChosenParam1, TChosenParam2, TChosenParam3
		>
		extends IParameterMapperChosenParameterMappingState
				<
				TInitialState,
				Triplet<IModel<TChosenParam1>, IModel<TChosenParam2>, IModel<TChosenParam3>>
				> {

}