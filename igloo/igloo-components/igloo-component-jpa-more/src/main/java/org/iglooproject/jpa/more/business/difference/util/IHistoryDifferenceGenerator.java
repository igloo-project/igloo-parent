package org.iglooproject.jpa.more.business.difference.util;

import java.util.List;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;

public interface IHistoryDifferenceGenerator<T> {

  <HD extends AbstractHistoryDifference<HD, ?>> List<HD> toHistoryDifferences(
      Supplier2<HD> historyDifferenceSupplier, Difference<T> rootDifference);
}
