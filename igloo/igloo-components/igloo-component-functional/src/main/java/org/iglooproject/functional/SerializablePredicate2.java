package org.iglooproject.functional;

import java.util.Objects;

import org.danekja.java.util.function.serializable.SerializablePredicate;

@FunctionalInterface
public interface SerializablePredicate2<T> extends Predicate2<T>, SerializablePredicate<T> {

	default SerializablePredicate2<T> and(SerializablePredicate2<? super T> other) {
		Objects.requireNonNull(other);
		return t -> test(t) && other.test(t);
	}

	@Override
	default SerializablePredicate2<T> negate() {
		return t -> !test(t);
	}

	default SerializablePredicate2<T> or(SerializablePredicate2<? super T> other) {
		Objects.requireNonNull(other);
		return t -> test(t) || other.test(t);
	}

	static <T> SerializablePredicate2<T> isEqual(Object targetRef) {
		return (null == targetRef) ? Objects::isNull : object -> targetRef.equals(object);
	}

}
