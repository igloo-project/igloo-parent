package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.PlaceholderEnclosureVisibilityBuilder.Visibility;

public class EnclosureContainer extends AbstractHideableContainer<EnclosureContainer> {

	private static final long serialVersionUID = 8163938380844150417L;

	public EnclosureContainer(String id) {
		super(id, Visibility.HIDE_IF_EMPTY);
	}
	
	@Override
	protected EnclosureContainer thisAsT() {
		return this;
	}

}
