package org.iglooproject.jpa.more.business.search.query;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.SortUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;

public abstract class AbstractJpaSearchQuery<T, S extends ISort<OrderSpecifier<?>>> extends AbstractSearchQuery<T, S> /* NOT Serializable */ {
	
	private final EntityPath<T> entityPath;
	
	private JPAQuery<T> jpaQuery;
	private JPAQuery<T> finalJpaQuery;
	
	@SafeVarargs
	protected AbstractJpaSearchQuery(EntityPath<T> entityPath, S ... defaultSorts) {
		super(defaultSorts);
		this.entityPath = entityPath;
	}
	
	@PostConstruct
	private void init() {
		jpaQuery = new JPAQuery<T>(entityManager)
				.select(entityPath)
				.from(entityPath);
	}
	
	public <P> void innerJoin(CollectionExpression<?,P> target, Path<P> alias) {
		jpaQuery.innerJoin(target, alias);
	}

	// Junction appender
	// 	>	Must
	protected void must(BooleanExpression booleanExpression) {
		if (booleanExpression != null) {
			jpaQuery.where(booleanExpression);
		}
	}
	
	protected void mustIfNotNull(JPAQuery<T> jpaQuery, BooleanExpression ... booleanExpressions) {
		for (BooleanExpression booleanExpression : booleanExpressions) {
			if (booleanExpression != null) {
				jpaQuery.where(booleanExpression);
			}
		}
	}
	
	// 	>	Should
	protected void shouldIfNotNull(BooleanExpression ... booleanExpressions) {
		BooleanExpression condition = null;
		for (BooleanExpression booleanExpression : booleanExpressions) {
			if (condition == null) {
				condition = booleanExpression;
			} else {
				condition = condition.or(booleanExpression);
			}
		}
		if (condition != null) {
			jpaQuery.where(condition);
		}
	}
	
	// List and count
	/**
	 * Allow to add filter before generating the full text query.<br />
	 * Sample:
	 * <ul>
	 * 	<li>must(matchIfGiven(qCompany.manager.organization, organization))</li>
	 * 	<li>must(matchIfGiven(qCompany.status, CompanyStatus.ACTIVE))</li>
	 * </ul>
	 */
	protected void addFilterBeforeFinalizeQuery() {
		// Nothing
	}
	
	private JPAQuery<T> getFinalQuery() {
		if (finalJpaQuery == null) {
			addFilterBeforeFinalizeQuery();
			
			for (OrderSpecifier<?> orderSpecifier : SortUtils.getOrderSpecifierWithDefaults(sortMap, defaultSorts)) {
				jpaQuery.orderBy(orderSpecifier);
			}
			
			finalJpaQuery = jpaQuery;
		}
		return finalJpaQuery;
	}
	
	@Override
	@Transactional(readOnly = true)
	public final List<T> fullList() {
		return getQueryList(null, null).fetch();
	}
	
	@Override
	@Transactional(readOnly = true)
	public final List<T> list(long offset, long limit) {
		if (limit == 0) {
			return ImmutableList.of();
		}
		return getQueryList(offset, limit).fetch();
	}

	protected JPAQuery<T> getQueryList(Long offset, Long limit) {
		JPAQuery<T> finalQuery = getFinalQuery();
		
		// Handle multiple calls to getQueryList()
		finalQuery.restrict(QueryModifiers.EMPTY);
		
		if (offset != null) {
			finalQuery.offset(offset);
		}
		if (limit != null) {
			finalQuery.limit(limit);
		}
		
		return finalQuery;
	}
	
	@Override
	@Transactional(readOnly = true)
	public long count() {
		return getFinalQuery().fetchCount();
	}
	
	// Query factory
	// 	>	Match if given
	protected <P extends Comparable<?>> BooleanExpression matchIfGiven(SimpleExpression<P> simpleExpression, P value) {
		if (value != null) {
			return simpleExpression.eq(value);
		}
		return null;
	}
	
	// 	>	Contains if given
	protected <C extends Collection<E>, E, Q extends SimpleExpression<? super E>> BooleanExpression containsIfGiven(CollectionPathBase<C, E, Q> collectionPath, E value) {
		if (value != null) {
			return collectionPath.contains(value);
		}
		return null;
	}
	
	// 	>	Match one if given
	protected <P extends Comparable<?>> BooleanExpression matchOneIfGiven(SimpleExpression<P> simpleExpression, Collection<? extends P> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			return simpleExpression.in(possibleValues);
		}
		return null;
	}
	
	// 	>	Match all if given
	protected <P extends Comparable<?>> BooleanExpression matchAllIfGiven(SimpleExpression<P> simpleExpression, Collection<? extends P> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			BooleanExpression rootExpression = null;
			for (P possibleValue : possibleValues) {
				if (rootExpression == null) {
					rootExpression = simpleExpression.eq(possibleValue);
				} else {
					rootExpression = rootExpression.and(simpleExpression.eq(possibleValue));
				}
			}
			return rootExpression;
		}
		return null;
	}
	
	// 	>	Match range if given
	protected <P extends Comparable<?>> BooleanExpression matchRangeIfGiven(ComparableExpression<P> comparableExpression, P minValue, P maxValue) {
		if (minValue != null && maxValue != null) {
			return comparableExpression.between(minValue, maxValue);
		} else if (minValue != null) {
			return comparableExpression.goe(minValue);
		} else if (maxValue != null) {
			return comparableExpression.loe(maxValue);
		}
		return null;
	}
	
	protected <P extends Comparable<?>> BooleanExpression matchExclusiveRangeIfGiven(ComparableExpression<P> comparableExpression, P minValue, P maxValue) {
		if (minValue != null && maxValue != null) {
			return comparableExpression.gt(minValue).and(comparableExpression.lt(maxValue));
		} else if (minValue != null) {
			return comparableExpression.gt(minValue);
		} else if (maxValue != null) {
			return comparableExpression.lt(maxValue);
		}
		return null;
	}
}