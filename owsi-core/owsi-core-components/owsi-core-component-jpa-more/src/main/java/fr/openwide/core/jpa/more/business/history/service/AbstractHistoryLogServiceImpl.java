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
import fr.openwide.core.jpa.more.business.history.dao.IGenericHistoryLogDao;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import fr.openwide.core.jpa.more.business.history.util.HistoryLogBeforeCommitTask;
import fr.openwide.core.jpa.more.business.history.util.HistoryLogBeforeCommitWithDifferencesTask;
import fr.openwide.core.jpa.more.business.history.util.IHistoryDifferenceHandler;
import fr.openwide.core.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;

public abstract class AbstractHistoryLogServiceImpl<HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>,
				HLAIB extends AbstractHistoryLogAdditionalInformationBean>
		extends GenericEntityServiceImpl<Long, HL> implements IGenericHistoryLogService<HL, HET, HD, HLAIB> {

	@Autowired
	protected ITransactionSynchronizationTaskManagerService transactionSynchronizationTaskManagerService;

	@Autowired
	protected IHistoryValueService valueService;
	
	@Autowired
	public AbstractHistoryLogServiceImpl(IGenericHistoryLogDao<HL, HET> dao) {
		super(dao);
	}
	
	@Override
	public <T> HL logNow(Date date, HET eventType, List<HD> differences, T mainObject, HLAIB additionalInformation)
			throws ServiceException, SecurityServiceException {
		HL log = newHistoryLog(date, eventType, differences, mainObject, additionalInformation);

		log.setDifferences(differences);
		for (HD difference : differences) {
			difference.setParentLog(log);
		}
		
		create(log);
		return log;
	}
	
	protected abstract <T> HL newHistoryLog(Date date, HET eventType, List<HD> differences, T mainObject, HLAIB additionalInformation);

	protected abstract Supplier<HD> newHistoryDifferenceSupplier();

	protected void setAdditionalInformation(HL log, HLAIB additionalInformation) {
		if (additionalInformation != null) {
			setAdditionalInformation(log, additionalInformation.getSecondaryObjects());
		}
	}

	protected void setAdditionalInformation(HL log, Object[] objects) {
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
	public <T> void log(HET eventType, T mainObject, HLAIB additionalInformation) throws ServiceException,
			SecurityServiceException {
		transactionSynchronizationTaskManagerService.push(
				new HistoryLogBeforeCommitTask<T, HLAIB, HL, HET, HD>(new Date(), eventType, mainObject, additionalInformation)
		);
	}

	@Override
	public final <T> void logWithDifferences(HET eventType, T mainObject, HLAIB additionalInformation,
			IDifferenceService<T> differenceService) throws ServiceException, SecurityServiceException {
		logWithDifferences(eventType, mainObject, additionalInformation, differenceService.getMainDifferenceGenerator(),
				differenceService);
	}

	@Override
	@SafeVarargs
	public final <T> void logWithDifferences(HET eventType, T mainObject, HLAIB additionalInformation,
			IDifferenceService<T> differenceService, IHistoryDifferenceHandler<T, HL, HET, HD> ... differenceHandlers) throws ServiceException,
			SecurityServiceException {
		logWithDifferences(eventType, mainObject, additionalInformation, differenceService.getMainDifferenceGenerator(),
				differenceService, differenceHandlers);
	}
	
	@Override
	@SafeVarargs
	public final <T> void logWithDifferences(HET eventType, T mainObject, HLAIB additionalInformation,
			IDifferenceFromReferenceGenerator<T> differenceGenerator,
			IHistoryDifferenceGenerator<T> historyDifferenceGenerator, IHistoryDifferenceHandler<T, HL, HET, HD>... differenceHandlers) throws ServiceException, SecurityServiceException {
		transactionSynchronizationTaskManagerService.push(
				new HistoryLogBeforeCommitWithDifferencesTask<T, HLAIB, HL, HET, HD>(
						new Date(), eventType,
						mainObject, additionalInformation,
						newHistoryDifferenceSupplier(),
						differenceGenerator, historyDifferenceGenerator, differenceHandlers
				)
		);
	}

}
