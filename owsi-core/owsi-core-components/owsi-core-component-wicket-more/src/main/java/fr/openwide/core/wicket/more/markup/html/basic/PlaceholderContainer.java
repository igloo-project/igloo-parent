package fr.openwide.core.wicket.more.markup.html.basic;

public class PlaceholderContainer extends AbstractHideableContainer {

	private static final long serialVersionUID = 1664956501257659431L;

	public PlaceholderContainer(String id) {
		super(id, new PlaceholderBehavior());
	}

}
