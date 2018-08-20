package org.iglooproject.wicket.more.markup.html.basic;

import org.iglooproject.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyBehavior.Operator;
import org.iglooproject.wicket.more.markup.html.basic.impl.AbstractConfigurableComponentBooleanPropertyContainer;

public class PlaceholderContainer extends AbstractConfigurableComponentBooleanPropertyContainer<PlaceholderContainer> {

	private static final long serialVersionUID = 1664956501257659431L;

	public PlaceholderContainer(String id) {
		this(id, ComponentBooleanProperty.VISIBLE);
	}

	public PlaceholderContainer(String id, ComponentBooleanProperty property) {
		super(id, new ComponentBooleanPropertyBehavior(property, Operator.WHEN_ALL_FALSE));
	}
	
	@Override
	protected PlaceholderContainer thisAsT() {
		return this;
	}

}
