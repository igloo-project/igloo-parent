package fr.openwide.core.jpa.more.business.search.query;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CollectionPath;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.jpa.more.business.sort.SortUtils;

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
		jpaQuery = new JPAQuery<T>(entityManager).from(entityPath);
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
			if (booleanExpressions != null) {
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
	public List<T> list(long offset, long limit) {
		return getFinalQuery().offset(offset).limit(limit).list(entityPath);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long count() {
		return getFinalQuery().fetchCount();
	}
	
	// Query factory
	// 	>	Match if given
	protected <P extends Comparable<?>> BooleanExpression matchIfGiven(ComparablePath<P> comparablePath, P value) {
		if (value != null) {
			return comparablePath.eq(value);
		}
		return null;
	}
	
	// 	>	Contains if given
	protected <E, Q extends SimpleExpression<? super E>> BooleanExpression containsIfGiven(CollectionPath<E, Q> collectionPath, E value) {
		if (value != null) {
			return collectionPath.contains(value);
		}
		return null;
	}
	
	// 	>	Match one if given
	protected <P extends Comparable<?>> BooleanExpression matchOneIfGiven(ComparablePath<P> comparablePath, Collection<? extends P> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			return comparablePath.in(possibleValues);
		}
		return null;
	}
	
	// 	>	Match all if given
	protected <P extends Comparable<?>> BooleanExpression matchAllIfGiven(ComparablePath<P> comparablePath, Collection<? extends P> possibleValues) {
		if (possibleValues != null && !possibleValues.isEmpty()) {
			BooleanExpression rootExpression = null;
			for (P possibleValue : possibleValues) {
				if (rootExpression == null) {
					rootExpression = comparablePath.eq(possibleValue);
				} else {
					rootExpression = rootExpression.and(comparablePath.eq(possibleValue));
				}
			}
			return rootExpression;
		}
		return null;
	}
	
	// 	>	Match range if given
	protected <P extends Comparable<?>> BooleanExpression matchRangeIfGiven(ComparablePath<P> comparablePath, P minValue, P maxValue) {
		if (minValue != null && maxValue != null) {
			return comparablePath.between(minValue, maxValue);
		} else if (minValue != null) {
			return comparablePath.goe(minValue);
		} else if (maxValue != null) {
			return comparablePath.loe(maxValue);
		}
		return null;
	}
	
	protected <P extends Comparable<?>> BooleanExpression matchExclusiveRangeIfGiven(ComparablePath<P> comparablePath, P minValue, P maxValue) {
		if (minValue != null && maxValue != null) {
			return comparablePath.gt(minValue).and(comparablePath.lt(maxValue));
		} else if (minValue != null) {
			return comparablePath.gt(minValue);
		} else if (maxValue != null) {
			return comparablePath.lt(maxValue);
		}
		return null;
	}
}