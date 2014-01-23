package fr.openwide.core.commons.util.functional;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

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
	
	public static Predicate<String> hasText() {
		return StringPredicate.HAS_TEXT;
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
	
	private enum StringPredicate implements Predicate<String>, Serializable {
		HAS_TEXT {
			@Override
			public boolean apply(String input) {
				return StringUtils.isNotBlank(input);
			}
		}
	}

}
