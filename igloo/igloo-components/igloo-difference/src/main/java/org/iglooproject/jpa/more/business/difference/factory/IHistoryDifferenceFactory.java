package org.iglooproject.jpa.more.business.difference.factory;

import java.util.Collection;
import java.util.List;

import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;

import de.danielbechler.diff.node.DiffNode;

public interface IHistoryDifferenceFactory<T> {

	<HD extends AbstractHistoryDifference<HD, ?>> List<HD> create(Supplier2<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, Collection<DiffNode> nodes, Predicate2<? super DiffNode> dynamicNodeFilter);

	@Deprecated
	default <HD extends AbstractHistoryDifference<HD, ?>> List<HD> create(Supplier2<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, Collection<DiffNode> nodes) {
		return create(historyDifferenceSupplier, rootDifference, nodes, null);
	}

}