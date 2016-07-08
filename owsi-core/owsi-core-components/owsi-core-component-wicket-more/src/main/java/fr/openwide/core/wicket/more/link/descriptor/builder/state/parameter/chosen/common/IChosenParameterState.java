package fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.javatuples.Tuple;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.validator.IValidatorState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IDetachableFactory;

public interface IChosenParameterState
		<
		TInitialState,
		TTuple extends Tuple,
		TLateTargetDefinitionPageResult,
		TLateTargetDefinitionResourceResult,
		TLateTargetDefinitionImageResourceResult
		> {

	/**
	 * Maps the chosen <em>model</em> parameters to HTTP query parameters using the given factory.
	 */
	IAddedParameterMappingState<TInitialState> map(
			ILinkParameterMappingEntryFactory<? super TTuple> parameterMappingEntryFactory);

	/**
	 * A version of {@link IValidatorState#validator(Condition)} with a parameter-dependent validator.
	 * <p>This method has the advantage over {@link #validator(IDetachableFactory)} that it allows for validation
	 * of raw parameters, not only model parameters.
	 */
	TInitialState validator(ILinkParameterValidatorFactory<? super TTuple> parameterValidatorFactory);

	/**
	 * A version of {@link IValidatorState#validator(Condition)} with a parameter-dependent condition.
	 */
	TInitialState validator(IDetachableFactory<? super TTuple, ? extends Condition> conditionFactory);
	
	/**
	 * A version of {@link ILateTargetDefinitionTerminalState#page(Class)} with a parameter-dependent target.
	 */
	TLateTargetDefinitionPageResult page(
			IDetachableFactory<? super TTuple, ? extends IModel<? extends Class<? extends Page>>> pageClassFactory);

	/**
	 * A version of {@link ILateTargetDefinitionTerminalState#resource(ResourceReference)} with a
	 * parameter-dependent target.
	 */
	TLateTargetDefinitionResourceResult resource(
			IDetachableFactory<? super TTuple, ? extends IModel<? extends ResourceReference>> resourceReferenceFactory);

	/**
	 * A version of {@link ILateTargetDefinitionTerminalState#imageResource(ResourceReference)} with a
	 * parameter-dependent target.
	 */
	TLateTargetDefinitionImageResourceResult imageResource(
			IDetachableFactory<? super TTuple, ? extends IModel<? extends ResourceReference>> resourceReferenceFactory);

}
