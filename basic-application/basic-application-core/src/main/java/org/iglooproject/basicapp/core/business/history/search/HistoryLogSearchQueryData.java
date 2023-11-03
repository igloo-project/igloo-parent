package org.iglooproject.basicapp.core.business.history.search;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class HistoryLogSearchQueryData implements ISearchQueryData<HistoryLog> {

	private User subject;

	private Instant dateMin;

	private Instant dateMax;

	private GenericEntity<Long, ? extends GenericEntity<Long, ?>> object;

	private GenericEntity<Long, ? extends GenericEntity<Long, ?>> object1;

	private GenericEntity<Long, ? extends GenericEntity<Long, ?>> object2;

	private GenericEntity<Long, ? extends GenericEntity<Long, ?>> object3;

	private GenericEntity<Long, ? extends GenericEntity<Long, ?>> object4;

	private Set<HistoryEventType> mandatoryDifferencesEventTypes = EnumSet.noneOf(HistoryEventType.class);

	public User getSubject() {
		return subject;
	}

	public void setSubject(User subject) {
		this.subject = subject;
	}

	public Instant getDateMin() {
		return dateMin;
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

	public GenericEntity<Long, ? extends GenericEntity<Long, ?>> getObject() {
		return object;
	}

	public void setObject(GenericEntity<Long, ? extends GenericEntity<Long, ?>> object) {
		this.object = object;
	}

	public GenericEntity<Long, ? extends GenericEntity<Long, ?>> getObject1() {
		return object1;
	}

	public void setObject1(GenericEntity<Long, ? extends GenericEntity<Long, ?>> object1) {
		this.object1 = object1;
	}

	public GenericEntity<Long, ? extends GenericEntity<Long, ?>> getObject2() {
		return object2;
	}

	public void setObject2(GenericEntity<Long, ? extends GenericEntity<Long, ?>> object2) {
		this.object2 = object2;
	}

	public GenericEntity<Long, ? extends GenericEntity<Long, ?>> getObject3() {
		return object3;
	}

	public void setObject3(GenericEntity<Long, ? extends GenericEntity<Long, ?>> object3) {
		this.object3 = object3;
	}

	public GenericEntity<Long, ? extends GenericEntity<Long, ?>> getObject4() {
		return object4;
	}

	public void setObject4(GenericEntity<Long, ? extends GenericEntity<Long, ?>> object4) {
		this.object4 = object4;
	}

	public Set<HistoryEventType> getMandatoryDifferencesEventTypes() {
		return Collections.unmodifiableSet(mandatoryDifferencesEventTypes);
	}

	public void setMandatoryDifferencesEventTypes(Set<HistoryEventType> mandatoryDifferencesEventTypes) {
		CollectionUtils.replaceAll(this.mandatoryDifferencesEventTypes, mandatoryDifferencesEventTypes);
	}

	public void addMandatoryDifferencesEventType(HistoryEventType eventType) {
		mandatoryDifferencesEventTypes.add(eventType);
	}

}
