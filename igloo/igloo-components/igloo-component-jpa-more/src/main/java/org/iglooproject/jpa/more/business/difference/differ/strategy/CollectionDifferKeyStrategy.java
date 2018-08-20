package org.iglooproject.jpa.more.business.difference.differ.strategy;

import java.util.Collection;
import java.util.stream.Collectors;

import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.functional.Function2;
import org.iglooproject.jpa.more.business.difference.access.CollectionItemByKeyAccessor;

import com.google.common.base.Equivalence;

import de.danielbechler.diff.access.Accessor;

public final class CollectionDifferKeyStrategy<T, K> extends AbstractCollectionDifferStrategy<T, K> {
	private final Function2<? super T, ? extends K> keyFunction;
	private final Equivalence<? super K> keyEquivalence;
	private final Function2<? super K, String> toStringFunction;
	
	public CollectionDifferKeyStrategy(ItemContentComparisonStrategy itemContentComparisonStrategy,
			Function2<? super T, ? extends K> keyFunction, Equivalence<? super K> keyEquivalence,
			Function2<? super K, String> toStringFunction) {
		super(itemContentComparisonStrategy);
		this.keyFunction = keyFunction;
		this.keyEquivalence = keyEquivalence;
		this.toStringFunction = toStringFunction;
	}

	@Override
	protected Accessor createItemAccessor(K key) {
		return new CollectionItemByKeyAccessor<T, K>(key, keyFunction, keyEquivalence, toStringFunction.apply(key));
	}
	
	@Override
	public Iterable<K> difference(Collection<T> source, Collection<T> filter) {
		return CollectionUtils.difference(
				source == null ? null : source.stream().map(keyFunction).collect(Collectors.toList()),
				filter == null ? null : filter.stream().map(keyFunction).collect(Collectors.toList()),
				keyEquivalence
		);
	}

	@Override
	public Iterable<K> intersection(Collection<T> source, Collection<T> filter) {
		return CollectionUtils.intersection(
				source == null ? null : source.stream().map(keyFunction).collect(Collectors.toList()),
				filter == null ? null : filter.stream().map(keyFunction).collect(Collectors.toList()),
				keyEquivalence
		);
	}
}