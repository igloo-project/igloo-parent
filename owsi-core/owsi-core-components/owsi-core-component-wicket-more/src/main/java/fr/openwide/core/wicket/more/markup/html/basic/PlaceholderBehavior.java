package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyBehavior;

@Deprecated
public class PlaceholderBehavior extends AbstractConfigurableComponentBooleanPropertyBehavior<PlaceholderBehavior> {

	private static final long serialVersionUID = -4321921413728629980L;

	public PlaceholderBehavior() {
		this(ComponentBooleanProperty.VISIBILITY_ALLOWED);
	}
	
	public PlaceholderBehavior(ComponentBooleanProperty property) {
		super(property, Operator.WHEN_ALL_FALSE);
	}

	@Override
	protected PlaceholderBehavior thisAsT() {
		return this;
	}
}
