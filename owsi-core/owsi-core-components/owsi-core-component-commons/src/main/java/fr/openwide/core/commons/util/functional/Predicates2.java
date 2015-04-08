package fr.openwide.core.commons.util.functional;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public final class Predicates2 {

	private Predicates2() { }
	
	/**
	 * An identity-like predicate evaluating to true when the input is true, false when it is false,
	 * and <strong>false when the input is null</strong>.
	 * @see Predicates2#isTrueOrNull()
	 */
	public static Predicate<Boolean> isTrue() {
		return Predicates.equalTo(true);
	}
	
	/**
	 * An identity-like predicate evaluating to true when the input is true, false when it is false,
	 * and <strong>true when the input is null</strong>.
	 */
	public static Predicate<Boolean> isTrueOrNull() {
		return Predicates.or(Predicates.equalTo(true), Predicates.isNull());
	}

	/**
	 * A not-like predicate evaluating to false when the input is true, true when it is false,
	 * and <strong>false when the input is null</strong>.
	 * @see Predicates2#isFalseOrNull()
	 */
	public static Predicate<Boolean> isFalse() {
		return Predicates.equalTo(false);
	}

	/**
	 * A not-like predicate evaluating to false when the input is true, true when it is false,
	 * and <strong>true when the input is null</strong>.
	 */
	public static Predicate<Boolean> isFalseOrNull() {
		return Predicates.or(Predicates.equalTo(false), Predicates.isNull());
	}
	
	public static <T extends Collection<?>> Predicate<T> isEmpty() {
		return CollectionPredicate.IS_EMPTY.withNarrowedType();
	}
	
	public static <T extends Collection<?>> Predicate<T> notEmpty() {
		return CollectionPredicate.NOT_EMPTY.withNarrowedType();
	}

	private enum CollectionPredicate implements Predicate<Collection<?>>, Serializable {
		IS_EMPTY {
			@Override
			public boolean apply(Collection<?> input) {
				return input == null || input.isEmpty();
			}
		},
		NOT_EMPTY {
			@Override
			public boolean apply(Collection<?> input) {
				return input != null && !input.isEmpty();
			}
		};
		
		@SuppressWarnings("unchecked") // these Collection predicates work for any T that extends Collection<?>
		<T extends Collection<?>> Predicate<T> withNarrowedType() {
			return (Predicate<T>) this;
		}
	}
	
	public static <T extends Map<?, ?>> Predicate<T> mapIsEmpty() {
		return MapPredicate.IS_EMPTY.withNarrowedType();
	}
	
	public static <T extends Map<?, ?>> Predicate<T> mapNotEmpty() {
		return MapPredicate.NOT_EMPTY.withNarrowedType();
	}

	private enum MapPredicate implements Predicate<Map<?, ?>>, Serializable {
		IS_EMPTY {
			@Override
			public boolean apply(Map<?, ?> input) {
				return input == null || input.isEmpty();
			}
		},
		NOT_EMPTY {
			@Override
			public boolean apply(Map<?, ?> input) {
				return input != null && !input.isEmpty();
			}
		};
		
		@SuppressWarnings("unchecked") // these Map predicates work for any T that extends Map<?, ?>
		<T extends Map<?, ?>> Predicate<T> withNarrowedType() {
			return (Predicate<T>) this;
		}
	}
	
	public static Predicate<Collection<?>> contains(Object referenceValue) {
		return new ContainsPredicate(referenceValue);
	}
	
	private static class ContainsPredicate implements SerializablePredicate<Collection<?>> {
		private static final long serialVersionUID = -9193654606378621631L;
		
		private final Object referenceValue;
		
		public ContainsPredicate(Object referenceValue) {
			this.referenceValue = referenceValue;
		}
		
		@Override
		public boolean apply(Collection<?> input) {
			return input != null && input.contains(referenceValue);
		}
		
		@Override
		public String toString() {
			return "contains(" + referenceValue + ")";
		}
	}
	
	public static Predicate<Collection<?>> containsAny(Iterable<?> referenceValues) {
		return new ContainsAnyPredicate(referenceValues);
	}
	
	private static class ContainsAnyPredicate implements SerializablePredicate<Collection<?>> {
		private static final long serialVersionUID = -9193654606378621631L;
		
		private final Set<?> referenceValues;
		
		public ContainsAnyPredicate(Iterable<?> referenceValues) {
			this.referenceValues = Sets.newLinkedHashSet(checkNotNull(referenceValues));
		}
		
		@Override
		public boolean apply(Collection<?> input) {
			if (input == null) {
				return false;
			}
			for (Object value : referenceValues) {
				if (input.contains(value)) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public String toString() {
			return "containsAny(" + referenceValues + ")";
		}
	}
	
	public static Predicate<String> hasText() {
		return StringPredicate.HAS_TEXT;
	}
	
	private enum StringPredicate implements Predicate<String>, Serializable {
		HAS_TEXT {
			@Override
			public boolean apply(String input) {
				return StringUtils.isNotBlank(input);
			}
		}
	}
	
	public static <T> Predicate<T> comparesEqualTo(T value, Comparator<? super T> comparator) {
		return new ComparesEqualToPredicate<T>(value, comparator);
	}
	
	private static class ComparesEqualToPredicate<T> implements SerializablePredicate<T> {
		private static final long serialVersionUID = -9193654606378621631L;
		
		private final T referenceValue;
		private final Comparator<? super T> comparator;
		
		public ComparesEqualToPredicate(T header, Comparator<? super T> comparator) {
			this.referenceValue = header;
			this.comparator = comparator;
		}
		
		@Override
		public boolean apply(T input) {
			return comparator.compare(referenceValue, input) == 0;
		}
	}

	/**
	 * @return A predicate returning true if the given iterable is non-null, is non-empty and has at least one element
	 *         that satisfies {@code itemPredicate}.
	 * @see Iterables#any(Iterable, Predicate)
	 */
	public static <T> Predicate<Iterable<? extends T>> any(Predicate<? super T> itemPredicate) {
		return new IterableAnyPredicate<T>(itemPredicate);
	}
	
	private static class IterableAnyPredicate<T> implements Predicate<Iterable<? extends T>>, Serializable {
		private static final long serialVersionUID = -359783441767977199L;
		
		private final Predicate<? super T> itemPredicate;

		public IterableAnyPredicate(Predicate<? super T> itemPredicate) {
			super();
			this.itemPredicate = itemPredicate;
		}
		
		@Override
		public boolean apply(Iterable<? extends T> input) {
			return input != null && Iterables.any(input, itemPredicate);
		}
		
		@Override
		public String toString() {
			return new StringBuilder().append("any(").append(itemPredicate).append(")").toString();
		}
	}
	
	/**
	 * @return A predicate returning true if the given iterable is null, is empty or has only elements
	 *         that satisfy {@code itemPredicate}.
	 * @see Iterables#all(Iterable, Predicate)
	 */
	public static <T> Predicate<Iterable<? extends T>> all(Predicate<? super T> itemPredicate) {
		return new IterableAllPredicate<T>(itemPredicate);
	}
	
	private static class IterableAllPredicate<T> implements Predicate<Iterable<? extends T>>, Serializable {
		private static final long serialVersionUID = -359783441767977199L;
		
		private final Predicate<? super T> itemPredicate;

		public IterableAllPredicate(Predicate<? super T> itemPredicate) {
			super();
			this.itemPredicate = itemPredicate;
		}
		
		@Override
		public boolean apply(Iterable<? extends T> input) {
			return input == null || Iterables.all(input, itemPredicate);
		}
		
		@Override
		public String toString() {
			return new StringBuilder().append("all(").append(itemPredicate).append(")").toString();
		}
	}

	public static <T> Predicate<T> notNullAndNot(Predicate<T> predicate) {
		return Predicates.and(Predicates.notNull(), Predicates.not(predicate));
	}

}
