package org.iglooproject.jpa.business.generic.dao;

import java.util.List;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.QGenericEntity;

public abstract class AbstractEntityDaoImpl<E> extends JpaDaoSupport {

	protected <T extends E, V extends Comparable<?>> T getByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue) {
		return queryByPredicate(entityPath, eqOrIsNull(field, fieldValue)).fetchOne();
	}

	protected <T extends E> T getByFieldIgnoreCase(EntityPath<T> entityPath, StringExpression field, String fieldValue) {
		return queryByPredicate(entityPath, eqIgnoreCaseOrIsNull(field, fieldValue)).fetchOne();
	}

	protected <T extends E> List<T> list(EntityPath<T> entityPath) {
		return list(entityPath, null, null);
	}

	protected <T extends E> List<T> list(EntityPath<T> entityPath, Long limit, Long offset) {
		OrderSpecifier<?> order = null;
		if (GenericEntity.class.isAssignableFrom(entityPath.getType())) {
			// cast possible puisqu'on vient de vérifier le type de objectclass
			@SuppressWarnings("unchecked")
			QGenericEntity qGenericEntity = new QGenericEntity((Path<? extends GenericEntity<?, ?>>) (Object) entityPath);
			order = qGenericEntity.id.asc();
		}
		return queryByPredicateOrdered(entityPath, null, limit, offset, order).fetch();
	}

	protected <T extends E, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue, OrderSpecifier<?>... orderSpecifiers) {
		return queryByPredicateOrdered(entityPath, eqOrIsNull(field, fieldValue), orderSpecifiers).fetch();
	}

	protected <T extends E, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue, Long limit, Long offset, OrderSpecifier<?>... orderSpecifiers) {
		return queryByPredicateOrdered(entityPath, eqOrIsNull(field, fieldValue), limit, offset, orderSpecifiers).fetch();
	}

	protected Long count(EntityPath<? extends E> entityPath) {
		return queryByPredicate(entityPath, null).distinct().fetchCount();
	}

	protected <V extends Comparable<?>> Long countByField(EntityPath<? extends E> entityPath, SimpleExpression<V> field, V fieldValue) {
		return queryByPredicate(entityPath, eqOrIsNull(field, fieldValue)).distinct().fetchCount();
	}

	private static <V extends Comparable<?>> Predicate eqOrIsNull(SimpleExpression<V> field, V fieldValue) {
		return fieldValue != null ? field.eq(fieldValue) : field.isNull() ;
	}

	private static Predicate eqIgnoreCaseOrIsNull(StringExpression field, String fieldValue) {
		return fieldValue != null ? field.equalsIgnoreCase(fieldValue) : field.isNull();
	}
}
