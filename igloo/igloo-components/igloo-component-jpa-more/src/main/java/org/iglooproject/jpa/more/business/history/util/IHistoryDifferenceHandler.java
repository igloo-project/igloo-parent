package org.iglooproject.jpa.more.business.history.util;

import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;

/**
 * Update an object depending on the differences found on it.
 * <p>At the moment, the only implementation is {@link HistoryEventDifferenceHandler}.
 */
public interface IHistoryDifferenceHandler<T, HL extends AbstractHistoryLog<?, ?, ?>> {
	
	void handle(T entity, Difference<? extends T> difference, HL historyLog);
	
}
