package org.iglooproject.basicapp.core.business.history.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.EnumUtils;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.QHistoryDifference;
import org.iglooproject.basicapp.core.business.history.model.QHistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryEntityReference;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

@Service
public class HistoryLogSearchQueryImpl implements IHistoryLogSearchQuery {

  private static final QHistoryLog qHistoryLog = QHistoryLog.historyLog;
  private static final QHistoryDifference qHistoryDifference = QHistoryDifference.historyDifference;

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Collection<HistoryLog> list(
      HistoryLogSearchQueryData data,
      Map<HistoryLogSort, ISort.SortOrder> sorts,
      Integer offset,
      Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    JPAQuery<HistoryLog> query =
        new JPAQuery<>(entityManager).select(qHistoryLog).from(qHistoryLog);

    joinContributor(query, data);
    predicateContributor(query, data);
    sortContributor(query, sorts);
    hitsContributor(query, offset, limit);

    return query.distinct().fetch();
  }

  @Override
  public long size(HistoryLogSearchQueryData data) {
    JPAQuery<Long> query =
        new JPAQuery<>(entityManager).select(qHistoryLog.countDistinct()).from(qHistoryLog);

    joinContributor(query, data);
    predicateContributor(query, data);

    return Optional.ofNullable(query.fetchOne()).orElse(0L);
  }

  private void joinContributor(JPAQuery<?> query, HistoryLogSearchQueryData data) {
    if (data.getMandatoryDifferencesEventTypes() != null
        && !data.getMandatoryDifferencesEventTypes().isEmpty()) {
      query.leftJoin(qHistoryLog.differences, qHistoryDifference);
    }
  }

  private void predicateContributor(JPAQuery<?> query, HistoryLogSearchQueryData data) {
    if (data.getDateMin() != null || data.getDateMax() != null) {
      query.where(qHistoryLog.date.between(data.getDateMin(), data.getDateMax()));
    }
    if (data.getEventTypes() != null && !data.getEventTypes().isEmpty()) {
      query.where(qHistoryLog.eventType.in(data.getEventTypes()));
    }
    if (data.getSubject() != null) {
      query.where(qHistoryLog.subject.reference.eq(new HistoryEntityReference(data.getSubject())));
    }
    if (data.getAllObjects() != null) {
      BooleanBuilder condition = new BooleanBuilder();
      condition.or(qHistoryLog.mainObject.reference.eq(data.getAllObjects()));
      condition.or(qHistoryLog.object1.reference.eq(data.getAllObjects()));
      condition.or(qHistoryLog.object2.reference.eq(data.getAllObjects()));
      condition.or(qHistoryLog.object3.reference.eq(data.getAllObjects()));
      condition.or(qHistoryLog.object4.reference.eq(data.getAllObjects()));
      query.where(condition);
    }
    if (data.getMainObject() != null) {
      query.where(qHistoryLog.mainObject.reference.eq(data.getMainObject()));
    }
    if (data.getObject1() != null) {
      query.where(qHistoryLog.object1.reference.eq(data.getObject1()));
    }
    if (data.getObject2() != null) {
      query.where(qHistoryLog.object2.reference.eq(data.getObject2()));
    }
    if (data.getObject3() != null) {
      query.where(qHistoryLog.object3.reference.eq(data.getObject3()));
    }
    if (data.getObject4() != null) {
      query.where(qHistoryLog.object4.reference.eq(data.getObject4()));
    }
    if (data.getMandatoryDifferencesEventTypes() != null
        && !data.getMandatoryDifferencesEventTypes().isEmpty()) {
      Collection<HistoryEventType> allowedWithoutDifferencesEventTypes =
          EnumUtils.getEnumList(HistoryEventType.class);
      allowedWithoutDifferencesEventTypes.removeAll(data.getMandatoryDifferencesEventTypes());
      BooleanBuilder condition = new BooleanBuilder();
      condition.or(qHistoryLog.eventType.in(allowedWithoutDifferencesEventTypes));
      condition.or(qHistoryDifference.isNotNull());
      query.where(condition);
    }
  }
}
