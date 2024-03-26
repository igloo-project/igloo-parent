package org.iglooproject.jpa.more.business.difference.factory;

import java.util.Collection;
import java.util.List;

import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.fieldpath.FieldPathComponent;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.util.DiffUtils;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;

import com.google.common.collect.Lists;

import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.DiffNode.Visitor;
import de.danielbechler.diff.node.Visit;

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
public final class DefaultHistoryDifferenceFactory<T> extends AbstractHistoryDifferenceFactory<T> {
	private Predicate2<? super DiffNode> branchFilter = input -> input.hasChanges();
	
	private Predicate2<? super DiffNode> nodeFilter = input -> DiffUtils.isItem(input) || !input.hasChildren();;
	
	public void setFilter(Predicate2<? super DiffNode> filter) {
		this.branchFilter = filter;
	}
	
	public void setNodeFilter(Predicate2<? super DiffNode> nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	@Override
	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> create(Supplier2<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, Collection<DiffNode> nodes, Predicate2<? super DiffNode> dynamicNodeFilter) {
		final List<HD> result = Lists.newLinkedList();
		for (DiffNode node : nodes) {
			FieldPath path = DiffUtils.toFieldPath(node.getPath());
			result.add(create(historyDifferenceSupplier, rootDifference, rootDifference.getDiffNode(), node, path,
					dynamicNodeFilter));
		}
		return result;
	}
	
	public <HD extends AbstractHistoryDifference<HD, ?>> HD create(Supplier2<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, DiffNode parentNode, DiffNode currentNode, FieldPath currentNodeRelativePath,
			Predicate2<? super DiffNode> dynamicNodeFilter) {
		HD difference = newHistoryDifference(historyDifferenceSupplier, rootDifference, parentNode, currentNode,
				currentNodeRelativePath, toHistoryDifferenceAction(currentNode));
		List<HD> subDifferences = createChildren(historyDifferenceSupplier, rootDifference, currentNode, FieldPath.ROOT,
				dynamicNodeFilter);
		add(difference, subDifferences);
		return difference;
	}

	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> createChildren(
			final Supplier2<HD> historyDifferenceSupplier, final Difference<T> rootDifference,
			final DiffNode parentNode, final FieldPath parentRelativePath,
			Predicate2<? super DiffNode> dynamicNodeFilter) {
		final List<HD> result = Lists.newLinkedList();
		parentNode.visitChildren(new Visitor() {
			@Override
			public void node(DiffNode currentNode, Visit visit) {
				visit.dontGoDeeper();
				if (branchFilter.test(currentNode)) {
					FieldPathComponent component = DiffUtils.toFieldPathComponent(currentNode.getPath().getLastElementSelector());
					FieldPath currentNodeRelativePath = parentRelativePath.append(component);
					if ((dynamicNodeFilter == null || dynamicNodeFilter.test(currentNode))
							&& nodeFilter.test(currentNode)) {
						result.add(create(historyDifferenceSupplier, rootDifference, parentNode, currentNode,
								currentNodeRelativePath, dynamicNodeFilter));
					} else {
						result.addAll(createChildren(historyDifferenceSupplier, rootDifference, currentNode,
								currentNodeRelativePath, dynamicNodeFilter));
					}
				}
			}
		});
		return result;
	}
}