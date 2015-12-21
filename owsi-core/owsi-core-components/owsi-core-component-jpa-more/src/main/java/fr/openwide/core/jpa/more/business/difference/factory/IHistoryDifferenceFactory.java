package fr.openwide.core.jpa.more.business.difference.factory;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Supplier;

import de.danielbechler.diff.node.DiffNode;
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;

public interface IHistoryDifferenceFactory<T> {
	<HD extends AbstractHistoryDifference<HD, ?>> List<HD> create(Supplier<HD> historyDifferenceSupplier,
			Difference<T> rootDifference, Collection<DiffNode> nodes);
}