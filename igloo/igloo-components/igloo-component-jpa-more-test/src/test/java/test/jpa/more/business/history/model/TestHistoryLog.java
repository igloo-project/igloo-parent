package test.jpa.more.business.history.model;

import java.util.Date;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

import test.jpa.more.business.history.model.atomic.TestHistoryEventType;

@Entity
@Bindable
@Cacheable
@Indexed
public class TestHistoryLog extends AbstractHistoryLog<TestHistoryLog, TestHistoryEventType, TestHistoryDifference> {

	private static final long serialVersionUID = -8557932643510393694L;
	
	public TestHistoryLog() {
		// nothing to do
	}
	
	public TestHistoryLog(Date date, TestHistoryEventType eventType, HistoryValue mainObject) {
		super(date, eventType, mainObject);
	}


}
