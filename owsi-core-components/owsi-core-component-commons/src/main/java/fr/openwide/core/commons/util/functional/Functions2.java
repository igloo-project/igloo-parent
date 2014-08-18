package fr.openwide.core.commons.util.functional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import org.javatuples.valueintf.IValue0;
import org.javatuples.valueintf.IValue1;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

public final class Functions2 {

	private Functions2() { }
	
	public static <T> Function<Collection<? extends T>, Collection<T>> unmodifiableCollection() {
		return new UnmodifiableCollectionFunction<T>();
	}
	
	private static final class UnmodifiableCollectionFunction<T> implements Function<Collection<? extends T>, Collection<T>>, Serializable {
		private static final long serialVersionUID = -3456570157416618375L;
		
		@Override
		public Collection<T> apply(Collection<? extends T> input) {
			return input == null ? null : Collections.unmodifiableCollection(input);
		}
		
		@Override
		public String toString() {
			return "unmodifiableCollection";
		}
	}
	
	public static <T> Function<List<? extends T>, List<T>> unmodifiableList() {
		return new UnmodifiableListFunction<T>();
	}
	
	private static final class UnmodifiableListFunction<T> implements Function<List<? extends T>, List<T>>, Serializable {
		private static final long serialVersionUID = -4973967075781864935L;
		
		@Override
		public List<T> apply(List<? extends T> input) {
			return input == null ? null : Collections.unmodifiableList(input);
		}
		
		@Override
		public String toString() {
			return "unmodifiableList";
		}
	}
	
	public static <T> Function<Set<? extends T>, Set<T>> unmodifiableSet() {
		return new UnmodifiableSetFunction<T>();
	}
	
	private static final class UnmodifiableSetFunction<T> implements Function<Set<? extends T>, Set<T>>, Serializable {
		private static final long serialVersionUID = 3544407931818217873L;
		
		@Override
		public Set<T> apply(Set<? extends T> input) {
			return input == null ? null : Collections.unmodifiableSet(input);
		}
		
		@Override
		public String toString() {
			return "unmodifiableSet";
		}
	}
	
	public static <T> Function<SortedSet<T>, SortedSet<T>> unmodifiableSortedSet() {
		return new UnmodifiableSortedSetFunction<T>();
	}
	
	private static final class UnmodifiableSortedSetFunction<T> implements Function<SortedSet<T>, SortedSet<T>>, Serializable {
		private static final long serialVersionUID = -1090617064751406961L;
		
		@Override
		public SortedSet<T> apply(SortedSet<T> input) {
			return input == null ? null : Collections.unmodifiableSortedSet(input);
		}
		
		@Override
		public String toString() {
			return "unmodifiableSortedSet";
		}
	}
	
	public static <K, V> Function<Map<? extends K, ? extends V>, Map<K, V>> unmodifiableMap() {
		return new UnmodifiableMapFunction<K, V>();
	}
	
	private static final class UnmodifiableMapFunction<K, V> implements Function<Map<? extends K, ? extends V>, Map<K, V>>, Serializable {
		private static final long serialVersionUID = 5952443669998059686L;
		
		@Override
		public Map<K, V> apply(Map<? extends K, ? extends V> input) {
			return input == null ? null : Collections.unmodifiableMap(input);
		}
		
		@Override
		public String toString() {
			return "unmodifiableMap";
		}
	}
	
	public static <R, C, V> Function<Table<? extends R, ? extends C, ? extends V>, Table<R, C, V>> unmodifiableTable() {
		return new UnmodifiableTableFunction<>();
	}
	
	private static final class UnmodifiableTableFunction<R, C, V> implements Function<Table<? extends R, ? extends C, ? extends V>, Table<R, C, V>>, Serializable {
		private static final long serialVersionUID = 5952443669998059686L;
		
		@Override
		public Table<R, C, V> apply(Table<? extends R, ? extends C, ? extends V> input) {
			return input == null ? null : Tables.unmodifiableTable(input);
		}
		
		@Override
		public String toString() {
			return "unmodifiableTable";
		}
	}
	
	public static <T> Function<Iterable<T>, T> first() {
		return new IterableFirstFunction<T>();
	}
	
	private static final class IterableFirstFunction<T> implements Function<Iterable<T>, T>, Serializable {

		private static final long serialVersionUID = -8259072136500802108L;

		@Override
		public T apply(Iterable<T> input) {
			return input == null ? null : Iterables.getFirst(input, null);
		}
		
