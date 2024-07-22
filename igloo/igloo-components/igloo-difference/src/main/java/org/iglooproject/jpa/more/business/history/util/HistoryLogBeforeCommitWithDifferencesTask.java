package org.iglooproject.jpa.more.business.history.util;

import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.util.List;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;

public class HistoryLogBeforeCommitWithDifferencesTask<
        T,
        HLAIB extends AbstractHistoryLogAdditionalInformationBean,
        HL extends AbstractHistoryLog<HL, HET, HD>,
        HET extends Enum<HET>,
        HD extends AbstractHistoryDifference<HD, HL>>
    extends HistoryLogBeforeCommitTask<T, HLAIB, HL, HET, HD> {

  private Supplier2<HD> historyDifferenceSupplier;
  private IDifferenceFromReferenceGenerator<T> differenceGenerator;
  private IHistoryDifferenceGenerator<T> historyDifferenceGenerator;
  private List<IHistoryDifferenceHandler<? super T, ? super HL>> differenceHandlers;

  @SafeVarargs
  public HistoryLogBeforeCommitWithDifferencesTask(
      Instant date,
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      Supplier2<HD> historyDifferenceSupplier,
      IDifferenceFromReferenceGenerator<T> differenceGenerator,
      IHistoryDifferenceGenerator<T> historyDifferenceGenerator,
      IHistoryDifferenceHandler<? super T, ? super HL>... differenceHandlers) {
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

  private void logNow(T mainObject, Difference<T> difference)
      throws ServiceException, SecurityServiceException {
    List<HD> historyDifferences =
        historyDifferenceGenerator.toHistoryDifferences(historyDifferenceSupplier, difference);
    HL historyLog =
        getHistoryLogService()
            .logNow(date, eventType, historyDifferences, mainObject, additionalInformation);

    for (IHistoryDifferenceHandler<? super T, ? super HL> handler : differenceHandlers) {
      handler.handle(mainObject, difference, historyLog);
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
    return super
        .hashCode(); // NOSONAR : Sans cette implémentation, on a un avertissement de Sonar parce
    // que equals() est implémenté mais pas HashCode.
  }
}
