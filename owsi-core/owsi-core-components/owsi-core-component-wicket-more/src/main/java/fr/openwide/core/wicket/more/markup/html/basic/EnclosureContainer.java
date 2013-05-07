package fr.openwide.core.wicket.more.markup.html.basic;

public class EnclosureContainer extends AbstractHideableContainer {

	private static final long serialVersionUID = 8163938380844150417L;

	public EnclosureContainer(String id) {
		super(id, new EnclosureBehavior());
	}

}
