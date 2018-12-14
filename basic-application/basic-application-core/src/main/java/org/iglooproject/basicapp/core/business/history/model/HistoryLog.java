package org.iglooproject.basicapp.core.business.history.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.annotations.TypeDef;
import org.hibernate.search.annotations.Indexed;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.jpa.more.business.history.hibernate.FieldPathType;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;

@Entity
@Bindable
@Cacheable
@Indexed
@TypeDef(defaultForType = FieldPath.class, typeClass = FieldPathType.class)
public class HistoryLog extends AbstractHistoryLog<HistoryLog, HistoryEventType, HistoryDifference> {

	private static final long serialVersionUID = -8557932643510393694L;

	public HistoryLog() {
		// nothing to do
	}

	public HistoryLog(Date date, HistoryEventType eventType, HistoryValue mainObject) {
		super(date, eventType, mainObject);
	}

}
