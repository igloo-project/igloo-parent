package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.condition.BooleanOperator;
import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyBehavior;

public class ComponentBooleanPropertyBehavior extends AbstractConfigurableComponentBooleanPropertyBehavior<ComponentBooleanPropertyBehavior> {

	private static final long serialVersionUID = -4703385998400963974L;

	public ComponentBooleanPropertyBehavior(ComponentBooleanProperty property, BooleanOperator operator) {
		super(property, operator);
	}

	/**
	 * @deprecated Use {@link ComponentBooleanPropertyBehavior#ComponentBooleanPropertyBehavior(ComponentBooleanProperty, BooleanOperator)} instead.
	 */
	@Deprecated
	public ComponentBooleanPropertyBehavior(ComponentBooleanProperty property, Operator operator) {
		super(property, operator);
	}

	@Override
	public ComponentBooleanPropertyBehavior thisAsT() {
		return this;
	}

}
