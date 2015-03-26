package fr.openwide.core.jpa.business.generic.dao;

import java.util.List;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.StringExpression;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.QGenericEntity;

/**
 * Permet de factoriser les méthodes de GenericEntityDaoImpl, GenericListItemDaoImpl et GenericLocalizedGenericListItemDaoImpl
 */
public abstract class AbstractEntityDaoImpl<E> extends JpaDaoSupport {

	protected <T extends E, V extends Comparable<?>> T getByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue) {
		return queryByPredicate(entityPath, eqOrIsNull(field, fieldValue)).uniqueResult(entityPath);
	}

	protected <T extends E> T getByFieldIgnoreCase(EntityPath<T> entityPath, StringExpression field, String fieldValue) {
		return queryByPredicate(entityPath, eqIgnoreCaseOrIsNull(field, fieldValue)).uniqueResult(entityPath);
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
		return queryByPredicateOrdered(entityPath, null, limit, offset, order).list(entityPath);
	}

	protected <T extends E, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue, OrderSpecifier<?> orderSpecifier) {
		return queryByPredicateOrdered(entityPath, eqOrIsNull(field, fieldValue), orderSpecifier).list(entityPath);
	}

	protected <T extends E, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue, Long limit, Long offset, OrderSpecifier<?> orderSpecifier) {
		return queryByPredicateOrdered(entityPath, eqOrIsNull(field, fieldValue), limit, offset, orderSpecifier).list(entityPath);
	}

	protected <V extends Comparable<?>> Long count(EntityPath<? extends E> entityPath) {
		return queryByPredicate(entityPath, null).distinct().count();
	}

	protected <V extends Comparable<?>> Long countByField(EntityPath<? extends E> entityPath, SimpleExpression<V> field, V fieldValue) {
		return queryByPredicate(entityPath, eqOrIsNull(field, fieldValue)).distinct().count();
	}

	private static <V extends Comparable<?>> Predicate eqOrIsNull(SimpleExpression<V> field, V fieldValue) {
		return fieldValue != null ? field.eq(fieldValue) : field.isNull() ;
	}

	private static <V extends Comparable<?>> Predicate eqIgnoreCaseOrIsNull(StringExpression field, String fieldValue) {
		return fieldValue != null ? field.equalsIgnoreCase(fieldValue) : field.isNull();
	}
}
