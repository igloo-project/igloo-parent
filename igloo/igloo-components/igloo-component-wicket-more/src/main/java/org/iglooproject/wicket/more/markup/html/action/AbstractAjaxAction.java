package org.iglooproject.wicket.more.markup.html.action;

import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;

import org.iglooproject.wicket.more.condition.Condition;


public abstract class AbstractAjaxAction implements IAjaxAction {

	private static final long serialVersionUID = -8244302682768981697L;

	@Override
	public void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		// nothing to do
	}

	@Override
	public void detach() {
		// nothing to do
	}

	@Override
	public Condition getActionAvailableCondition() {
		return Condition.alwaysTrue();
	}

}
