package fr.openwide.core.commons.util.functional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * More useful suppliers.
 *
 * <p>All methods return serializable suppliers as long as they're given
 * serializable parameters.
 * 
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
 * ... = new MyType&lt;&gt;((Supplier&lt;? extends List&lt;T&gt;&gt;)Suppliers2.&lt;T&gt;arrayList());
 * 
 * // With xAsY
 * ... = new MyType&lt;&gt;(Suppliers2.&lt;T&gt;arrayListAsList());
 * // ... which is a tad shorter
 * </pre></code>
 * 
 * @see Suppliers
 *
 */
public final class Suppliers2 {

	private Suppliers2() { }

	public static <T> Supplier<T> constant(T value) {
		return new ConstantSupplier<T>(value);
	}
	
	private static class ConstantSupplier<T> implements Supplier<T>, Serializable {
		private static final long serialVersionUID = 6476238745119640079L;
		
		private final T value;
		
		public ConstantSupplier(T value) {
			super();
			this.value = value;
		}

		@Override
		public T get() {
			return value;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // LinkedListSupplier works for any T
	public static <T> Supplier<LinkedList<T>> linkedList() {
		return (Supplier) LinkedListSupplier.INSTANCE;
	}
	
	public static <T> Supplier<? extends List<T>> linkedListAsList() {
		return linkedList();
	}
	
	private static enum LinkedListSupplier implements Supplier<LinkedList<?>> {
		INSTANCE;
		
		@Override
		public LinkedList<?> get() {
			return Lists.newLinkedList();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // ArrayListSupplier works for any T
	public static <T> Supplier<ArrayList<T>> arrayList() {
		return (Supplier) ArrayListSupplier.INSTANCE;
	}
	
	public static <T> Supplier<? extends List<T>> arrayListAsList() {
		return arrayList();
	}
	
	private static enum ArrayListSupplier implements Supplier<ArrayList<?>> {
		INSTANCE;
		
		@Override
		public ArrayList<?> get() {
			return Lists.newArrayList();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // HashSetSupplier works for any T
	public static <T> Supplier<HashSet<T>> hashSet() {
		return (Supplier) HashSetSupplier.INSTANCE;
	}
	
	public static <T> Supplier<? extends Set<T>> hashSetAsSet() {
		return hashSet();
	}
	
	private static enum HashSetSupplier implements Supplier<HashSet<?>> {
		INSTANCE;
		
		@Override
		public HashSet<?> get() {
			return Sets.newHashSet();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // HashSetSupplier works for any T
	public static <T> Supplier<LinkedHashSet<T>> linkedHashSet() {
		return (Supplier) LinkedHashSetSupplier.INSTANCE;
	}
	
	public static <T> Supplier<? extends Set<T>> linkedHashSetAsSet() {
		return linkedHashSet();
	}
	
	private static enum LinkedHashSetSupplier implements Supplier<LinkedHashSet<?>> {
		INSTANCE;
		
		@Override
		public LinkedHashSet<?> get() {
			return Sets.newLinkedHashSet();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // NaturalOrderTreeSetSupplier works for any T
	public static <T extends Comparable> Supplier<TreeSet<T>> treeSet() {
		return (Supplier) NaturalOrderTreeSetSupplier.INSTANCE;
	}
	
	@SuppressWarnings("rawtypes")
	public static <T extends Comparable> Supplier<? extends Set<T>> treeSetAsSet() {
		return treeSet();
	}
	
	@SuppressWarnings("rawtypes")
	public static <T extends Comparable> Supplier<? extends SortedSet<T>> treeSetAsSortedSet() {
		return treeSet();
	}

	private static enum NaturalOrderTreeSetSupplier implements Supplier<TreeSet<?>> {
		INSTANCE;
		
		@Override
		@SuppressWarnings("rawtypes")
		public TreeSet<?> get() {
			return (TreeSet) Sets.newTreeSet();
		}
	}
	
	public static <T> Supplier<TreeSet<T>> treeSet(Comparator<? super T> comparator) {
		return new ComparatorTreeSetSupplier<T>(comparator);
	}
	
	public static <T> Supplier<? extends Set<T>> treeSetAsSet(Comparator<? super T> comparator) {
		return treeSet(comparator);
	}
	
	public static <T> Supplier<? extends SortedSet<T>> treeSetAsSortedSet(Comparator<? super T> comparator) {
		return treeSet(comparator);
	}
	
	private static class ComparatorTreeSetSupplier<T> implements Supplier<TreeSet<T>>, Serializable {
		private static final long serialVersionUID = 6476238745119640079L;
		
		private final Comparator<? super T> comparator;
		
		public ComparatorTreeSetSupplier(Comparator<? super T> comparator) {
			super();
			this.comparator = comparator;
		}

		@Override
		public TreeSet<T> get() {
			return Sets.newTreeSet(comparator);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // HashMapSupplier works for any K and V
	public static <K, V> Supplier<HashMap<K, V>> hashMap() {
		return (Supplier) HashMapSupplier.INSTANCE;
	}
	
	public static <K, V> Supplier<? extends Map<K, V>> hashMapAsMap() {
		return hashMap();
	}
	
	private static enum HashMapSupplier implements Supplier<HashMap<?, ?>> {
		INSTANCE;
		
		@Override
		public HashMap<?, ?> get() {
			return Maps.newHashMap();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // HashMapSupplier works any K and V
	public static <K, V> Supplier<LinkedHashMap<K, V>> linkedHashMap() {
		return (Supplier) LinkedHashMapSupplier.INSTANCE;
	}
	
	public static <K, V> Supplier<? extends Map<K, V>> linkedHashMapAsMap() {
		return linkedHashMap();
	}
	
	private static enum LinkedHashMapSupplier implements Supplier<LinkedHashMap<?, ?>> {
		INSTANCE;
		
		@Override
		public LinkedHashMap<?, ?> get() {
			return Maps.newLinkedHashMap();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" }) // NaturalOrderTreeMapSupplier works for any K and V
	public static <K extends Comparable, V> Supplier<TreeMap<K, V>> treeMap() {
		return (Supplier) NaturalOrderTreeMapSupplier.INSTANCE;
	}
	
	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V> Supplier<? extends Map<K, V>> treeMapAsMap() {
		return treeMap();
	}
	
	@SuppressWarnings("rawtypes")
	public static <K extends Comparable, V> Supplier<? extends SortedMap<K, V>> treeMapAsSortedMap() {
		return treeMap();
	}

	private static enum NaturalOrderTreeMapSupplier implements Supplier<TreeMap<?, ?>> {
		INSTANCE;
		
		@Override
		@SuppressWarnings("rawtypes")
		public TreeMap<?, ?> get() {
			return (TreeMap) Maps.newTreeMap();
		}
	}
	
	public static <K, V> Supplier<TreeMap<K, V>> treeMap(Comparator<? super K> comparator) {
		return new ComparatorTreeMapSupplier<K, V>(comparator);
	}
	
	public static <K, V> Supplier<? extends Map<K, V>> treeMapAsMap(Comparator<? super K> comparator) {
		return treeMap(comparator);
	}
	
	public static <K, V> Supplier<? extends SortedMap<K, V>> treeMapAsSortedMap(Comparator<? super K> comparator) {
		return treeMap(comparator);
	}
	
	private static class ComparatorTreeMapSupplier<K, V> implements Supplier<TreeMap<K, V>>, Serializable {
		private static final long serialVersionUID = 6476238745119640079L;
		
		private final Comparator<? super K> comparator;
		
		public ComparatorTreeMapSupplier(Comparator<? super K> comparator) {
			super();
			this.comparator = comparator;
		}

		@Override
		public TreeMap<K, V> get() {
			return Maps.newTreeMap(comparator);
		}
	}

}