		@Override
		public String toString() {
			return "first";
		}
	}
	
	/**
	 * Same as {@link Functions#forMap(Map, Object)}, except that the default value is defined as a function of the unknown key.
	 */
	public static <K, V> Function<K, V> forMap(Map<? super K, ? extends V> map, Function<? super K, ? extends V> defaultValueFunction) {
		return new ForMapFunction<>(map, defaultValueFunction);
	}
	
	private static final class ForMapFunction<K, V> implements Function<K, V>, Serializable {
		
		private static final long serialVersionUID = 1409896188722279336L;
		
		private final Map<? super K, ? extends V> map;
		private final Function<? super K, ? extends V> defaultValueFunction;

		public ForMapFunction(Map<? super K, ? extends V> map, Function<? super K, ? extends V> defaultValueFunction) {
			super();
			this.map = map;
			this.defaultValueFunction = defaultValueFunction;
		}

		@Override
		public V apply(K key) {
			if (map.containsKey(key)) {
				return map.get(key);
			} else {
				return defaultValueFunction.apply(key);
			}
		}
		
		@Override
		public String toString() {
			return "forMap(" + map + ", " + defaultValueFunction + ")";
		}
	}
	
	/**
	 * @return A function that returns its argument if {@code validValuePredicate.apply(argument)} and {@code defaultValueFunction.apply(argument)} otherwise.
	 */
	public static <T> Function<T, T> defaultValue(Predicate<? super T> validValuePredicate, Function<? super T, ? extends T> defaultValueFunction) {
		return new DefaultValueFunction<T>(validValuePredicate, defaultValueFunction);
	}
	
	/**
	 * Shorthand for {@code defaulValue(Predicates.notNull(), Functions.constant(valueIfInvalid))}
	 */
	public static <T> Function<T, T> defaultValue(T valueIfInvalid) {
		return defaultValue(Predicates.notNull(), Functions.constant(valueIfInvalid));
	}
	
	private static final class DefaultValueFunction<T> implements Function<T, T>, Serializable {
		private static final long serialVersionUID = 5952443669998059686L;
		
		private final Predicate<? super T> validValuePredicate;
		private final Function<? super T, ? extends T> defaultValueFunction;
		
		public DefaultValueFunction(Predicate<? super T> validValuePredicate, Function<? super T, ? extends T> defaultValueFunction) {
			super();
			this.validValuePredicate = validValuePredicate;
			this.defaultValueFunction = defaultValueFunction;
		}

		@Override
		public T apply(T input) {
			return validValuePredicate.apply(input) ? input : defaultValueFunction.apply(input);
		}
		
		@Override
		public String toString() {
			return "default(if not " + validValuePredicate + ", then " + defaultValueFunction + ")";
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) // Works for any K, since it is read-only
	public static <K> Function<Entry<? extends K, ?>, K> entryKey() {
		return (Function) EntryFunction.KEY;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) // Works for any V, since it is read-only
	public static <V> Function<Entry<?, ? extends V>, V> entryValue() {
		return (Function) EntryFunction.VALUE;
	}
	
	private enum EntryFunction implements Function<Entry<?, ?>, Object> {
		KEY {
			@Override
			public Object apply(Entry<?, ?> entry) {
				return entry.getKey();
			}
			@Override
			public String toString() {
				return "key";
			}
		},
		VALUE {
			@Override
			public Object apply(Entry<?, ?> entry) {
				return entry.getValue();
			}
			@Override
			public String toString() {
				return "value";
			}
		};
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) // Works for any T, since it is read-only
	public static <T> Function<IValue0<? extends T>, T> tupleValue0() {
		return (Function) JavaTuplesValue0Function.INSTANCE;
	}
	
	private enum JavaTuplesValue0Function implements Function<IValue0<?>, Object> {
		INSTANCE {
			@Override
			public Object apply(IValue0<?> entry) {
				return entry.getValue0();
			}
			@Override
			public String toString() {
				return "value0";
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" }) // Works for any T, since it is read-only
	public static <T> Function<IValue1<? extends T>, T> tupleValue1() {
		return (Function) JavaTuplesValue1Function.INSTANCE;
	}
	
	private enum JavaTuplesValue1Function implements Function<IValue1<?>, Object> {
		INSTANCE {
			@Override
			public Object apply(IValue1<?> entry) {
				return entry.getValue1();
			}
			@Override
			public String toString() {
				return "value1";
			}
		};
	}
}
