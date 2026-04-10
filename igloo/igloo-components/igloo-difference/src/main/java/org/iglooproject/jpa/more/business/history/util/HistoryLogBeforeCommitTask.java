package org.iglooproject.jpa.more.business.history.util;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryDifference;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.atomic.IHistoryLogEventType;
import org.iglooproject.jpa.more.business.history.model.bean.AbstractHistoryLogAdditionalInformationBean;
import org.iglooproject.jpa.more.business.history.service.IGenericHistoryLogService;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import org.springframework.beans.factory.annotation.Autowired;

public class HistoryLogBeforeCommitTask<
        T,
        HLAIB extends AbstractHistoryLogAdditionalInformationBean,
        HL extends AbstractHistoryLog<HL, HLET, HD>,
        HLET extends Enum<HLET> & IHistoryLogEventType<HLET>,
        HD extends AbstractHistoryDifference<HD, HL>>
    implements ITransactionSynchronizationBeforeCommitTask {

  protected final Instant date;

  protected final HLET eventType;

  protected final T mainObject;

  protected final HLAIB additionalInformation;

  @Autowired private IGenericHistoryLogService<HL, HLET, HD, HLAIB> historyLogService;

  public HistoryLogBeforeCommitTask(
      Instant date, HLET eventType, T mainObject, HLAIB additionalInformation) {
    super();
    this.date = date;
    this.eventType = eventType;
    this.mainObject = mainObject;
    this.additionalInformation = additionalInformation;
  }

  /**
   * @return true, because this task requires its parameters to be still attached to the session
   *     when it executes.
   */
  @Override
  public boolean shouldRunBeforeClear() {
    return true;
  }

  public T getMainObject() {
    return mainObject;
  }

  @Override
  public void run() throws Exception {
    logNow();
  }

  protected final IGenericHistoryLogService<HL, HLET, HD, HLAIB> getHistoryLogService() {
    return historyLogService;
  }

  protected void logNow() throws ServiceException, SecurityServiceException {
    getHistoryLogService().logNow(date, eventType, List.of(), mainObject, additionalInformation);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof HistoryLogBeforeCommitTask<?, ?, ?, ?, ?> other)) {
      return false;
    }

    boolean mergeGroupMode =
        eventType != null
            && eventType.getMergeGroup() != null
            && other.eventType != null
            && other.eventType.getMergeGroup() != null;

    return Objects.equals(
            mergeGroupMode ? eventType.getMergeGroup() : eventType,
            mergeGroupMode ? other.eventType.getMergeGroup() : other.eventType)
        && Objects.equals(mainObject, other.mainObject);
  }

  @Override
  public int hashCode() {
    boolean mergeGroupMode = eventType != null && eventType.getMergeGroup() != null;
    return Objects.hash(mergeGroupMode ? eventType.getMergeGroup() : eventType, mainObject);
  }
}
