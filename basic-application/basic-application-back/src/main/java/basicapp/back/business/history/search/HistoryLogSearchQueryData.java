package basicapp.back.business.history.search;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import org.bindgen.Bindable;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.user.model.User;

@Bindable
public class HistoryLogSearchQueryData implements ISearchQueryData<HistoryLog> {

	private Instant dateMin;

	private Instant dateMax;

	private User subject;

	private HistoryEntityReference allObjects;

	private HistoryEntityReference mainObject;

	private HistoryEntityReference object1;

	private HistoryEntityReference object2;

	private HistoryEntityReference object3;

	private HistoryEntityReference object4;

	private Collection<HistoryEventType> mandatoryDifferencesEventTypes = EnumSet.noneOf(HistoryEventType.class);

	public User getSubject() {
		return subject;
	}

	public void setDateMin(Instant dateMin) {
		this.dateMin = dateMin;
	}

	public Instant getDateMax() {
		return dateMax;
	}

	public void setDateMax(Instant dateMax) {
		this.dateMax = dateMax;
	}

	public void setSubject(User subject) {
		this.subject = subject;
	}

	public Instant getDateMin() {
		return dateMin;
	}

	public HistoryEntityReference getAllObjects() {
		return allObjects;
	}

	public void setAllObjects(HistoryEntityReference allObjects) {
		this.allObjects = allObjects;
	}

	public HistoryEntityReference getMainObject() {
		return mainObject;
	}

	public void setMainObject(HistoryEntityReference mainObject) {
		this.mainObject = mainObject;
	}

	public HistoryEntityReference getObject1() {
		return object1;
	}

	public void setObject1(HistoryEntityReference object1) {
		this.object1 = object1;
	}

	public HistoryEntityReference getObject2() {
		return object2;
	}

	public void setObject2(HistoryEntityReference object2) {
		this.object2 = object2;
	}

	public HistoryEntityReference getObject3() {
		return object3;
	}

	public void setObject3(HistoryEntityReference object3) {
		this.object3 = object3;
	}

	public HistoryEntityReference getObject4() {
		return object4;
	}

	public void setObject4(HistoryEntityReference object4) {
		this.object4 = object4;
	}

	public Collection<HistoryEventType> getMandatoryDifferencesEventTypes() {
		return Collections.unmodifiableCollection(mandatoryDifferencesEventTypes);
	}

	public void setMandatoryDifferencesEventTypes(Collection<HistoryEventType> mandatoryDifferencesEventTypes) {
		CollectionUtils.replaceAll(this.mandatoryDifferencesEventTypes, mandatoryDifferencesEventTypes);
	}

	public void addMandatoryDifferencesEventType(HistoryEventType eventType) {
		mandatoryDifferencesEventTypes.add(eventType);
	}

}
