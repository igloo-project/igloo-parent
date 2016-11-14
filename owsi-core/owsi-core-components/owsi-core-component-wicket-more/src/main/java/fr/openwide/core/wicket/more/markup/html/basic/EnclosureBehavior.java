package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyBehavior;

@Deprecated
public class EnclosureBehavior extends AbstractConfigurableComponentBooleanPropertyBehavior<EnclosureBehavior> {

	private static final long serialVersionUID = -589273014057505964L;

	public EnclosureBehavior() {
		this(ComponentBooleanProperty.VISIBILITY_ALLOWED);
	}
	
	public EnclosureBehavior(ComponentBooleanProperty property) {
		super(property, Operator.WHEN_ANY_TRUE);
	}

	@Override
	protected EnclosureBehavior thisAsT() {
		return this;
	}

}
