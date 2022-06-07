package org.iglooproject.wicket.component;

import org.iglooproject.wicket.condition.AbstractConfigurableComponentBooleanPropertyBehavior.Operator;
import org.iglooproject.wicket.condition.AbstractConfigurableComponentBooleanPropertyContainer;
import org.iglooproject.wicket.condition.ComponentBooleanProperty;
import org.iglooproject.wicket.condition.ComponentBooleanPropertyBehavior;

public class PlaceholderContainer extends AbstractConfigurableComponentBooleanPropertyContainer<PlaceholderContainer> {

	private static final long serialVersionUID = 1664956501257659431L;

	public PlaceholderContainer(String id) {
		this(id, ComponentBooleanProperty.VISIBLE);
	}

	public PlaceholderContainer(String id, ComponentBooleanProperty property) {
		super(id, new ComponentBooleanPropertyBehavior(property, Operator.WHEN_ALL_FALSE));
	}
	
	@Override
	protected PlaceholderContainer thisAsT() {
		return this;
	}

}
