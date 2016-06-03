package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Quartet;

public interface IParameterMapperFourChosenParameterMappingState<InitialState, T1, T2, T3, T4>
		extends IParameterMapperChosenParameterMappingState<
				InitialState,
				Quartet<IModel<T1>, IModel<T2>, IModel<T3>, IModel<T4>>
		> {

}