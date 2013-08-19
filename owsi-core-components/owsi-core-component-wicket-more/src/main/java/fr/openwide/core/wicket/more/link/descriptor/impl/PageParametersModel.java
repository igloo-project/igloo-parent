package fr.openwide.core.wicket.more.link.descriptor.impl;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.ImmutableMap;

/**
 * Represents {@link PageParameters} whose values can change over time, especially between two Ajax refreshes.
 * <p>This cannot be implemented directly as a subclass of PageParameters, because DynamicPageParameters references IModels that have to be
 * detached by the client, whereas PageParameters does not.
 */
public class PageParametersModel implements IModel<PageParameters>, IComponentAssignedModel<PageParameters> {
	
	private static final long serialVersionUID = -9066291686294702275L;
	
	private final Map<String, IModel<?>> parameterModelMap;

	public PageParametersModel(Map<String, IModel<?>> parameterModelMap) {
		super();
		this.parameterModelMap = ImmutableMap.copyOf(parameterModelMap);
	}
	
	@Override
	public PageParameters getObject() {
		PageParameters result = new PageParameters();

		for (Map.Entry<String, IModel<?>> parameter : parameterModelMap.entrySet()) {
			Object value = parameter.getValue().getObject();
			if (value != null) {
				result.add(parameter.getKey(), parameter.getValue().getObject());
			}
		}
		
		return result;
	}
	
	@Override
	public void setObject(PageParameters object) {
		throw new UnsupportedOperationException("Model " + getClass() + " does not support setObject(Object)");
	}
	
	@Override
	public IWrapModel<PageParameters> wrapOnAssignment(Component component) {
		return new WrapModel(component);
	}

	@Override
	public void detach() {
		for (Map.Entry<String, IModel<?>> parameter : parameterModelMap.entrySet()) {
			parameter.getValue().detach();
		}
	}
	
	private class WrapModel extends PageParametersModel implements IWrapModel<PageParameters> {
		private static final long serialVersionUID = -1776808095158473219L;

		public WrapModel(Component component) {
			super(wrapParameterModelMap(PageParametersModel.this.parameterModelMap, component));
		}

		@Override
		public IModel<?> getWrappedModel() {
			return PageParametersModel.this;
		}
	}
	
	private static Map<String, IModel<?>> wrapParameterModelMap(Map<String, IModel<?>> parameterModelMap, Component component) {
		ImmutableMap.Builder<String, IModel<?>> builder = ImmutableMap.builder();
		for (Map.Entry<String, IModel<?>> parameter : parameterModelMap.entrySet()) {
			IModel<?> model = parameter.getValue();
			model = wrap(model, component);
			builder.put(parameter.getKey(), model);
		}
		return builder.build();
	}
	
	private static IModel<?> wrap(IModel<?> model, Component component) {
		if (model instanceof IComponentAssignedModel) {
			return ((IComponentAssignedModel<?>)model).wrapOnAssignment(component);
		} else {
			return model;
		}
	}

}
