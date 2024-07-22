package basicapp.back.business.history.search;

import basicapp.back.business.history.model.HistoryLog;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.EnumUtils;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

@Service
public class HistoryLogSearchQueryImpl implements IHistoryLogSearchQuery {

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

    return Search.session(entityManager)
        .search(HistoryLog.class)
        .where(predicateContributor(data))
        .sort(sortContributor(sorts))
        .fetchHits(offset, limit);
  }

  @Override
  public long size(HistoryLogSearchQueryData data) {
    return Search.session(entityManager)
        .search(HistoryLog.class)
        .where(predicateContributor(data))
        .fetchTotalHitCount();
  }

  private BiConsumer<
          ? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?>>
      predicateContributor(HistoryLogSearchQueryData data) {
    return (f, root) -> {
      root.add(f.matchAll());
      if (data.getDateMin() != null || data.getDateMax() != null) {
        root.add(f.range().field(HistoryLog.DATE).between(data.getDateMin(), data.getDateMax()));
      }
      if (data.getSubject() != null) {
        root.add(f.match().field(HistoryLog.SUBJECT_REFERENCE).matching(data.getSubject()));
      }
      if (data.getAllObjects() != null) {
        root.add(f.match().field(HistoryLog.ALL_OBJECTS_REFERENCE).matching(data.getAllObjects()));
      }
      if (data.getMainObject() != null) {
        root.add(f.match().field(HistoryLog.MAIN_OBJECT_REFERENCE).matching(data.getMainObject()));
      }
      if (data.getObject1() != null) {
        root.add(f.match().field(HistoryLog.OBJECT1_REFERENCE).matching(data.getObject1()));
      }
      if (data.getObject2() != null) {
        root.add(f.match().field(HistoryLog.OBJECT2_REFERENCE).matching(data.getObject2()));
      }
      if (data.getObject3() != null) {
        root.add(f.match().field(HistoryLog.OBJECT3_REFERENCE).matching(data.getObject3()));
      }
      if (data.getObject4() != null) {
        root.add(f.match().field(HistoryLog.OBJECT4_REFERENCE).matching(data.getObject4()));
      }
      if (data.getMandatoryDifferencesEventTypes() != null
          && !data.getMandatoryDifferencesEventTypes().isEmpty()) {
        Collection<HistoryEventType> allowedWithoutDifferencesEventTypes =
            EnumUtils.getEnumList(HistoryEventType.class);
        allowedWithoutDifferencesEventTypes.removeAll(data.getMandatoryDifferencesEventTypes());
        root.add(
            f.or()
                .with(
                    or -> {
                      or.add(
                          f.terms()
                              .field(HistoryLog.EVENT_TYPE)
                              .matchingAny(allowedWithoutDifferencesEventTypes));
                      or.add(f.match().field(HistoryLog.HAS_DIFFERENCES).matching(true));
                    }));
      }
    };
  }
}
