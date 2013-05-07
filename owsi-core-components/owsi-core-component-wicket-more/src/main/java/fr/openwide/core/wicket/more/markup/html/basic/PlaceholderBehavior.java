package fr.openwide.core.wicket.more.markup.html.basic;

public class PlaceholderBehavior extends AbstractHideableBehavior<PlaceholderBehavior> {

	private static final long serialVersionUID = -4321921413728629980L;

	public PlaceholderBehavior() {
		super(Visibility.SHOW_IF_EMPTY);
	}

	@Override
	protected PlaceholderBehavior thisAsT() {
		return this;
	}
}
