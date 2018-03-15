package org.iglooproject.functional;

import java.util.Objects;

import org.danekja.java.util.function.serializable.SerializableFunction;

@FunctionalInterface
public interface SerializableFunction2<T, R> extends Function2<T, R>, SerializableFunction<T, R> {

	default <V> SerializableFunction2<V, R> compose(SerializableFunction2<? super V, ? extends T> before) {
		Objects.requireNonNull(before);
		return (V v) -> apply(before.apply(v));
	}

	default <V> SerializableFunction2<T, V> andThen(SerializableFunction2<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return (T t) -> after.apply(apply(t));
	}

	static <T> SerializableFunction2<T, T> identity() {
		return t -> t;
	}

}
