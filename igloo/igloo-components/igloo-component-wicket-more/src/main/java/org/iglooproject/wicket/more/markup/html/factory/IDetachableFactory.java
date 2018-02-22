package org.iglooproject.wicket.more.markup.html.factory;

import org.apache.wicket.model.IDetachable;

public interface IDetachableFactory<T, R> extends java.util.function.Function<T, R>, com.google.common.base.Function<T, R>, IDetachable {

	R create(T parameter);

	/**
	 * @deprecated Use {@link #create(T)} instead.
	 */
	@Deprecated
	@Override
	default R apply(T parameter) {
		return create(parameter);
	}

	@Override
	default void detach() {
		// nothing to do
	}

}
