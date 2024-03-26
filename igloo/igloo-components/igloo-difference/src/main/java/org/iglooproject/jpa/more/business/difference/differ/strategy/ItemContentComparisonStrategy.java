package org.iglooproject.jpa.more.business.difference.differ.strategy;

import de.danielbechler.diff.access.Accessor;
import de.danielbechler.diff.access.Instances;
import de.danielbechler.diff.differ.DifferDispatcher;
import de.danielbechler.diff.filtering.IsReturnableResolver;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.State;
import de.danielbechler.util.Assert;

public abstract class ItemContentComparisonStrategy {
	
	private static final ItemContentComparisonStrategy DEEP = new ItemContentComparisonStrategy() {
		@Override
		public DiffNode compareItem(DifferDispatcher differDispatcher, IsReturnableResolver isReturnableResolver,
				DiffNode parentNode, Instances parentInstances, Accessor itemAccessor) {
			return differDispatcher.dispatch(parentNode, parentInstances, itemAccessor);
		}
	};
	
	public static ItemContentComparisonStrategy deep() {
		return DEEP;
	}

	private static final ItemContentComparisonStrategy SHALLOW = new ItemContentComparisonStrategy() {
		@Override
		public DiffNode compareItem(DifferDispatcher differDispatcher, IsReturnableResolver isReturnableResolver, DiffNode parentNode,
				Instances parentInstances, Accessor itemAccessor) {
			Assert.notNull(parentInstances, "parentInstances");
			Assert.notNull(itemAccessor, "accessor");
			
			final DiffNode node = compare(parentNode, parentInstances, itemAccessor);
			if (parentNode != null && isReturnableResolver.isReturnable(node)) {
				parentNode.addChild(node);
			}
			return node;
		}

		private DiffNode compare(DiffNode parentNode, Instances parentInstances, Accessor itemAccessor) {
			Instances instances = parentInstances.access(itemAccessor);
			final DiffNode node = new DiffNode(parentNode, instances.getSourceAccessor(), instances.getType());
			if (instances.hasBeenAdded()) {
				node.setState(DiffNode.State.ADDED);
			} else if (instances.hasBeenRemoved()) {
				node.setState(DiffNode.State.REMOVED);
			} else if (instances.areSame() || instances.areEqual()) {
				node.setState(State.UNTOUCHED);
			} else {
				node.setState(State.CHANGED);
			}
			return node;
		}
	};
	
	public static ItemContentComparisonStrategy shallow() {
		return SHALLOW;
	}
	
	public abstract DiffNode compareItem(DifferDispatcher differDispatcher, IsReturnableResolver isReturnableResolver,
			DiffNode parentNode, Instances parentInstances, Accessor itemAccessor);

}
