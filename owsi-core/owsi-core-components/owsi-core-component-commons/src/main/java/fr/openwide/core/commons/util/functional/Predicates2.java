package fr.openwide.core.commons.util.functional;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;

public final class Predicates2 {

	private Predicates2() { }
	
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
