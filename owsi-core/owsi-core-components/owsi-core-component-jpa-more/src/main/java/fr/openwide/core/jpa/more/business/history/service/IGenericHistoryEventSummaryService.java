package fr.openwide.core.jpa.more.business.history.service;

import java.util.Date;

import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryEventSummary;

public interface IGenericHistoryEventSummaryService<U> {

	void refresh(HistoryEventSummary event);
	
	void refresh(HistoryEventSummary event, Date date);

	void refresh(HistoryEventSummary event, Date date, U subject);

	void refresh(HistoryEventSummary event, AbstractHistoryLog<?, ?, ?> historyLog);
	
	void clear(HistoryEventSummary event);
	
	boolean isSubject(HistoryEventSummary event, U subject);

}
