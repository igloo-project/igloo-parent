package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.util.model.Detachables;

public final class OneParameterModelFactory {

	private OneParameterModelFactory() {
	}

	public static final <T, U> IOneParameterModelFactory<T, U> identity(final IModel<U> model) {
		return new AbstractOneParameterModelFactory<T, U>() {
			private static final long serialVersionUID = 1L;
			@Override
			public IModel<U> create(T parameter) {
				return model;
			}
			@Override
			public void detach() {
				Detachables.detach(model);
			}
		};
	}

}
