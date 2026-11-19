package org.iglooproject.jpa.more.business.history.service;

import java.time.Instant;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEventSummary;
import org.jspecify.annotations.Nullable;

public interface IGenericHistoryEventSummaryService<U> {

  void refresh(HistoryEventSummary event);

  void refresh(HistoryEventSummary event, Instant date);

  void refresh(HistoryEventSummary event, Instant date, @Nullable U subject);

  void refresh(HistoryEventSummary event, AbstractHistoryLog<?, ?, ?> historyLog);

  void clear(HistoryEventSummary event);

  boolean isSubject(HistoryEventSummary event, @Nullable U subject);
}
