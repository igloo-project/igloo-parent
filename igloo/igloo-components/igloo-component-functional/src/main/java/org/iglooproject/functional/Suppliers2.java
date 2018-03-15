package org.iglooproject.functional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * <p>Methods named xAsY ({@link #linkedListAsList()}, {@link #treeSetAsSet(Comparator)}, and so on) are just here to
 * make your like easy when you want to use the diamond operator on a method that receives your supplier as a parameter.
 * For instance:
 * <code><pre>
 * public MyType&lt;T, C extends Collection&lt;T&gt;&gt; {
 *   public MyType(Supplier&lt;C&gt; supplier) {
 *     // ...
 *   }
 * }
 * 
 * // With the original method
 * ... = new MyType&lt;&gt;((Supplier&lt;? extends List&lt;T&gt;&gt;)Supplier2.&lt;T&gt;arrayList());
 * 
 * // With xAsY
 * ... = new MyType&lt;&gt;(Supplier2.&lt;T&gt;arrayListAsList());
 * // ... which is a tad shorter
 * </pre></code>
 */
public final class Suppliers2 {

	public static <T> SerializableSupplier2<T> from(Supplier<T> supplier) {
		Objects.requireNonNull(supplier);
		return supplier::get;
	}

	public static <F, T> SerializableSupplier2<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
		return () -> function.apply(supplier.get());
	}

	/**
	 * @see {@link com.google.common.base.Suppliers#memoize(com.google.common.base.Supplier)}
	 */
	public static <T> SerializableSupplier2<T> memoize(Supplier<T> delegate) {
		return from(com.google.common.base.Suppliers.memoize(from(delegate)));
	}

	/**
	 * @see {@link com.google.common.base.Suppliers#memoizeWithExpiration(com.google.common.base.Supplier, long, TimeUnit)}
	 */
	public static <T> SerializableSupplier2<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
		return from(com.google.common.base.Suppliers.memoizeWithExpiration(from(delegate), duration, unit));
	}

	public static <T> SerializableSupplier2<T> ofInstance(T instance) {
		return () -> instance;
	}

	/**
	 * @see {@link com.google.common.base.Suppliers#synchronizedSupplier(com.google.common.base.Supplier)}
	 */
	public static <T> SerializableSupplier2<T> synchronizedSupplier(Supplier<T> delegate) {
		return from(com.google.common.base.Suppliers.synchronizedSupplier(from(delegate)));
	}

	public static <T> SerializableFunction2<Supplier<T>, T> supplierFunction() {
		return (s) -> s.get();
	}

	public static <T> SerializableSupplier2<LinkedList<T>> linkedList() {
		return Lists::newLinkedList;
	}

	public static <T> SerializableSupplier2<? extends List<T>> linkedListAsList() {
		return linkedList();
	}

	public static <T> SerializableSupplier2<ArrayList<T>> arrayList() {
		return Lists::newArrayList;
	}

	public static <T> SerializableSupplier2<? extends List<T>> arrayListAsList() {
		return arrayList();
	}

	public static <T> SerializableSupplier2<HashSet<T>> hashSet() {
		return Sets::newHashSet;
	}

	public static <T> SerializableSupplier2<? extends Set<T>> hashSetAsSet() {
		return hashSet();
	}

	public static <T> SerializableSupplier2<LinkedHashSet<T>> linkedHashSet() {
		return Sets::newLinkedHashSet;
	}

	public static <T> SerializableSupplier2<? extends Set<T>> linkedHashSetAsSet() {
		return linkedHashSet();
	}

	@SuppressWarnings("rawtypes")
	public static <T extends Comparable> SerializableSupplier2<TreeSet<T>> treeSet() {
		return Sets::newTreeSet;
	}

	@SuppressWarnings("rawtypes")
	public static <T extends Comparable> SerializableSupplier2<? extends Set<T>> treeSetAsSet() {
		return treeSet();
	}

	@SuppressWarnings("rawtypes")
	public static <T extends Comparable> SerializableSupplier2<? extends SortedSet<T>> treeSetAsSortedSet() {
		return treeSet();
	}

	public static <T> SerializableSupplier2<TreeSet<T>> treeSet(Comparator<? super T> comparator) {
		return () -> Sets.newTreeSet(comparator);
	}

	public static <T> SerializableSupplier2<? extends Set<T>> treeSetAsSet(Comparator<? super T> comparator) {
		return treeSet(comparator);
	}

	public static <T> SerializableSupplier2<? extends SortedSet<T>> treeSetAsSortedSet(Comparator<? super T> comparator) {
		return treeSet(comparator);
	}

	public static <K, V> SerializableSupplier2<HashMap<K, V>> hashMap() {
		return Maps::newHashMap;
	}

	public static <K, V> SerializableSupplier2<? extends Map<K, V>> hashMapAsMap() {
		return hashMap();
	}

	public static <K, V> SerializableSupplier2<LinkedHashMap<K, V>> linkedHashMap() {
		return Maps::newLinkedHashMap;
	}

	public static <K, V> SerializableSupplier2<? extends Map<K, V>> linkedHashMapAsMap() {
		return linkedHashMap();
	}

	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V> SerializableSupplier2<TreeMap<K, V>> treeMap() {
		return Maps::newTreeMap;
	}

	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V> SerializableSupplier2<? extends Map<K, V>> treeMapAsMap() {
		return treeMap();
	}

	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V> SerializableSupplier2<? extends SortedMap<K, V>> treeMapAsSortedMap() {
		return treeMap();
	}

	public static <K, V> SerializableSupplier2<TreeMap<K, V>> treeMap(Comparator<? super K> comparator) {
		return () -> Maps.newTreeMap(comparator);
	}

	public static <K, V> SerializableSupplier2<? extends Map<K, V>> treeMapAsMap(Comparator<? super K> comparator) {
		return treeMap(comparator);
	}

	public static <K, V> SerializableSupplier2<? extends SortedMap<K, V>> treeMapAsSortedMap(Comparator<? super K> comparator) {
		return treeMap(comparator);
	}

	private Suppliers2() {
	}

}
