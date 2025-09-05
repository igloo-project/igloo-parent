package org.iglooproject.jpa.more.business.history.service;

import java.time.Instant;
import java.util.List;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.more.business.difference.service.IDifferenceService;
import org.iglooproject.jpa.more.business.difference.util.IDifferenceFromReferenceGenerator;
import org.iglooproject.jpa.more.business.difference.util.IHistoryDifferenceGenerator;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import org.iglooproject.jpa.more.business.history.util.HistoryLogBeforeCommitTask;
import org.iglooproject.jpa.more.business.history.util.HistoryLogBeforeCommitWithDifferencesTask;
import org.iglooproject.jpa.more.business.history.util.IHistoryDifferenceHandler;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * abstract class without adhesion with GenericEntityService and therefore without adhesion with the
 * ORM API used
 */
public abstract class AbstractHistoryLogJpaServiceImpl<
        HL extends AbstractHistoryLog<HL, HET, HD>,
        HET extends Enum<HET>,
        HD extends AbstractHistoryDifference<HD, HL>,
        HLAIB extends AbstractHistoryLogAdditionalInformationBean>
    implements IGenericHistoryLogService<HL, HET, HD, HLAIB> {

  @Autowired
  protected ITransactionSynchronizationTaskManagerService
      transactionSynchronizationTaskManagerService;

  @Autowired protected IHistoryValueService valueService;

  @Override
  @Transactional
  public <T> HL logNow(
      Instant date,
      HET eventType,
      List<HD> differences,
      T mainObject,
      HLAIB additionalInformation) {
    HL log = newHistoryLog(date, eventType, differences, mainObject, additionalInformation);

    log.setDifferences(differences);
    for (HD difference : differences) {
      difference.setParentLog(log);
    }
    saveHistoryLog(log);
    return log;
  }

  protected abstract <T> HL newHistoryLog(
      Instant date, HET eventType, List<HD> differences, T mainObject, HLAIB additionalInformation);

  protected abstract <T> void saveHistoryLog(HL historyLog);

  protected abstract Supplier2<HD> newHistoryDifferenceSupplier();

  protected void setAdditionalInformation(HL log, HLAIB additionalInformation) {
    if (additionalInformation != null) {
      setAdditionalInformation(log, additionalInformation.getSecondaryObjects());
    }
  }

  protected void setAdditionalInformation(HL log, Object[] objects) {
    if (objects.length > 4) {
      throw new IllegalArgumentException(
          String.format("Too many arguments (%d, expected %d or less)", objects.length, 4));
    }
    if (objects.length >= 1) {
      log.setObject1(valueService.createHistoryValue(objects[0]));
    }
    if (objects.length >= 2) {
      log.setObject2(valueService.createHistoryValue(objects[1]));
    }
    if (objects.length >= 3) {
      log.setObject3(valueService.createHistoryValue(objects[2]));
    }
    if (objects.length >= 4) {
      log.setObject4(valueService.createHistoryValue(objects[3]));
    }
  }

  @Override
  @Transactional
  public <T> void log(HET eventType, T mainObject, HLAIB additionalInformation) {
    transactionSynchronizationTaskManagerService.push(
        new HistoryLogBeforeCommitTask<T, HLAIB, HL, HET, HD>(
            Instant.now(), eventType, mainObject, additionalInformation));
  }

  @Override
  public final <T> void logWithDifferences(
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      IDifferenceService<T> differenceService) {
    logWithDifferences(
        eventType,
        mainObject,
        additionalInformation,
        differenceService.getMainDifferenceGenerator(),
        differenceService);
  }

  @Override
  @SafeVarargs
  public final <T> void logWithDifferences(
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      IDifferenceService<T> differenceService,
      IHistoryDifferenceHandler<? super T, ? super HL>... differenceHandlers) {
    logWithDifferences(
        eventType,
        mainObject,
        additionalInformation,
        differenceService.getMainDifferenceGenerator(),
        differenceService,
        differenceHandlers);
  }

  @Override
  @SafeVarargs
  public final <T> void logWithDifferences(
      HET eventType,
      T mainObject,
      HLAIB additionalInformation,
      IDifferenceFromReferenceGenerator<T> differenceGenerator,
      IHistoryDifferenceGenerator<T> historyDifferenceGenerator,
      IHistoryDifferenceHandler<? super T, ? super HL>... differenceHandlers) {
    transactionSynchronizationTaskManagerService.push(
        new HistoryLogBeforeCommitWithDifferencesTask<T, HLAIB, HL, HET, HD>(
            Instant.now(),
            eventType,
            mainObject,
            additionalInformation,
            newHistoryDifferenceSupplier(),
            differenceGenerator,
            historyDifferenceGenerator,
            differenceHandlers));
  }
}
