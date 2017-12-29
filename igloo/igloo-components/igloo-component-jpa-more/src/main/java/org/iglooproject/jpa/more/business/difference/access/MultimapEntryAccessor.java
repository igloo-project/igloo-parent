package org.iglooproject.jpa.more.business.difference.access;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Equivalence;
import com.google.common.collect.Multimap;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.selector.ElementSelector;
import org.iglooproject.jpa.more.business.difference.selector.MultimapEntrySelector;

public class MultimapEntryAccessor<K, V> implements Accessor, IKeyAwareAccessor<K> {

	private final Map.Entry<K, V> entry;
	private final Equivalence<? super Entry<K, V>> entryEquivalence;

	public MultimapEntryAccessor(Map.Entry<K, V> entry, Equivalence<? super Entry<K, V>> entryEquivalence) {
		super();
		this.entry = entry;
		this.entryEquivalence = entryEquivalence;
	}

	@Override
	public K getKey() {
		return entry.getKey();
	}

	@Override
	public ElementSelector getElementSelector() {
		return new MultimapEntrySelector<>(entry, entryEquivalence);
	}

	@SuppressWarnings("rawtypes")
	protected Multimap objectToMultimap(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Multimap) {
			return (Multimap) object;
		} else {
			throw new IllegalArgumentException("Expecting a Multimap");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object get(final Object target) {
		final Multimap<K, V> targetMap = objectToMultimap(target);
		if (targetMap != null) {
			Collection<? extends Map.Entry<K, V>> entries = targetMap.entries();
			for (final Map.Entry<K, V> item : entries) {
				if (item != null && entryEquivalence.equivalent(entry, item)) {
					return item;
				}
			}
		}
		return null;
	}

	@Override
	public void set(final Object target, final Object value) {
		throw new UnsupportedOperationException("Cannot set value on multimap entries.");
	}

	@Override
	public void unset(final Object target) {
		throw new UnsupportedOperationException("Cannot unset value on multimap entries.");
	}

}
