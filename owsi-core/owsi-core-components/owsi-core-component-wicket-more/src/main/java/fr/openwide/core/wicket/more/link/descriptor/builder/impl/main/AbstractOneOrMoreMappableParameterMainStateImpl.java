package fr.openwide.core.wicket.more.link.descriptor.builder.impl.main;

import java.util.List;

import org.apache.wicket.util.lang.Args;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.wicket.more.link.descriptor.builder.impl.parameter.AbstractChosenParameterStateImpl;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.mapping.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.factory.ILinkParameterMappingEntryFactory;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.factory.ILinkParameterValidatorFactory;

abstract class AbstractOneOrMoreMappableParameterMainStateImpl
		<
		TSelf extends IMainState<TSelf>,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends AbstractMainStateImpl
						<
						TSelf,
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						> {
	
	protected final List<Class<?>> dynamicParameterTypes;
	
	public AbstractOneOrMoreMappableParameterMainStateImpl(
			NoMappableParameterMainStateImpl<
					TEarlyTargetDefinitionLinkDescriptor,
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> previousState,
			Class<?> addedParameterType) {
		super(previousState);
		this.dynamicParameterTypes = ImmutableList.<Class<?>>of(addedParameterType);
	}
	
	public AbstractOneOrMoreMappableParameterMainStateImpl(
			AbstractOneOrMoreMappableParameterMainStateImpl<
					?,
					TEarlyTargetDefinitionLinkDescriptor,
					TLateTargetDefinitionPageLinkDescriptor,
					TLateTargetDefinitionResourceLinkDescriptor,
					TLateTargetDefinitionImageResourceLinkDescriptor
					> previousState,
			Class<?> addedParameterType, int expectedNumberOfParameters) {
		super(previousState);
		this.dynamicParameterTypes = ImmutableList.<Class<?>>builder()
				.addAll(previousState.dynamicParameterTypes).add(addedParameterType).build();
		Args.withinRange(
				expectedNumberOfParameters, expectedNumberOfParameters,
				dynamicParameterTypes.size(), "dynamicParameterTypes.size()"
		);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	protected abstract class AbstractInternalChosenParameterStateImpl<TSelfChosen>
			extends AbstractChosenParameterStateImpl<TSelfChosen, TSelf> {
		
		@Override
		protected Class<?> getParameterType(int index) {
			return dynamicParameterTypes.get(index);
		}
		
		@Override
		public IAddedParameterMappingState<TSelf> map(ILinkParameterMappingEntryFactory entryFactory) {
			return AbstractOneOrMoreMappableParameterMainStateImpl.this.doMap(entryFactory, getParameterIndices());
		}
		
		@Override
		public TSelf validator(ILinkParameterValidatorFactory factory) {
			return (TSelf) AbstractOneOrMoreMappableParameterMainStateImpl.this.doValidator(factory, getParameterIndices());
		}
	}
}
