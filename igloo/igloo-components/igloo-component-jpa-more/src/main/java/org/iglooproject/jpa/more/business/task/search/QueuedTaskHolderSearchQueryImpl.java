package org.iglooproject.jpa.more.business.task.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.iglooproject.jpa.more.business.task.model.QueuedTaskHolder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class QueuedTaskHolderSearchQueryImpl implements IQueuedTaskHolderSearchQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<QueuedTaskHolder> list(QueuedTaskHolderSearchQueryData data, Map<QueuedTaskHolderSort, SortOrder> sorts, Integer offset, Integer limit) {
		if (!checkLimit(limit)) {
			return List.of();
		}
		
		return Search.session(entityManager)
			.search(QueuedTaskHolder.class)
			.where(predicateContributor(data))
			.sort(sortContributor(sorts))
			.fetchHits(offset, limit);
	}

	@Override
	public long size(QueuedTaskHolderSearchQueryData data) {
		return Search.session(entityManager)
			.search(QueuedTaskHolder.class)
			.where(predicateContributor(data))
			.fetchTotalHitCount();
	}

	private BiConsumer<? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?>> predicateContributor(QueuedTaskHolderSearchQueryData data) {
		return (f, root) -> {
			root.add(f.matchAll());
			if (data.getName() != null) {
				root.add(f.simpleQueryString().field(QueuedTaskHolder.NAME).matching(wildcardTokensOr(data.getName())));
			}
			if (data.getStatuses() != null && !data.getStatuses().isEmpty()) {
				root.add(f.terms().field(QueuedTaskHolder.STATUS).matchingAny(data.getStatuses()));
			}
			if (data.getResults() != null && !data.getResults().isEmpty()) {
				root.add(f.terms().field(QueuedTaskHolder.RESULT).matchingAny(data.getResults()));
			}
			if (data.getTaskTypes() != null && !data.getTaskTypes().isEmpty()) {
				root.add(f.terms().field(QueuedTaskHolder.TASK_TYPE).matchingAny(data.getTaskTypes()));
			}
			if (data.getQueueIds() != null && !data.getQueueIds().isEmpty()) {
				root.add(f.terms().field(QueuedTaskHolder.QUEUE_ID).matchingAny(data.getQueueIds()));
			}
			if (data.getCreationDate() != null) {
				root.add(f.range().field(QueuedTaskHolder.CREATION_DATE).atMost(data.getCreationDate()));
			}
			if (data.getStartDate() != null) {
				root.add(f.range().field(QueuedTaskHolder.START_DATE).atMost(data.getStartDate()));
			}
			if (data.getEndDate() != null) {
				root.add(f.range().field(QueuedTaskHolder.END_DATE).atMost(data.getEndDate()));
			}
		};
	}

}
