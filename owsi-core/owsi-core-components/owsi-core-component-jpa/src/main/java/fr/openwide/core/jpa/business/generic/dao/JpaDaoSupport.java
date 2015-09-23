package fr.openwide.core.jpa.business.generic.dao;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.Session;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableEntityPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.QGenericEntity;

public class JpaDaoSupport {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * @deprecated Utiliser QueryDSL.
	 * 
	 * Crée la requête et applique les conditions de limite / offset et retourne la {@link TypedQuery}
	 * correspondante.
	 * 
	 * @param <T> le type de l'entité retournée
	 * @param criteria
	 * @param limit null si pas de limite
	 * @param offset null si pas d'offset
	 * @return la {@link TypedQuery} avec limite et offset le cas échéant
	 */
	@Deprecated
	protected <T> TypedQuery<T> buildTypedQuery(CriteriaQuery<T> criteria, Integer limit, Integer offset) {
		TypedQuery<T> query = getEntityManager().createQuery(criteria);
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}
		return query;
		
	}

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	protected void filterCriteriaQuery(CriteriaQuery<?> criteria, Expression<Boolean> filter) {
		if (filter != null) {
			javax.persistence.criteria.Predicate currentFilter = criteria.getRestriction();
			if (currentFilter != null) {
				filter = getEntityManager().getCriteriaBuilder().and(currentFilter, filter);
			}
			criteria.where(filter);
		}
	}

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	protected <T> Root<T> rootCriteriaQuery(CriteriaBuilder builder, CriteriaQuery<?> criteria, Class<T> objectClass) {
		return criteria.from(objectClass);
	}

	protected <T, K> T getEntity(Class<T> clazz, K id) {
		return getEntityManager().find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntityByNaturalId(Class<T> clazz, Object naturalId) {
		if (naturalId == null) {
			throw new IllegalArgumentException("Natural id may not be null");
		}
		
		Session session = getEntityManager().unwrap(Session.class);
		
		return (T) session.bySimpleNaturalId(clazz).load(naturalId);
	}

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	public <T, V extends Comparable<?>> T getEntityByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, "rootAlias");
		return queryEntityByField(entityPath, attribute.getBindableJavaType(), attribute.getName(), fieldValue).fetchOne();
	}

	protected <T, V extends Comparable<?>> JPAQuery<T> queryEntityByField(EntityPath<T> entityPath, Class<V> fieldClass, String fieldName, V fieldValue) {
		ComparableEntityPath<V> field = Expressions.comparableEntityPath(fieldClass, entityPath, fieldName);
		
		return queryByPredicate(entityPath, field.eq(fieldValue));
		
	}

	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	public <T> T getEntityByFieldIgnoreCase(Class<T> clazz, SingularAttribute<? super T, String> attribute, String fieldValue) {
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, "rootAlias");
		StringPath field = Expressions.stringPath(entityPath, attribute.getName());
		
		return queryByPredicate(entityPath, field.equalsIgnoreCase(fieldValue)).fetchOne();
	}
	
	protected <T> void update(T entity) {
		if (!getEntityManager().contains(entity)) {
			throw new PersistenceException("Updated entity must be attached");
		}
		//TODO: http://blog.xebia.com/2009/03/23/jpa-implementation-patterns-saving-detached-entities/
	}
	
	protected <T> void save(T entity) {
		getEntityManager().persist(entity);
	}
	
	protected <T> void delete(T entity) {
		getEntityManager().remove(entity);
	}
	
	protected <T> T refresh(T entity) {
		getEntityManager().refresh(entity);
		
		return entity;
	}
	
	public void flush() {
		getEntityManager().flush();
	}
	
	public void clear() {
		getEntityManager().clear();
	}
	
	// TODO : à refaire : il n'est pas possible de construire un filter ou un order stateless
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	protected <T> List<T> listEntity(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(objectClass);
		rootCriteriaQuery(builder, criteria, objectClass);
		
		if (filter != null) {
			filterCriteriaQuery(criteria, filter);
		}
		if (orders != null && orders.length > 0) {
			criteria.orderBy(orders);
		}
		TypedQuery<T> query = buildTypedQuery(criteria, limit, offset);
		
		List<T> entities = query.getResultList();
		
		if (orders == null || orders.length == 0) {
			sort(entities);
		}
		
		return entities;
	}

	public <T> List<T> listEntity(Class<T> objectClass) {
		PathBuilder<T> pathBuilder = new PathBuilder<T>(objectClass, "rootAlias");
		OrderSpecifier<?> order = null;
		if (GenericEntity.class.isAssignableFrom(objectClass)) {
			// cast possible puisqu'on vient de vérifier le type de objectclass
			@SuppressWarnings("unchecked")
			QGenericEntity qGenericEntity = new QGenericEntity((Path<? extends GenericEntity<?, ?>>) (Object) pathBuilder);
			order = qGenericEntity.id.asc();
		}
		
		return queryByPredicateOrdered(pathBuilder, null, order).list(pathBuilder);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	protected <T> List<T> listEntity(Class<T> objectClass, Expression<Boolean> filter) {
		return listEntity(objectClass, filter, null, null);
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	protected <T, V extends Comparable<?>> List<T> listEntityByField(Class<T> objectClass, SingularAttribute<? super T, V> attribute, V fieldValue) {
		PathBuilder<T> entityPath = new PathBuilder<T>(objectClass, "rootAlias");
		List<T> entities = queryEntityByField(entityPath, attribute.getBindableJavaType(), attribute.getName(), fieldValue).list(entityPath);
		sort(entities);
		
		return entities;
	}

	protected <T> Long countEntity(Class<T> clazz) {
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, "rootAlias");
		return queryByPredicate(entityPath, null).distinct().fetchCount();
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	protected <T, V extends Comparable<?>> Long countEntityByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		PathBuilder<T> entityPath = new PathBuilder<T>(clazz, "rootAlias");
		return queryEntityByField(entityPath, attribute.getBindableJavaType(), attribute.getName(), fieldValue).distinct().fetchCount();
	}

	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	protected <T> Long countEntity(Class<T> clazz, Expression<Boolean> filter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> void sort(List<T> entities) {
		Object[] a = entities.toArray();
		Arrays.sort(a);
		ListIterator<T> i = entities.listIterator();
		for (int j = 0; j < a.length; j++) {
			i.next();
			i.set((T) a[j]);
		}
	}

	protected <T> JPAQuery<T> queryByPredicateOrdered(EntityPath<T> entityPath, Predicate predicate, OrderSpecifier<?>... orderSpecifiers) {
		return queryByPredicateOrdered(entityPath, predicate, null, null, orderSpecifiers);
	}

	protected <T> JPAQuery<T> queryByPredicateOrdered(EntityPath<T> entityPath, Predicate predicate, Long limit, Long offset, OrderSpecifier<?>... orderSpecifiers) {
		JPAQuery<T> query = queryByPredicate(entityPath, predicate, limit, offset);
		
		if (orderSpecifiers != null && orderSpecifiers.length > 0) {
			query.orderBy(orderSpecifiers);
		}
		
		return query;
	}

	protected <T> JPAQuery<T> queryByPredicate(EntityPath<T> entityPath, Predicate predicate) {
		return queryByPredicate(entityPath, predicate, null, null);
	}

	protected <T> JPAQuery<T> queryByPredicate(EntityPath<T> entityPath, Predicate predicate, Long limit, Long offset) {
		JPAQuery<T> query = new JPAQuery<>(getEntityManager());
		query.from(entityPath);
		
		if (predicate != null) {
			query.where(predicate);
		}
		
		if (offset != null) {
			query.offset(offset);
		}
		
		if (limit != null) {
			query.limit(limit);
		}
		
		return query;
	}

}
