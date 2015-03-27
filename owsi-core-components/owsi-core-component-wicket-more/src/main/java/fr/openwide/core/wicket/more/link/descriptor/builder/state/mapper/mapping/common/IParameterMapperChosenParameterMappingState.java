package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;

public interface IParameterMapperChosenParameterMappingState<InitialState, TupleType extends Tuple> {

	public abstract IAddedParameterMappingState<InitialState> map(ILinkParameterMappingEntryFactory<TupleType> parameterMappingEntryFactory);

}
