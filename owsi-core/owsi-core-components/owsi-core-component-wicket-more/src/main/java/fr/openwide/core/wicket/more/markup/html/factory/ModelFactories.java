package fr.openwide.core.wicket.more.markup.html.factory;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.util.model.Detachables;

public final class ModelFactories {

	private ModelFactories() {
	}

	public static final <R extends IModel<?>, T> IDetachableFactory<T, R> constant(final R model) {
		return new AbstractDetachableFactory<T, R>() {
			private static final long serialVersionUID = 1L;
			@Override
			public R create(T parameter) {
				return model;
			}
			@Override
			public void detach() {
				Detachables.detach(model);
			}
		};
	}

}
