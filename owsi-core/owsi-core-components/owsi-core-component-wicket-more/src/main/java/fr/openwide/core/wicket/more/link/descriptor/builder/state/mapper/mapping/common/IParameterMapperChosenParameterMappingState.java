package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.common;

import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface IParameterMapperChosenParameterMappingState<TInitialState, TTuple extends Tuple> {

	IAddedParameterMappingState<TInitialState> map(ILinkParameterMappingEntryFactory<? super TTuple> parameterMappingEntryFactory);

	TInitialState validator(ILinkParameterValidatorFactory<? super TTuple> parameterValidatorFactory);
	
	TInitialState validator(IDetachableFactory<? super TTuple, ? extends Condition> conditionFactory);

}
