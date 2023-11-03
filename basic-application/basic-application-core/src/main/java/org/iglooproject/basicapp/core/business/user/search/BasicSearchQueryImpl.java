package org.iglooproject.basicapp.core.business.user.search;

import static org.iglooproject.jpa.more.search.query.HibernateSearchUtils.wildcardTokensOr;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesCollector;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class BasicSearchQueryImpl implements IBasicUserSearchQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Collection<BasicUser> list(BasicUserSearchQueryData data, Map<BasicUserSort, ISort.SortOrder> sorts, Integer offset, Integer limit) {
		if (!checkLimit(limit)) {
			return List.of();
		}
		
		return Search.session(entityManager)
			.search(BasicUser.class)
			.where(predicateContributor(data))
			.sort(sortContributor(sorts))
			.fetchHits(offset, limit);
	}

	@Override
	public long size(BasicUserSearchQueryData data) {
		return Search.session(entityManager)
			.search(BasicUser.class)
			.where(predicateContributor(data))
			.fetchTotalHitCount();
	}

	private BiConsumer<? super SearchPredicateFactory, ? super SimpleBooleanPredicateClausesCollector<?>> predicateContributor(BasicUserSearchQueryData data) {
		return (f, root) -> {
			root.add(f.matchAll());
			if (data.getTerm() != null) {
				root.add(f.or().with(or -> {
					or.add(f.simpleQueryString().field(User.LAST_NAME).matching(wildcardTokensOr(data.getTerm())));
					or.add(f.simpleQueryString().field(User.FIRST_NAME).matching(wildcardTokensOr(data.getTerm())));
					or.add(f.simpleQueryString().field(User.USERNAME).matching(wildcardTokensOr(data.getTerm())));
				}));
			}
			if (data.getGroup() != null) {
				root.add(f.terms().field(User.GROUPS).matchingAny(data.getGroup()));
			}
			if (data.getEnabledFilter() != null && !Objects.equals(data.getEnabledFilter(), EnabledFilter.ALL)) {
				boolean active = switch(data.getEnabledFilter()) {
					case DISABLED_ONLY -> false;
					case ENABLED_ONLY -> true;
					default -> throw new IllegalSwitchValueException(data.getEnabledFilter());
				};
				root.add(f.match().field(User.ENABLED).matching(active));
			}
		};
	}

}
