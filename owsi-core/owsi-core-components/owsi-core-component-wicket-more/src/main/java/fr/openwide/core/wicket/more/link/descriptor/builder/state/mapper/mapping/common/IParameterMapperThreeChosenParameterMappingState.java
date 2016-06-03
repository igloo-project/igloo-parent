package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Triplet;

public interface IParameterMapperThreeChosenParameterMappingState<InitialState, T1, T2, T3>
		extends IParameterMapperChosenParameterMappingState<
				InitialState,
				Triplet<IModel<T1>, IModel<T2>, IModel<T3>>
		> {

}