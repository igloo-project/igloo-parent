package org.iglooproject.jpa.more.business.difference.util;

import java.util.List;

import com.google.common.base.Supplier;

import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;

public interface IHistoryDifferenceGenerator<T> {
	
	 <HD extends AbstractHistoryDifference<HD, ?>> List<HD> toHistoryDifferences(Supplier<HD> historyDifferenceSupplier, Difference<T> rootDifference);

}
