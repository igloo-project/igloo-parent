package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface IParameterMapperChosenParameterMappingState<InitialState, TupleType extends Tuple> {

	IAddedParameterMappingState<InitialState> map(ILinkParameterMappingEntryFactory<? super TupleType> parameterMappingEntryFactory);

	InitialState validator(ILinkParameterValidatorFactory<? super TupleType> parameterValidatorFactory);
	
	InitialState validator(IDetachableFactory<? super TupleType, ? extends Condition> conditionFactory);

}
