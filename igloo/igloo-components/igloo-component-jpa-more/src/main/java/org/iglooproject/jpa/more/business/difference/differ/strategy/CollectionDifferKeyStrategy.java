package org.iglooproject.jpa.more.business.difference.differ.strategy;

import java.util.Collection;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import de.danielbechler.diff.access.Accessor;
import org.iglooproject.jpa.more.business.difference.access.CollectionItemByKeyAccessor;
import org.iglooproject.commons.util.collections.CollectionUtils;

public final class CollectionDifferKeyStrategy<T, K> extends AbstractCollectionDifferStrategy<T, K> {
	private final Function<? super T, ? extends K> keyFunction;
	private final Equivalence<? super K> keyEquivalence;
	private final Function<? super K, String> toStringFunction;
	
	public CollectionDifferKeyStrategy(ItemContentComparisonStrategy itemContentComparisonStrategy,
			Function<? super T, ? extends K> keyFunction, Equivalence<? super K> keyEquivalence,
			Function<? super K, String> toStringFunction) {
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
				source == null ? null : Collections2.transform(source, keyFunction),
				filter == null ? null : Collections2.transform(filter, keyFunction),
				keyEquivalence
		);
	}

	@Override
	public Iterable<K> intersection(Collection<T> source, Collection<T> filter) {
		return CollectionUtils.intersection(
				source == null ? null : Collections2.transform(source, keyFunction),
				filter == null ? null : Collections2.transform(filter, keyFunction),
				keyEquivalence
		);
	}
}