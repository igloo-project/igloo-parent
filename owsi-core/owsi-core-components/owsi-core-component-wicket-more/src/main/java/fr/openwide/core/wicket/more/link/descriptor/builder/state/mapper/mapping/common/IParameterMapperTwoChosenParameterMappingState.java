package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

public interface IParameterMapperTwoChosenParameterMappingState<InitialState, T1, T2> extends IParameterMapperChosenParameterMappingState<InitialState, Pair<IModel<T1>, IModel<T2>>> {

}