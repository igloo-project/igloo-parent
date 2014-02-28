package fr.openwide.core.wicket.more.markup.html.basic;

import fr.openwide.core.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyContainer;

public class EnclosureContainer extends AbstractConfigurableComponentBooleanPropertyContainer<EnclosureContainer> {

	private static final long serialVersionUID = 8163938380844150417L;

	public EnclosureContainer(String id) {
		super(id, new EnclosureBehavior(ComponentBooleanProperty.VISIBLE));
	}
	
	@Override
	protected EnclosureContainer thisAsT() {
		return this;
	}

}
