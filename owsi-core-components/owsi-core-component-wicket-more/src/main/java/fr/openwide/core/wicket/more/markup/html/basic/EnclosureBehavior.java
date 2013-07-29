package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractPlaceholderEnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.impl.PlaceholderEnclosureVisibilityBuilder.Visibility;

public class EnclosureBehavior extends AbstractPlaceholderEnclosureBehavior<EnclosureBehavior> {

	private static final long serialVersionUID = -589273014057505964L;

	public EnclosureBehavior() {
		super(Visibility.HIDE_IF_EMPTY);
	}

	@Override
	protected EnclosureBehavior thisAsT() {
		return this;
	}

}
