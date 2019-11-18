package org.iglooproject.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.text.WordUtils;
import org.javatuples.Pair;
import org.javatuples.valueintf.IValue0;
import org.javatuples.valueintf.IValue1;

import com.google.common.base.Converter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

public final class Functions2 {

	private Functions2() {
	}

	public static <T, R> SerializableFunction2<T, R> from(com.google.common.base.Function<T, R> function) {
		Objects.requireNonNull(function);
		return function::apply;
	}

	public static <T, R> SerializableFunction2<T, R> from(Converter<T, R> converter) {
		Objects.requireNonNull(converter);
		return converter::convert;
	}

	public static <T> SerializableFunction2<T, T> identity() {
		return SerializableFunction2.identity();
	}

	public static SerializableFunction2<Object, String> toStringFunction() {
		return Object::toString;
	}

	public static <K, V> SerializableFunction2<K, V> forMap(Map<K, V> map) {
		Objects.requireNonNull(map);
		return map::get;
	}

	public static <K, V> SerializableFunction2<K, V> forMap(Map<? super K, ? extends V> map, V defaultValue) {
		Objects.requireNonNull(map);
		return forMap(map, constant(defaultValue));
	}

	public static <K, V> SerializableFunction2<K, V> forMap(Map<? super K, ? extends V> map, Function<? super K, ? extends V> defaultValueFunction) {
		return k -> Optional.ofNullable((V) map.get(k)).orElse(defaultValueFunction.apply(k));
	}

	public static <A, B, C> SerializableFunction2<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
		Objects.requireNonNull(g);
		Objects.requireNonNull(f);
		return a -> g.apply(f.apply(a));
	}

	public static <A, B, C> SerializableFunction2<A, C> andThen(Function<A, ? extends B> f, Function<B, C> g) {
		Objects.requireNonNull(g);
		Objects.requireNonNull(f);
		return a -> g.apply(f.apply(a));
	}

	public static <T> SerializableFunction2<T, Boolean> forPredicate(Predicate<T> predicate) {
		Objects.requireNonNull(predicate);
		return predicate::test;
	}

	public static <E> SerializableFunction2<Object, E> constant(E value) {
		return o -> value;
	}

	public static <T> SerializableFunction2<Object, T> forSupplier(Supplier<T> supplier) {
		Objects.requireNonNull(supplier);
		return t -> supplier.get();
	}

	public static <A, B> SerializableFunction2<Iterable<? extends A>, Iterable<B>> transformedIterable(Function<? super A, B> function) {
		return i -> (() -> Streams.stream(i).map(function).iterator());
	}

	public static <A, B> SerializableFunction2<Collection<? extends A>, Collection<B>> transformedCollection(Function<? super A, B> function) {
		return c -> c.stream().map(function).collect(Collectors.toCollection(ArrayList::new));
	}

	public static <A, B> SerializableFunction2<List<? extends A>, List<B>> transformedList(Function<? super A, B> function) {
		return l -> l.stream().map(function).collect(Collectors.toList());
	}

	public static <T> SerializableFunction2<Collection<? extends T>, Collection<T>> unmodifiableCollection() {
		return Collections::unmodifiableCollection;
	}

	public static <T> SerializableFunction2<List<? extends T>, List<T>> unmodifiableList() {
		return Collections::unmodifiableList;
	}

	public static <T> SerializableFunction2<Set<? extends T>, Set<T>> unmodifiableSet() {
		return Collections::unmodifiableSet;
	}

	public static <T> SerializableFunction2<SortedSet<T>, SortedSet<T>> unmodifiableSortedSet() {
		return Collections::unmodifiableSortedSet;
	}

	public static <K, V> SerializableFunction2<Map<? extends K, ? extends V>, Map<K, V>> unmodifiableMap() {
		return Collections::unmodifiableMap;
	}

	public static <R, C, V> SerializableFunction2<Table<? extends R, ? extends C, ? extends V>, Table<R, C, V>> unmodifiableTable() {
		return Tables::unmodifiableTable;
	}

	public static <T> SerializableFunction2<Iterable<T>, T> first() {
		return t -> Iterables.getFirst(t, null);
	}

	public static <T> SerializableFunction2<T, T> defaultValue(Predicate<? super T> validValuePredicate, Function<? super T, ? extends T> defaultValueFunction) {
		Objects.requireNonNull(validValuePredicate);
		Objects.requireNonNull(defaultValueFunction);
		return t -> validValuePredicate.test(t) ? t : defaultValueFunction.apply(t);
	}

	public static <T> SerializableFunction2<T, T> defaultValue(T valueIfInvalid) {
		return defaultValue(Predicates2.notNull(), constant(valueIfInvalid));
	}

	public static <K> SerializableFunction2<Entry<? extends K, ?>, K> entryKey() {
		return Entry::getKey;
	}

	public static <V> SerializableFunction2<Entry<?, ? extends V>, V> entryValue() {
		return Entry::getValue;
	}

	public static <K, V> SerializableFunction2<Entry<? extends K, ? extends V>, Pair<K, V>> entryToPair() {
		return e -> e != null ? Pair.with(e.getKey(), e.getValue()) : null;
	}

	public static <T> SerializableFunction2<IValue0<? extends T>, T> tupleValue0() {
		return IValue0::getValue0;
	}

	public static <T> SerializableFunction2<IValue1<? extends T>, T> tupleValue1() {
		return IValue1::getValue1;
	}

	public static SerializableFunction2<String, String> capitalize() {
		return WordUtils::capitalize;
	}

	public static SerializableFunction2<String, String> uncapitalize() {
		return WordUtils::uncapitalize;
	}

}
