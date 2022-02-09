package org.iglooproject.wicket.more.markup.html.action;

import java.util.Objects;

import org.iglooproject.wicket.api.action.IAction;
import org.iglooproject.wicket.api.action.IOneParameterAction;

public class Actions {

	public static <T> IOneParameterAction<T> ignoreParameter(final IAction action) {
		Objects.requireNonNull(action);
		return new IOneParameterAction<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void detach() {
				action.detach();
			}
			@Override
			public void execute(T parameter) {
				action.execute();
			}
		};
	}

	private Actions() {
	}

}
