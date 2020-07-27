package org.iglooproject.wicket.more.markup.html.action;

import org.apache.wicket.model.IDetachable;

@FunctionalInterface
public interface IAction extends IDetachable {

	void execute();

	@Override
	default void detach() {
		// nothing to do
	}

}
