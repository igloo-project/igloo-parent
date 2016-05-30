package fr.openwide.core.basicapp.core.business.history.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.annotations.TypeDef;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.basicapp.core.business.history.model.atomic.HistoryEventType;
import fr.openwide.core.commons.util.fieldpath.FieldPath;
import fr.openwide.core.jpa.more.business.history.hibernate.FieldPathType;
import fr.openwide.core.jpa.more.business.history.model.AbstractHistoryLog;
import fr.openwide.core.jpa.more.business.history.model.embeddable.HistoryValue;

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
