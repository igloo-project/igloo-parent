package org.iglooproject.wicket.action;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.model.IDetachable;
import org.iglooproject.wicket.condition.Condition;

@FunctionalInterface
public interface IAjaxAction extends IDetachable {

	void execute(AjaxRequestTarget target);

	default void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		// nothing to do
	}

	default Condition getActionAvailableCondition() {
		return Condition.alwaysTrue();
	}

	@Override
	default void detach() {
		// nothing to do
	}

}
