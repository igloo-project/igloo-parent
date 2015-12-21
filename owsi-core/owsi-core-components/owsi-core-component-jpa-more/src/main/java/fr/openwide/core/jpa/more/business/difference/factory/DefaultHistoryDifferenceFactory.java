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
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.difference.util.DiffUtils;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPathComponent;

/**
 * Une HistoryDifferenceFactory qui crée un HistoryDifference pour chaque DiffNode:
 * <ul>
 * <li>qui respecte le prédicat <code>branchFilter</code>
 * <li>ET dont tous les parents respectent le prédicat <code>branchFilter</code>
 * <li>ET qui respecte le prédicat <code>nodeFilter</code>
 * </ul>
 * 
 * <p>Par défaut, le <code>branchFilter</code> n'inclut que les noeuds modifiés (CHANGED, ADDED, REMOVED).
 * <p>Par défaut le <code>nodeFilter</code> est vrai pour tout noeud :
 * <ul>
 * 	<li>qui est un élément de Collection ou de Map
 * 	<li>OU qui n'a pas d'enfant
 * </ul>
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class DefaultHistoryDifferenceFactory<T2> extends AbstractHistoryDifferenceFactory<T2> {
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
			Difference<T2> rootDifference, Collection<DiffNode> nodes) {
		final List<HD> result = Lists.newLinkedList();
		for (DiffNode node : nodes) {
			FieldPath path = DiffUtils.toFieldPath(node.getPath());
			result.add(create(historyDifferenceSupplier, rootDifference, rootDifference.getDiffNode(), node, path));
		}
		return result;
	}
	
	public <HD extends AbstractHistoryDifference<HD, ?>> HD create(Supplier<HD> historyDifferenceSupplier,
			Difference<T2> rootDifference, DiffNode parentNode, DiffNode currentNode, FieldPath currentNodeRelativePath) {
		HD difference = newHistoryDifference(historyDifferenceSupplier, rootDifference, parentNode,
				currentNode, currentNodeRelativePath, toHistoryDifferenceAction(currentNode));
		List<HD> subDifferences = createChildren(historyDifferenceSupplier, rootDifference, currentNode, FieldPath.ROOT);
		add(difference, subDifferences);
		return difference;
	}
	
	public <HD extends AbstractHistoryDifference<HD, ?>> List<HD> createChildren(final Supplier<HD> historyDifferenceSupplier,
			final Difference<T2> rootDifference, final DiffNode parentNode, final FieldPath parentRelativePath) {
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