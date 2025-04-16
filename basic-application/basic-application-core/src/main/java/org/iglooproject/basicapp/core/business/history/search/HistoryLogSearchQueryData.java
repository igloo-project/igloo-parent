package org.iglooproject.basicapp.core.business.history.search;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;
import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.business.search.query.ISearchQueryData;

@Bindable
public class HistoryLogSearchQueryData implements ISearchQueryData<HistoryLog> {

  private Date dateMin;

  private Date dateMax;

  private final Set<HistoryEventType> eventTypes = EnumSet.noneOf(HistoryEventType.class);

  private User subject;

  private HistoryEntityReference allObjects;

  private HistoryEntityReference mainObject;

  private HistoryEntityReference object1;

  private HistoryEntityReference object2;

  private HistoryEntityReference object3;

  private HistoryEntityReference object4;

  private final Collection<HistoryEventType> mandatoryDifferencesEventTypes =
      EnumSet.noneOf(HistoryEventType.class);

  public void setDateMin(Date dateMin) {
    this.dateMin = dateMin;
  }

  public Date getDateMax() {
    return dateMax;
  }

  public void setDateMax(Date dateMax) {
    this.dateMax = dateMax;
  }

  public Set<HistoryEventType> getEventTypes() {
    return Collections.unmodifiableSet(eventTypes);
  }

  public void setEventTypes(Set<HistoryEventType> eventTypes) {
    CollectionUtils.replaceAll(this.eventTypes, eventTypes);
  }

  public void addEventType(HistoryEventType eventType) {
    eventTypes.add(eventType);
  }

  public User getSubject() {
    return subject;
  }

  public void setSubject(User subject) {
    this.subject = subject;
  }

  public Date getDateMin() {
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

  public void setMandatoryDifferencesEventTypes(
      Collection<HistoryEventType> mandatoryDifferencesEventTypes) {
    CollectionUtils.replaceAll(this.mandatoryDifferencesEventTypes, mandatoryDifferencesEventTypes);
  }

  public void addMandatoryDifferencesEventType(HistoryEventType eventType) {
    mandatoryDifferencesEventTypes.add(eventType);
  }
}
