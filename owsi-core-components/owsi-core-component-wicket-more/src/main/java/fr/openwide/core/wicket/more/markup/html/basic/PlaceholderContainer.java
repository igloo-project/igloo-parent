package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyContainer;

public class PlaceholderContainer extends AbstractConfigurableComponentBooleanPropertyContainer<PlaceholderContainer> {

	private static final long serialVersionUID = 1664956501257659431L;

	public PlaceholderContainer(String id) {
		this(id, ComponentBooleanProperty.VISIBLE);
	}

	public PlaceholderContainer(String id, ComponentBooleanProperty property) {
		super(id, new PlaceholderBehavior(property));
	}
	
	@Override
	protected PlaceholderContainer thisAsT() {
		return this;
	}

}
