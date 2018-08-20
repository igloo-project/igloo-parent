package org.iglooproject.wicket.more.markup.html.basic;

import org.iglooproject.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyBehavior;

public class ComponentBooleanPropertyBehavior extends AbstractConfigurableComponentBooleanPropertyBehavior<ComponentBooleanPropertyBehavior> {

	private static final long serialVersionUID = -4703385998400963974L;

	public ComponentBooleanPropertyBehavior(ComponentBooleanProperty property, Operator operator) {
		super(property, operator);
	}

	@Override
	public ComponentBooleanPropertyBehavior thisAsT() {
		return this;
	}

}
