package fr.openwide.core.jpa.more.business.difference.factory;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.Visitor;
import de.danielbechler.diff.node.Visit;
import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.commons.util.fieldpath.FieldPathComponent;
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.difference.util.DiffUtils;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;

/**
 * A HistoryDifferenceFactory which creates a HistoryDifference for each DiffNode:
 * <ul>
 * <li>which respects the predicate <code>branchFilter</code>
 * <li>AND all the parents of which respect the predicate <code>branchFilter</code>
 * <li>AND which respects the predicate <code>nodeFilter</code>
 * </ul>
 * 
 * <p>By default, the <code>branchFilter</code> only includes the modified nodes (CHANGED, ADDED, REMOVED).
 * <p>By default, the <code>nodeFilter</code> is right for each node:
 * <ul>
 * 	<li>which is an element of a collection or a map
 * 	<li>OR which does not have any children
 * </ul>
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class DefaultHistoryDifferenceFactory<T> extends AbstractHistoryDifferenceFactory<T> {
	private Predicate<? super DiffNode> branchFilter = new Predicate<DiffNode>() {
		@Override
		public boolean apply(DiffNode input) {
			return input.hasChanges();
		}
	};
	
	private Predicate<? super DiffNode> nodeFilter = new Predicate<DiffNode>() {
		@Override
		public boolean apply(DiffNode input) {
			return DiffUtils.isItem(input) || !input.hasChildren();
		}
	};
	
	public void setFilter(Predicate<? super DiffNode> filter) {
		this.branchFilter = filter;
	}
	
	public void setNodeFilter(Predicate<? super DiffNode> nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	@Override
	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> create(Supplier<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, Collection<DiffNode> nodes) {
		final List<HD> result = Lists.newLinkedList();
		for (DiffNode node : nodes) {
			FieldPath path = DiffUtils.toFieldPath(node.getPath());
			result.add(create(historyDifferenceSupplier, rootDifference, rootDifference.getDiffNode(), node, path));
		}
		return result;
	}
	
	public <HD extends AbstractHistoryDifference<HD, ?>> HD create(Supplier<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, DiffNode parentNode, DiffNode currentNode, FieldPath currentNodeRelativePath) {
		HD difference = newHistoryDifference(historyDifferenceSupplier, rootDifference, parentNode,
				currentNode, currentNodeRelativePath, toHistoryDifferenceAction(currentNode));
		List<HD> subDifferences = createChildren(historyDifferenceSupplier, rootDifference, currentNode, FieldPath.ROOT);
		add(difference, subDifferences);
		return difference;
	}
	
	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> createChildren(final Supplier<HD> historyDifferenceSupplier,
			final Difference<T> rootDifference, final DiffNode parentNode, final FieldPath parentRelativePath) {
		final List<HD> result = Lists.newLinkedList();
		parentNode.visitChildren(new Visitor() {
			@Override
			public void node(DiffNode currentNode, Visit visit) {
				visit.dontGoDeeper();
				if (branchFilter.apply(currentNode)) {
					FieldPathComponent component = DiffUtils.toFieldPathComponent(currentNode.getPath().getLastElementSelector());
					FieldPath currentNodeRelativePath = parentRelativePath.append(component);
					if (nodeFilter.apply(currentNode)) {
						result.add(create(historyDifferenceSupplier, rootDifference, parentNode, currentNode, currentNodeRelativePath));
					} else {
						result.addAll(createChildren(historyDifferenceSupplier, rootDifference, currentNode, currentNodeRelativePath));
					}
				}
			}
		});
		return result;
	}
}