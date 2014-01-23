package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.ILinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.LinkParametersMapping;
import fr.openwide.core.wicket.more.link.descriptor.parameter.mapping.SimpleLinkParameterMappingEntry;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.ILinkParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidators;

public class CoreLinkDescriptorBuilderParametersStateImpl<L extends ILinkDescriptor>
		implements IParameterMappingState<L>, IAddedParameterMappingState<L> {
	
	private final CoreLinkDescriptorBuilderFactory<L> factory;
	private final Collection<ILinkParameterMappingEntry> parameterMappingEntries;
	private final Collection<ILinkParameterValidator> parameterValidators;
	
	private ILinkParameterMappingEntry lastAddedParameterMappingEntry;
	
	public CoreLinkDescriptorBuilderParametersStateImpl(CoreLinkDescriptorBuilderFactory<L> factory) {
		this.factory = factory;
		this.parameterMappingEntries = Lists.newArrayList();
		this.parameterValidators = Lists.newArrayList();
	}

	@Override
	public <T> IAddedParameterMappingState<L> map(String name, IModel<T> valueModel, Class<T> valueType) {
		Args.notNull(name, "name");
		Args.notNull(valueModel, "valueModel");
		Args.notNull(valueType, "valueType");

		return map(new SimpleLinkParameterMappingEntry<T>(name, valueModel, valueType));
	}
	
	@Override
	public IAddedParameterMappingState<L> map(ILinkParameterMappingEntry parameterMappingEntry) {
		Args.notNull(parameterMappingEntry, "parameterMappingEntry");
		
		parameterMappingEntries.add(parameterMappingEntry);
		lastAddedParameterMappingEntry = parameterMappingEntry;
		
		return this;
	}
	
	@Override
	public IParameterMappingState<L> optional() {
		return this;
	}
	
	@Override
	public IParameterMappingState<L> mandatory() {
		parameterValidators.add(lastAddedParameterMappingEntry.mandatoryValidator());
		return this;
	}

	@Override
	public IParameterMappingState<L> validator(ILinkParameterValidator validator) {
		Args.notNull(validator, "validator");
		parameterValidators.add(validator);
		return this;
	}
	
	@Override
	public final L build() {
		LinkParametersMapping parametersMapping = new LinkParametersMapping(parameterMappingEntries);
		ILinkParameterValidator validator = LinkParameterValidators.chain(parameterValidators);
		return factory.create(parametersMapping, validator);
	}

}
