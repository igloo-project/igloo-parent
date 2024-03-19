package org.iglooproject.jpa.more.business.difference.differ.strategy;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.filtering.IsReturnableResolver;
import de.danielbechler.diff.node.DiffNode;

public abstract class AbstractContainerDifferStrategy<TContainer, K> {
	
	private final ItemContentComparisonStrategy itemContentComparisonStrategy;
	
	public AbstractContainerDifferStrategy(ItemContentComparisonStrategy itemContentComparisonStrategy) {
		super();
		this.itemContentComparisonStrategy = itemContentComparisonStrategy;
	}

	protected abstract Accessor createItemAccessor(K key);
	
	public abstract TContainer toContainer(Object object);
	
	public void compareKeys(DifferDispatcher differDispatcher, IsReturnableResolver isReturnableResolver,
			final DiffNode parentNode, final Instances parentInstances, final Iterable<K> keys) {
		for (final K key : keys) {
			final Accessor itemAccessor = createItemAccessor(key);
			doCompareItems(differDispatcher, isReturnableResolver, parentNode, parentInstances, itemAccessor);
		}
	}

	public void compareItems(DifferDispatcher differDispatcher, IsReturnableResolver isReturnableResolver,
			final DiffNode parentNode, final Instances parentInstances, final TContainer elements) {
		for (final K key : difference(elements, null)) {
			final Accessor itemAccessor = createItemAccessor(key);
			doCompareItems(differDispatcher, isReturnableResolver, parentNode, parentInstances, itemAccessor);
		}
	}
	
	private void doCompareItems(DifferDispatcher differDispatcher, IsReturnableResolver isReturnableResolver,
			DiffNode parentNode, Instances parentInstances, Accessor itemAccessor) {
		itemContentComparisonStrategy.compareItem(differDispatcher, isReturnableResolver, parentNode, parentInstances, itemAccessor);
	}
	
	public abstract Iterable<K> difference(TContainer source, TContainer filter);
	public abstract Iterable<K> intersection(TContainer source, TContainer filter);
}
