package fr.openwide.core.commons.util.functional;

import java.util.Collection;

import com.google.common.base.Predicate;

public final class Predicates2 {

	private Predicates2() { }
	
	public static <T extends Collection<?>> Predicate<T> isEmpty() {
		return CollectionPredicate.IS_EMPTY.withNarrowedType();
	}
	
	public static <T extends Collection<?>> Predicate<T> notEmpty() {
		return CollectionPredicate.NOT_EMPTY.withNarrowedType();
	}

	private enum CollectionPredicate implements Predicate<Collection<?>> {
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

}
