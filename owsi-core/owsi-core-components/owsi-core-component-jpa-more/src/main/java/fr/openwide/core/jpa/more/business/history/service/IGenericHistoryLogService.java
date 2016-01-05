package fr.openwide.core.jpa.more.business.history.service;

import java.util.Date;
import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.difference.service.IDifferenceService;
import fr.openwide.core.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import fr.openwide.core.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogObjectsBean;
import fr.openwide.core.jpa.more.business.history.util.IDifferenceHandler;

public interface IGenericHistoryLogService<
				HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>>
		extends IGenericEntityService<Long, HL> {
	
	<T> void log(HET eventType, AbstractHistoryLogObjectsBean<T> objects)
			throws ServiceException, SecurityServiceException;

	<T> void logWithDifferences(HET eventType, AbstractHistoryLogObjectsBean<T> objects, IDifferenceService<T> differenceService)
			throws ServiceException, SecurityServiceException;
	
	@SuppressWarnings("unchecked")
	<T> void logWithDifferences(HET eventType, AbstractHistoryLogObjectsBean<T> objects, IDifferenceService<T> differenceService,
			IDifferenceHandler<T> ... differenceHandlers)
			throws ServiceException, SecurityServiceException;
	
	/*
	 * Using interfaces more generic than IDifferenceService here allows the caller to use
	 * objects that compute differences in a customizable way (for instance in order to occasionally
	 * ignore some fields).
	 */
	@SuppressWarnings("unchecked")
	<T> void logWithDifferences(HET eventType, AbstractHistoryLogObjectsBean<T> objects,
			IDifferenceFromReferenceGenerator<T> differenceGenerator,
			IHistoryDifferenceGenerator<T> historyDifferenceGenerator,
			IDifferenceHandler<T> ... differenceHandlers)
			throws ServiceException, SecurityServiceException;
	
	HL logNow(Date date, HET eventType, List<HD> differences, AbstractHistoryLogObjectsBean<?> objects)
			throws ServiceException, SecurityServiceException;

}
