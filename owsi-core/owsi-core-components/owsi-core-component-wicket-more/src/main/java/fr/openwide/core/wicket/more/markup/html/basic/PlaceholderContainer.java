package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.PlaceholderEnclosureVisibilityBuilder.Visibility;

public class PlaceholderContainer extends AbstractHideableContainer<PlaceholderContainer> {

	private static final long serialVersionUID = 1664956501257659431L;

	public PlaceholderContainer(String id) {
		super(id, Visibility.SHOW_IF_EMPTY);
	}
	
	@Override
	protected PlaceholderContainer thisAsT() {
		return this;
	}

}
