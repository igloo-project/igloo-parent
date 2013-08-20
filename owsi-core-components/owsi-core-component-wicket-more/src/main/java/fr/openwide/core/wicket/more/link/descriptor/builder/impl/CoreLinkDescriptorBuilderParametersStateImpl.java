package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IAddedParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParametersState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.ITerminalState;
import fr.openwide.core.wicket.more.link.descriptor.impl.PageParametersModel;
import fr.openwide.core.wicket.more.link.descriptor.validator.IParameterValidator;
import fr.openwide.core.wicket.more.link.descriptor.validator.ParameterValidators;

public class CoreLinkDescriptorBuilderParametersStateImpl<T extends ILinkDescriptor>
		implements IParametersState<T>, IAddedParameterState<T> {
	
	private final CoreLinkDescriptorBuilderFactory<T> factory;
	private final Map<String, IModel<?>> parameterModelMap;
	private final Collection<IParameterValidator> parameterValidators;
	
	private String lastAddedParameterName;
	
	public CoreLinkDescriptorBuilderParametersStateImpl(CoreLinkDescriptorBuilderFactory<T> factory) {
		this.factory = factory;
		this.parameterModelMap = Maps.newLinkedHashMap();
		this.parameterValidators = Lists.newArrayList();
	}

	@Override
	public IAddedParameterState<T> parameter(String name, IModel<?> valueModel) {
		Args.notNull(name, "name");
		Args.notNull(valueModel, "valueModel");
		
		parameterModelMap.put(name, valueModel);
		lastAddedParameterName = name;
		
		return this;
	}

	@Override
	public IAddedParameterState<T> parameter(String name, Serializable value) {
		return parameter(name, Model.of(value));
	}
	
	@Override
	public IParametersState<T> optional() {
		return this;
	}
	
	@Override
	public IParametersState<T> mandatory() {
		parameterValidators.add(new CoreLinkDescriptorBuilderMandatoryParameterValidator(lastAddedParameterName));
		return this;
	}

	@Override
	public ITerminalState<T> validator(IParameterValidator validator) {
		Args.notNull(validator, "validator");
		parameterValidators.add(validator);
		return this;
	}
	
	@Override
	public final T build() {
		PageParametersModel parameters = new PageParametersModel(parameterModelMap);
		IParameterValidator validator = ParameterValidators.chain(parameterValidators);
		return factory.create(parameters, validator);
	}

}
