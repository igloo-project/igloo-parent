package org.iglooproject.basicapp.core.business.history.model;

import java.time.Instant;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Bindable
@Cacheable
@Indexed
public class HistoryLog extends AbstractHistoryLog<HistoryLog, HistoryEventType, HistoryDifference> {

	private static final long serialVersionUID = -8557932643510393694L;

	public HistoryLog() {
		// nothing to do
	}

	public HistoryLog(Instant date, HistoryEventType eventType, HistoryValue mainObject) {
		super(date, eventType, mainObject);
	}

}
