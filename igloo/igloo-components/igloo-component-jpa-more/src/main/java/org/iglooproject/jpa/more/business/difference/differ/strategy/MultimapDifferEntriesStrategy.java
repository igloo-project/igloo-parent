package org.iglooproject.jpa.more.business.difference.differ.strategy;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.common.base.Equivalence;
import com.google.common.collect.Multimap;

import de.danielbechler.diff.access.Accessor;
import org.iglooproject.jpa.more.business.difference.access.MultimapEntryAccessor;
import org.iglooproject.commons.util.collections.CollectionUtils;

public class MultimapDifferEntriesStrategy<K, V> extends AbstractContainerDifferStrategy<Multimap<K, V>, Entry<K, V>> {
	
	private final Equivalence<? super Entry<K, V>> entryEquivalence;

	public MultimapDifferEntriesStrategy(Equivalence<? super Entry<K, V>> entryEquivalence) {
		super(ItemContentComparisonStrategy.shallow());
		this.entryEquivalence = entryEquivalence;
	}

	@Override
	protected Accessor createItemAccessor(Entry<K, V> entry) {
		return new MultimapEntryAccessor<>(entry, entryEquivalence);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Multimap<K, V> toContainer(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Multimap) {
			return (Multimap)object;
		} else {
			throw new IllegalArgumentException("This differ only supports Maps and Multimaps.");
		}
	}
	
	@Override
	public Collection<Entry<K, V>> difference(Multimap<K, V> source, Multimap<K, V> filter) {
		Collection<Entry<K, V>> sourceEntries = null;
		Collection<Entry<K, V>> filterEntries = null;
		
		if (source != null) {
			sourceEntries = source.entries();
		}
		
		if (filter != null) {
			filterEntries = filter.entries();
		}
		
		return CollectionUtils.difference(sourceEntries, filterEntries, entryEquivalence);
	}
	
	@Override
	public Collection<Entry<K, V>> intersection(Multimap<K, V> source, Multimap<K, V> filter) {
		Collection<Entry<K, V>> sourceEntries = null;
		Collection<Entry<K, V>> filterEntries = null;
		
		if (source != null) {
			sourceEntries = source.entries();
		}
		
		if (filter != null) {
			filterEntries = filter.entries();
		}
		return CollectionUtils.intersection(sourceEntries, filterEntries, entryEquivalence);
	}
}
