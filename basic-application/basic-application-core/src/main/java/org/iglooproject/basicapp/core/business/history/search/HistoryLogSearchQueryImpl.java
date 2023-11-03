package org.iglooproject.basicapp.core.business.history.search;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.EnumUtils;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.business.history.model.atomic.HistoryEventType;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.more.business.history.search.HistoryLogSort;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class HistoryLogSearchQueryImpl implements IHistoryLogSearchQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Collection<HistoryLog> list(HistoryLogSearchQueryData data, Map<HistoryLogSort, ISort.SortOrder> sorts, Integer offset, Integer limit) {
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

	private BiConsumer<? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?>> predicateContributor(HistoryLogSearchQueryData data) {
		return (f, root) -> {
			root.add(f.matchAll());
			if (data.getSubject() != null) {
				root.add(f.match().field(HistoryLog.SUBJECT_REFERENCE).matching(data.getSubject()));
			}
			if (data.getDateMin() != null || data.getDateMax() != null) {
				root.add(f.range().field(HistoryLog.DATE).between(data.getDateMin(), data.getDateMax()));
			}
			if (data.getObject() != null) {
				root.add(f.match().field(HistoryLog.ALL_OBJECTS_REFERENCE).matching(GenericEntityReference.of(data.getObject())));
			}
			if (data.getObject1() != null) {
				root.add(f.match().field(HistoryLog.OBJECT1_REFERENCE).matching(GenericEntityReference.of(data.getObject1())));
			}
			if (data.getObject2() != null) {
				root.add(f.match().field(HistoryLog.OBJECT2_REFERENCE).matching(GenericEntityReference.of(data.getObject2())));
			}
			if (data.getObject3() != null) {
				root.add(f.match().field(HistoryLog.OBJECT3_REFERENCE).matching(GenericEntityReference.of(data.getObject3())));
			}
			if (data.getObject4() != null) {
				root.add(f.match().field(HistoryLog.OBJECT4_REFERENCE).matching(GenericEntityReference.of(data.getObject4())));
			}
			if (data.getMandatoryDifferencesEventTypes() != null && !data.getMandatoryDifferencesEventTypes().isEmpty()) {
				Collection<HistoryEventType> allowedWithoutDifferencesEventTypes = EnumUtils.getEnumList(HistoryEventType.class);
				allowedWithoutDifferencesEventTypes.removeAll(data.getMandatoryDifferencesEventTypes());
				root.add(
					f.or().with(or -> {
						or.add(f.terms().field(HistoryLog.EVENT_TYPE).matchingAny(allowedWithoutDifferencesEventTypes));
						or.add(f.match().field(HistoryLog.HAS_DIFFERENCES).matching(true));
					})
				);
			}
		};
	}

}
