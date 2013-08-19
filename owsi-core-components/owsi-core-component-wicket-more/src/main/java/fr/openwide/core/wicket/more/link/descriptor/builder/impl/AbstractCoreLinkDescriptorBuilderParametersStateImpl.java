package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import java.io.Serializable;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

import com.google.common.collect.Maps;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParametersState;
import fr.openwide.core.wicket.more.link.descriptor.impl.PageParametersModel;

public abstract class AbstractCoreLinkDescriptorBuilderParametersStateImpl<T extends ILinkDescriptor>
		implements IParametersState<T> {
	
	private final Map<String, IModel<?>> parameterModelMap;
	
	public AbstractCoreLinkDescriptorBuilderParametersStateImpl() {
		this.parameterModelMap = Maps.newLinkedHashMap();
	}

	@Override
	public IParametersState<T> parameter(String name, IModel<?> valueModel) {
		Args.notNull(name, "name");
		Args.notNull(valueModel, "valueModel");
		
		parameterModelMap.put(name, valueModel);
		
		return this;
	}

	@Override
	public IParametersState<T> parameter(String name, Serializable value) {
		return parameter(name, Model.of(value));
	}
	
	@Override
	public final T build() {
		return build(new PageParametersModel(parameterModelMap));
	}

	protected abstract T build(PageParametersModel parametersModel);

}
