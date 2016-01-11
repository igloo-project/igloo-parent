package fr.openwide.core.jpa.more.business.history.util;

import java.util.Date;
import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.difference.model.Difference;
import fr.openwide.core.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import fr.openwide.core.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryDifference;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;

public class HistoryLogBeforeCommitWithDifferencesTask<T,
				HLAIB extends AbstractHistoryLogAdditionalInformationBean,
				HL extends AbstractHistoryLog<HL, HET, HD>,
				HET extends Enum<HET>,
				HD extends AbstractHistoryDifference<HD, HL>>
		extends HistoryLogBeforeCommitTask<T, HLAIB, HL, HET, HD> {

	private Supplier<HD> historyDifferenceSupplier;
	private IDifferenceFromReferenceGenerator<T> differenceGenerator;
	private IHistoryDifferenceGenerator<T> historyDifferenceGenerator;
	private List<IDifferenceHandler<T>> differenceHandlers;
	
	@SafeVarargs
	public HistoryLogBeforeCommitWithDifferencesTask(
			Date date, HET eventType,
			T mainObject, HLAIB additionalInformation,
			Supplier<HD> historyDifferenceSupplier,
			IDifferenceFromReferenceGenerator<T> differenceGenerator,
			IHistoryDifferenceGenerator<T> historyDifferenceGenerator,
			IDifferenceHandler<T> ... differenceHandlers) {
		super(date, eventType, mainObject, additionalInformation);
		this.historyDifferenceSupplier = historyDifferenceSupplier;
		this.differenceGenerator = differenceGenerator;
		this.historyDifferenceGenerator = historyDifferenceGenerator;
		this.differenceHandlers = ImmutableList.copyOf(differenceHandlers);
	}

	@Override
	protected void logNow() throws ServiceException, SecurityServiceException {
		Difference<T> difference = differenceGenerator.diffFromReference(mainObject);
		logNow(mainObject, difference);
	}
	
	public IDifferenceFromReferenceGenerator<T> getDifferenceGenerator() {
		return differenceGenerator;
	}
	
	public void logNow(T mainObjectReference) throws ServiceException, SecurityServiceException {
		Difference<T> difference = differenceGenerator.diff(mainObject, mainObjectReference);
		logNow(mainObject, difference);
	}
	
	private void logNow(T mainObject, Difference<T> difference) throws ServiceException, SecurityServiceException {
		List<HD> historyDifferences = historyDifferenceGenerator.toHistoryDifferences(historyDifferenceSupplier, difference);
		getHistoryLogService().logNow(date, eventType, historyDifferences, mainObject, additionalInformation);
		
		for (IDifferenceHandler<T> handler : differenceHandlers) {
			handler.handle(mainObject, difference, date);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HistoryLogBeforeCommitWithDifferencesTask) {
			return super.equals(obj);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode(); // NOSONAR : Sans cette implémentation, on a un avertissement de Sonar parce que equals() est implémenté mais pas HashCode.
	}
}