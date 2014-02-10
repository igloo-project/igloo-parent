package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractPlaceholderEnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.impl.PlaceholderEnclosureVisibilityBuilder.Visibility;

public class PlaceholderBehavior extends AbstractPlaceholderEnclosureBehavior<PlaceholderBehavior> {

	private static final long serialVersionUID = -4321921413728629980L;

	public PlaceholderBehavior() {
		this(ComponentBooleanProperty.VISIBILITY_ALLOWED);
	}
	
	public PlaceholderBehavior(ComponentBooleanProperty property) {
		super(property, Visibility.SHOW_IF_EMPTY);
	}

	@Override
	protected PlaceholderBehavior thisAsT() {
		return this;
	}
}
