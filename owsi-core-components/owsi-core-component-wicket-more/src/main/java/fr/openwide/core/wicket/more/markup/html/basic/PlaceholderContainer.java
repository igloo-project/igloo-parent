package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyContainer;

public class PlaceholderContainer extends AbstractConfigurableComponentBooleanPropertyContainer<PlaceholderContainer> {

	private static final long serialVersionUID = 1664956501257659431L;

	public PlaceholderContainer(String id) {
		super(id, new PlaceholderBehavior(ComponentBooleanProperty.VISIBLE));
	}
	
	@Override
	protected PlaceholderContainer thisAsT() {
		return this;
	}

}
