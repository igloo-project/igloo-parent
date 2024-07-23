package org.iglooproject.jpa.more.business.difference.factory;

import de.danielbechler.diff.node.DiffNode;
import java.util.Collection;
import java.util.List;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;

public interface IHistoryDifferenceFactory<T> {

  <HD extends AbstractHistoryDifference<HD, ?>> List<HD> create(
      Supplier2<HD> historyDifferenceSupplier,
      Difference<T> rootDifference,
      Collection<DiffNode> nodes);
}
