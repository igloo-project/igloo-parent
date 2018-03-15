package org.iglooproject.jpa.more.business.difference.access;

import java.util.Arrays;
import java.util.Collection;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.jpa.more.business.difference.selector.CollectionItemByKeySelector;

import com.google.common.base.Equivalence;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.selector.ElementSelector;

public class CollectionItemByKeyAccessor<T, K> implements Accessor, IKeyAwareAccessor<K> {
	
	private final K reference;
	private final Function2<? super T, ? extends K> keyFunction;
	private final Equivalence<? super K> equivalence;
	private final Predicate2<? super T> predicate;
	
	private final String humanReadableString;

	public CollectionItemByKeyAccessor(
			K reference, Function2<? super T, ? extends K> keyFunction, Equivalence<? super K> equivalence,
			String humanReadableString) {
		this.reference = reference;
		this.keyFunction = keyFunction;
		this.equivalence = equivalence;
		this.predicate = Predicates2.compose(equivalence.equivalentTo(reference), keyFunction);
		this.humanReadableString = "[" + humanReadableString + "]";
	}
	
	@Override
	public K getKey() {
		return reference;
	}

	@SuppressWarnings("unchecked")
	private static <T> Collection<T> objectAsCollection(final Object object) {
		if (object == null) {
			return null; // NOSONAR: we can't return an empty collection, since we don't know which subtype (List, Set, ...) is expected.
		} else if (object instanceof Collection) {
			return (Collection<T>) object;
		} else if (object instanceof Object[]) {
			return Arrays.asList((T[]) object);
		}
		throw new IllegalArgumentException(object.getClass().toString());
	}

	@Override
	public ElementSelector getElementSelector() {
		return new CollectionItemByKeySelector<>(keyFunction, equivalence, reference, humanReadableString);
	}

	@Override
	public void set(final Object target, final Object value) {
		throw new UnsupportedOperationException("Cannot set value on collection/array items.");
	}

	@Override
	public Object get(final Object target) {
		final Collection<T> targetCollection = objectAsCollection(target);
		if (targetCollection == null) {
			return null;
		}
		for (final T item : targetCollection) {
			if (item != null && predicate.test(item)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void unset(final Object target) {
		throw new UnsupportedOperationException("Cannot unset value on collection/array items.");
	}

	@Override
	public String toString() {
		return "collection/array item " + getElementSelector();
	}
}
