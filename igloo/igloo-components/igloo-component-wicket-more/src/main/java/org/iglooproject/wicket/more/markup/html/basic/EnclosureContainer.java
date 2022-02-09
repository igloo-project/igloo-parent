package org.iglooproject.wicket.more.markup.html.basic;

import org.iglooproject.wicket.api.condition.AbstractConfigurableComponentBooleanPropertyContainer;
import org.iglooproject.wicket.api.condition.ComponentBooleanProperty;
import org.iglooproject.wicket.api.condition.ComponentBooleanPropertyBehavior;
import org.iglooproject.wicket.api.condition.AbstractConfigurableComponentBooleanPropertyBehavior.Operator;

public class EnclosureContainer extends AbstractConfigurableComponentBooleanPropertyContainer<EnclosureContainer> {

	private static final long serialVersionUID = 8163938380844150417L;

	public EnclosureContainer(String id) {
		this(id, ComponentBooleanProperty.VISIBLE);
	}

	public EnclosureContainer(String id, ComponentBooleanProperty property) {
		super(id, new ComponentBooleanPropertyBehavior(property, Operator.WHEN_ANY_TRUE));
	}
	
	@Override
	protected EnclosureContainer thisAsT() {
		return this;
	}

}
