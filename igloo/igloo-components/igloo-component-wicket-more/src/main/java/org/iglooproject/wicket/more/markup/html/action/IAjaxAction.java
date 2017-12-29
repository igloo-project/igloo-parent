package org.iglooproject.wicket.more.markup.html.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IDetachable;

import org.iglooproject.wicket.more.condition.Condition;

public interface IAjaxAction extends IDetachable {

	void execute(AjaxRequestTarget target);

	void updateAjaxAttributes(AjaxRequestAttributes attributes);

	Condition getActionAvailableCondition();

}
