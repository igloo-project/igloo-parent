package fr.openwide.core.jpa.more.business.history.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.difference.service.IDifferenceService;
import fr.openwide.core.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import fr.openwide.core.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import fr.openwide.core.jpa.more.business.history.dao.IAbstractHistoryLogDao;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogObjectsBean;
import fr.openwide.core.jpa.more.business.history.util.HistoryLogBeforeCommitTask;
import fr.openwide.core.jpa.more.business.history.util.HistoryLogBeforeCommitWithDifferencesTask;
import fr.openwide.core.jpa.more.business.history.util.IDifferenceHandler;
import fr.openwide.core.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;

public abstract class AbstractHistoryLogServiceImpl<HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>>
		extends GenericEntityServiceImpl<Long, HL> implements IAbstractHistoryLogService<HL, HET, HD> {

	@Autowired
	protected ITransactionSynchronizationTaskManagerService transactionSynchronizationTaskManagerService;

	@Autowired
	protected IHistoryValueService valueService;
	
	@Autowired
	public AbstractHistoryLogServiceImpl(IAbstractHistoryLogDao<HL, HET> dao) {
		super(dao);
	}
	
	@Override
	public HL logNow(Date date, HET eventType, List<HD> differences, AbstractHistoryLogObjectsBean<?> objects)
			throws ServiceException, SecurityServiceException {
		HL log = newHistoryLog(date, eventType, differences, objects);

		log.setDifferences(differences);
		for (HD difference : differences) {
			difference.setParentLog(log);
		}
		
		create(log);
		return log;
	}
	
	protected abstract HL newHistoryLog(Date date, HET eventType, List<HD> differences, AbstractHistoryLogObjectsBean<?> objects);

	protected abstract Supplier<HD> newHistoryDifferenceSupplier();

	protected void setSecondaryObjects(HL log, AbstractHistoryLogObjectsBean<?> objects) {
		setSecondaryObjects(log, objects.getSecondaryObjects());
	}

	protected void setSecondaryObjects(HL log, Object[] objects) {
		if (objects.length > 4) {
			throw new IllegalArgumentException(String.format("Too many arguments (%d, expected %d or less)", objects.length, 4));
		}
		if (objects.length >= 1) {
			log.setObject1(valueService.create(objects[0]));
		}
		if (objects.length >= 2) {
			log.setObject2(valueService.create(objects[1]));
		}
		if (objects.length >= 3) {
			log.setObject3(valueService.create(objects[2]));
		}
		if (objects.length >= 4) {
			log.setObject4(valueService.create(objects[3]));
		}
	}

	@Override
	public <T> void log(HET eventType, AbstractHistoryLogObjectsBean<T> objects) throws ServiceException,
			SecurityServiceException {
		transactionSynchronizationTaskManagerService.push(
				new HistoryLogBeforeCommitTask<T, HL, HET, HD>(new Date(), eventType, objects)
		);
	}

	@Override
	public final <T> void logWithDifferences(HET eventType, AbstractHistoryLogObjectsBean<T> objects,
			IDifferenceService<T> differenceService) throws ServiceException, SecurityServiceException {
		logWithDifferences(eventType, objects, differenceService.getMainDifferenceGenerator(),
				differenceService);
	}

	@Override
	@SafeVarargs
	public final <T> void logWithDifferences(HET eventType, AbstractHistoryLogObjectsBean<T> objects,
			IDifferenceService<T> differenceService, IDifferenceHandler<T> ... differenceHandlers) throws ServiceException,
			SecurityServiceException {
		logWithDifferences(eventType, objects, differenceService.getMainDifferenceGenerator(),
				differenceService, differenceHandlers);
	}
	
	@Override
	@SafeVarargs
	public final <T> void logWithDifferences(HET eventType, AbstractHistoryLogObjectsBean<T> objects,
			IDifferenceFromReferenceGenerator<T> differenceGenerator,
			IHistoryDifferenceGenerator<T> historyDifferenceGenerator, IDifferenceHandler<T>... differenceHandlers) throws ServiceException, SecurityServiceException {
		transactionSynchronizationTaskManagerService.push(
				new HistoryLogBeforeCommitWithDifferencesTask<T, HL, HET, HD>(
						new Date(), eventType, objects,
						newHistoryDifferenceSupplier(),
						differenceGenerator, historyDifferenceGenerator, differenceHandlers
				)
		);
	}

}
