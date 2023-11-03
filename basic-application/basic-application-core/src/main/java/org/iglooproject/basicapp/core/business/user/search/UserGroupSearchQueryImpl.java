package org.iglooproject.basicapp.core.business.user.search;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UserGroupSearchQueryImpl implements IUserGroupSearchQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Collection<UserGroup> list(UserGroupSearchQueryData data, Map<UserGroupSort, ISort.SortOrder> sorts, Integer offset, Integer limit) {
		if (!checkLimit(limit)) {
			return List.of();
		}
		
		return Search.session(entityManager)
			.search(UserGroup.class)
			.where(predicateContributor(data))
			.sort(sortContributor(sorts))
			.fetchHits(offset, limit);
	}

	@Override
	public long size(UserGroupSearchQueryData data) {
		return Search.session(entityManager)
			.search(UserGroup.class)
			.where(predicateContributor(data))
			.fetchTotalHitCount();
	}

	private BiConsumer<? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?>> predicateContributor(UserGroupSearchQueryData data) {
		return (f, root) -> {
			root.add(f.matchAll());
		};
	}

}
