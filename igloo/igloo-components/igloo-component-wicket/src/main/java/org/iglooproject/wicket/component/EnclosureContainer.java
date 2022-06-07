package org.iglooproject.wicket.component;

import org.iglooproject.wicket.condition.AbstractConfigurableComponentBooleanPropertyBehavior.Operator;
import org.iglooproject.wicket.condition.AbstractConfigurableComponentBooleanPropertyContainer;
import org.iglooproject.wicket.condition.ComponentBooleanProperty;
import org.iglooproject.wicket.condition.ComponentBooleanPropertyBehavior;

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
