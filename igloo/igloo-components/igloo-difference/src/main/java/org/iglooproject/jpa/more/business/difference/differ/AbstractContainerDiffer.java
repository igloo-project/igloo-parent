package org.iglooproject.jpa.more.business.difference.differ;

import java.util.Map;

import org.iglooproject.functional.Predicate2;
import org.iglooproject.jpa.more.business.difference.differ.strategy.AbstractContainerDifferStrategy;

import com.google.common.collect.Maps;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.comparison.ComparisonStrategyResolver;
import de.danielbechler.diff.differ.Differ;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.filtering.IsReturnableResolver;
import de.danielbechler.diff.node.DiffNode;

public abstract class AbstractContainerDiffer implements Differ {

	private final DifferDispatcher differDispatcher;
	private final ComparisonStrategyResolver comparisonStrategyResolver;
	private final IsReturnableResolver isReturnableResolver;
	private final Map<Predicate2<? super DiffNode>, AbstractContainerDifferStrategy<?, ?>> strategies = Maps.newLinkedHashMap();
	private final AbstractContainerDifferStrategy<?, ?> defaultStrategy;
	
	public AbstractContainerDiffer(DifferDispatcher differDispatcher,
			ComparisonStrategyResolver comparisonStrategyResolver, IsReturnableResolver isReturnableResolver,
			AbstractContainerDifferStrategy<?, ?> defaultStrategy) {
		this.differDispatcher = differDispatcher;
		this.comparisonStrategyResolver = comparisonStrategyResolver;
		this.isReturnableResolver = isReturnableResolver;
		this.defaultStrategy = defaultStrategy;
	}
	
	public void addStrategy(Predicate2<? super DiffNode> predicate, AbstractContainerDifferStrategy<?, ?> strategy) {
		strategies.put(predicate, strategy);
	}

	private static DiffNode newNode(final DiffNode parentNode, final Instances instances) {
		final Accessor accessor = instances.getSourceAccessor();
		final Class<?> type = instances.getType();
		return new DiffNode(parentNode, accessor, type);
	}

	@Override
	public DiffNode compare(DiffNode parentNode, Instances instances) {
		final DiffNode containerNode = newNode(parentNode, instances);
		for (Map.Entry<Predicate2<? super DiffNode>, AbstractContainerDifferStrategy<?, ?>> entry : strategies.entrySet()) {
			if (entry.getKey().test(containerNode)) {
				return compare(instances, entry.getValue(), containerNode);
			}
		}
		return compare(instances, defaultStrategy, containerNode);
	}

	protected <TContainer, K> DiffNode compare(Instances instances, AbstractContainerDifferStrategy<TContainer, K> strategy, DiffNode collectionNode) {
		TContainer working = strategy.toContainer(instances.getWorking());
		TContainer base = strategy.toContainer(instances.getBase());
		
		if (instances.hasBeenAdded()) {
			final TContainer addedItems = working;
			strategy.compareItems(differDispatcher, isReturnableResolver, collectionNode, instances, addedItems);
			if (collectionNode.hasChildren()) {
				collectionNode.setState(DiffNode.State.CHANGED);
			} else {
				// No child element, be it before or after the change: we'll say the collection did not change.
				collectionNode.setState(DiffNode.State.UNTOUCHED);
			}
		} else if (instances.hasBeenRemoved()) {
			final TContainer removedItems = base;
			strategy.compareItems(differDispatcher, isReturnableResolver, collectionNode, instances, removedItems);
			if (collectionNode.hasChildren()) {
				collectionNode.setState(DiffNode.State.CHANGED);
			} else {
				// No child element, be it before or after the change: we'll say the collection did not change.
				collectionNode.setState(DiffNode.State.UNTOUCHED);
			}
		} else if (instances.areSame()) {
			collectionNode.setState(DiffNode.State.UNTOUCHED);
		} else {
			final ComparisonStrategy comparisonStrategy = comparisonStrategyResolver.resolveComparisonStrategy(collectionNode);
			if (comparisonStrategy == null) {
				// Compare internally
				final Iterable<K> addedKeys = strategy.difference(working, base);
				final Iterable<K> removedKeys = strategy.difference(base, working);
				final Iterable<K> commonKeys = strategy.intersection(working, base);
				strategy.compareKeys(differDispatcher, isReturnableResolver, collectionNode, instances, addedKeys);
				strategy.compareKeys(differDispatcher, isReturnableResolver, collectionNode, instances, removedKeys);
				strategy.compareKeys(differDispatcher, isReturnableResolver, collectionNode, instances, commonKeys);
			} else {
				comparisonStrategy.compare(collectionNode, instances.getType(), working, base);
			}
		}
		
		return collectionNode;
	}

}