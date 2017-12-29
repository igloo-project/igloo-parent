package org.iglooproject.wicket.more.markup.html.action;

import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;

import org.iglooproject.wicket.more.condition.Condition;

public abstract class AbstractOneParameterAjaxAction<T> implements IOneParameterAjaxAction<T> {

	private static final long serialVersionUID = 3044339655373492663L;

	@Override
	public void updateAjaxAttributes(AjaxRequestAttributes attributes, T model) {
		// nothing to do
	}

	@Override
	public void detach() {
		// nothing to do
	}

	@Override
	public Condition getActionAvailableCondition(T parameter) {
		return Condition.alwaysTrue();
	}

}
