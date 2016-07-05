package fr.openwide.core.jpa.more.business.history.util;

import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;

/**
 * Update an object depending on the differences found on it.
 * <p>At the moment, only refreshes {@link HistoryEvent}.
 */
public interface IHistoryDifferenceHandler<T,
		HL extends AbstractHistoryLog<HL, HET, HD>,
		HET extends Enum<HET>,
		HD extends AbstractHistoryDifference<HD, HL>> {
	
	void handle(T entity, Difference<T> difference, HL historyLog);
	
}
