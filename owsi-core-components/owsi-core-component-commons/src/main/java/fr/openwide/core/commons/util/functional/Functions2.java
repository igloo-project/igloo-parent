package fr.openwide.core.commons.util.functional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class Functions2 {

	private Functions2() { }
	
	public <T> Function<Collection<? extends T>, Collection<T>> unmodifiableCollection() {
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
	
	public <T> Function<List<? extends T>, List<T>> unmodifiableList() {
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
	
	public <T> Function<Set<? extends T>, Set<T>> unmodifiableSet() {
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
	
	public <T> Function<SortedSet<T>, SortedSet<T>> unmodifiableSortedSet() {
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
	
	public <K, V> Function<Map<? extends K, ? extends V>, Map<K, V>> unmodifiableMap() {
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
	
}
