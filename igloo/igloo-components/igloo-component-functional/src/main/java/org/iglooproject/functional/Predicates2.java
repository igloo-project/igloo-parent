package org.iglooproject.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;

public final class Predicates2 {

	public static <T> SerializablePredicate2<T> from(com.google.common.base.Predicate<T> predicate) {
		Objects.requireNonNull(predicate);
		return predicate::test;
	}

	public static <T> SerializablePredicate2<T> alwaysTrue() {
		return t -> true;
	}

	public static <T> SerializablePredicate2<T> alwaysFalse() {
		return t -> false;
	}

	public static <T> SerializablePredicate2<T> isNull() {
		return t -> t == null;
	}

	public static <T> SerializablePredicate2<T> notNull() {
		return t -> t != null;
	}

	public static <T> SerializablePredicate2<T> not(Predicate<T> predicate) {
		Objects.requireNonNull(predicate);
		return t -> predicate.negate().test(t);
	}

	public static <T> SerializablePredicate2<T> and(Iterable<? extends Predicate<? super T>> predicates) {
		return t -> {
			for (Predicate<? super T> predicate : defensiveCopy(predicates)) {
				if (!predicate.test(t)) {
					return false;
				}
			}
			return true;
		};
	}

	@SafeVarargs
	public static <T> SerializablePredicate2<? super T> and(Predicate<? super T>... predicates) {
		return and(Predicates2.<Predicate<? super T>>defensiveCopy(predicates));
	}

	public static <T> SerializablePredicate2<T> and(SerializablePredicate2<? super T> first, Predicate<? super T> second) {
		return and(ImmutableList.<Predicate<? super T>>of(first, second));
	}

	public static <T> SerializablePredicate2<T> or(Iterable<? extends Predicate<? super T>> predicates) {
		return t -> {
			for (Predicate<? super T> predicate : defensiveCopy(predicates)) {
				if (predicate.test(t)) {
					return true;
				}
			}
			return false;
		};
	}

	@SafeVarargs
	public static <T> SerializablePredicate2<T> or(Predicate<? super T>... predicates) {
		return or(Predicates2.<Predicate<? super T>>defensiveCopy(predicates));
	}

	public static <T> SerializablePredicate2<T> or(Predicate<? super T> first, Predicate<? super T> second) {
		return or(ImmutableList.<Predicate<? super T>>of(first, second));
	}

	public static <T> SerializablePredicate2<T> equalTo(T target) {
		return isEqual(target);
	}

	public static <T> SerializablePredicate2<T> isEqual(T target) {
		return SerializablePredicate2.isEqual(target);
	}

	public static SerializablePredicate2<Object> instanceOf(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz::isInstance;
	}

	public static SerializablePredicate2<Class<?>> subtypeOf(Class<?> clazz) {
		Objects.requireNonNull(clazz);
		return clazz::isAssignableFrom;
	}

	public static <T> SerializablePredicate2<T> in(Collection<? extends T> target) {
		Objects.requireNonNull(target);
		return target::contains;
	}

	public static <A, B> SerializablePredicate2<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(function);
		return a -> predicate.test(function.apply(a));
	}

	public static SerializablePredicate2<CharSequence> containsPattern(String pattern) {
		Objects.requireNonNull(pattern);
		return contains(Pattern.compile(pattern));
	}

	public static SerializablePredicate2<CharSequence> contains(Pattern pattern) {
		Objects.requireNonNull(pattern);
		return t -> pattern.matcher(t).find();
	}

	public static SerializablePredicate2<Boolean> isTrue() {
		return equalTo(true);
	}

	public static SerializablePredicate2<Boolean> isTrueOrNull() {
		return or(equalTo(true), isNull());
	}

	public static SerializablePredicate2<Boolean> isFalse() {
		return equalTo(false);
	}

	public static SerializablePredicate2<Boolean> isFalseOrNull() {
		return or(equalTo(false), isNull());
	}

	public static <T extends Collection<?>> SerializablePredicate2<T> isEmpty() {
		return c -> c == null || c.isEmpty();
	}

	public static <T extends Collection<?>> SerializablePredicate2<T> notEmpty() {
		return c -> c != null && !c.isEmpty();
	}

	public static <T extends Map<?, ?>> SerializablePredicate2<T> mapIsEmpty() {
		return m -> m == null || m.isEmpty();
	}

	public static <T extends Map<?, ?>> SerializablePredicate2<T> mapNotEmpty() {
		return m -> m != null && !m.isEmpty();
	}

	public static SerializablePredicate2<Collection<?>> contains(Object referenceValue) {
		return c -> c != null && c.contains(referenceValue);
	}

	public static SerializablePredicate2<Collection<?>> containsAny(Iterable<?> referenceValues) {
		Objects.requireNonNull(referenceValues);
		return c -> c.stream().anyMatch(Sets.newLinkedHashSet(referenceValues)::contains);
	}

	public static SerializablePredicate2<String> hasText() {
		return StringUtils::isNotBlank;
	}

	public static <T> SerializablePredicate2<T> comparesEqualTo(T value, Comparator<? super T> comparator) {
		return t -> comparator.compare(value, t) == 0;
	}

	public static <T> SerializablePredicate2<Iterable<? extends T>> any(Predicate<? super T> itemPredicate) {
		return t -> Streams.stream(t).anyMatch(itemPredicate);
	}

	public static <T> SerializablePredicate2<Iterable<? extends T>> all(Predicate<? super T> itemPredicate) {
		return t -> Streams.stream(t).allMatch(itemPredicate);
	}

	public static <T> SerializablePredicate2<T> notNullAnd(Predicate<? super T> predicate) {
		return and(notNull(), predicate);
	}

	public static <T> SerializablePredicate2<T> notNullAndNot(Predicate<? super T> predicate) {
		return notNullAnd(not(predicate));
	}

	@SafeVarargs
	private static <T> List<T> defensiveCopy(T... array) {
		return defensiveCopy(Arrays.asList(array));
	}

	private static <T> List<T> defensiveCopy(Iterable<T> iterable) {
		ArrayList<T> list = new ArrayList<>();
		for (T element : iterable) {
			list.add(Objects.requireNonNull(element));
		}
		return list;
	}

	private Predicates2() {
	}

}
