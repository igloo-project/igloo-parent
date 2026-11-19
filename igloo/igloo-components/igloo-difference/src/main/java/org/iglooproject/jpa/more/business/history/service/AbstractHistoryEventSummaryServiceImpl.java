package org.iglooproject.jpa.more.business.history.service;

import java.time.Instant;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.iglooproject.jpa.util.HibernateUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractHistoryEventSummaryServiceImpl<U>
    implements IGenericHistoryEventSummaryService<U> {

  @Autowired private IHistoryValueService valueService;

  protected abstract @Nullable U getDefaultSubject();

  @Override
  public void refresh(HistoryEventSummary evenement) {
    refresh(evenement, Instant.now());
  }

  @Override
  public void refresh(HistoryEventSummary evenement, Instant date) {
    refresh(evenement, date, getDefaultSubject());
  }

  @Override
  public void refresh(HistoryEventSummary evenement, Instant date, @Nullable U subject) {
    evenement.setDate(date);
    evenement.setSubject(valueService.createHistoryEventValue(HibernateUtils.unwrap(subject)));
  }

  @Override
  public void refresh(HistoryEventSummary evenement, AbstractHistoryLog<?, ?, ?> historyLog) {
    evenement.setDate(historyLog.getDate());
    evenement.setSubject(historyLog.getSubject());
  }

  @Override
  public void clear(HistoryEventSummary event) {
    event.setDate(null);
    event.setSubject(null);
  }

  @Override
  public boolean isSubject(HistoryEventSummary event, @Nullable U subject) {
    return valueService.matches(event.getSubject(), subject).or(false);
  }
}
