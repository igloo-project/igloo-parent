package org.iglooproject.functional;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface Predicate2<T> extends Predicate<T>, com.google.common.base.Predicate<T> {

	@Override
	boolean test(T t);

	@Override
	default boolean apply(T input) {
		return test(input);
	}

	default Predicate2<T> and(Predicate2<? super T> other) {
		Objects.requireNonNull(other);
		return (t) -> test(t) && other.test(t);
	}

	@Override
	default Predicate2<T> negate() {
		return (t) -> !test(t);
	}

	default Predicate2<T> or(Predicate2<? super T> other) {
		Objects.requireNonNull(other);
		return (t) -> test(t) || other.test(t);
	}

	static <T> Predicate2<T> isEqual(Object targetRef) {
		return (null == targetRef) ? Objects::isNull : object -> targetRef.equals(object);
	}

}
