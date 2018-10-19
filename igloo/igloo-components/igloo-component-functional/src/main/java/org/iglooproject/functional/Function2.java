package org.iglooproject.functional;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Function2<T, R> extends Function<T, R>, com.google.common.base.Function<T, R> {

	default <V> Function2<V, R> compose(Function2<? super V, ? extends T> before) {
		Objects.requireNonNull(before);
		return v -> apply(before.apply(v));
	}

	default <V> Function2<T, V> andThen(Function2<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return t -> after.apply(apply(t));
	}

	static <T> Function2<T, T> identity() {
		return t -> t;
	}

}
