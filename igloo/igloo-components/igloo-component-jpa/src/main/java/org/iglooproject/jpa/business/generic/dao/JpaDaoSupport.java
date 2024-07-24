package org.iglooproject.jpa.business.generic.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableEntityPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import org.hibernate.Session;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.QGenericEntity;

public class JpaDaoSupport {

  @PersistenceContext private EntityManager entityManager;

  protected <T, K> T getEntity(Class<T> clazz, K id) {
    return getEntityManager().find(clazz, id);
  }

  public <T> T getEntityByNaturalId(Class<T> clazz, Object naturalId) {
    if (naturalId == null) {
      throw new IllegalArgumentException("Natural id may not be null");
    }

    Session session = getEntityManager().unwrap(Session.class);

    return (T) session.bySimpleNaturalId(clazz).load(naturalId);
  }

  protected <T, V extends Comparable<?>> JPAQuery<T> queryEntityByField(
      EntityPath<T> entityPath, Class<V> fieldClass, String fieldName, V fieldValue) {
    ComparableEntityPath<V> field =
        Expressions.comparableEntityPath(fieldClass, entityPath, fieldName);

    return queryByPredicate(entityPath, field.eq(fieldValue));
  }

  protected <T> void update(T entity) {
    if (!getEntityManager().contains(entity)) {
      throw new PersistenceException("Updated entity must be attached");
    }
    // TODO: http://blog.xebia.com/2009/03/23/jpa-implementation-patterns-saving-detached-entities/
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

  public <T> List<T> listEntity(Class<T> objectClass) {
    PathBuilder<T> pathBuilder = new PathBuilder<>(objectClass, "rootAlias");
    OrderSpecifier<?> order = null;
    if (GenericEntity.class.isAssignableFrom(objectClass)) {
      // cast possible puisqu'on vient de vérifier le type de objectclass
      @SuppressWarnings("unchecked")
      QGenericEntity qGenericEntity =
          new QGenericEntity((Path<? extends GenericEntity<?, ?>>) (Object) pathBuilder);
      order = qGenericEntity.id.asc();
    }

    return queryByPredicateOrdered(pathBuilder, null, order).fetch();
  }

  protected <T> Long countEntity(Class<T> clazz) {
    PathBuilder<T> entityPath = new PathBuilder<>(clazz, "rootAlias");
    return queryByPredicate(entityPath, null).distinct().fetchCount();
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

  protected <T> JPAQuery<T> queryByPredicateOrdered(
      EntityPath<T> entityPath, Predicate predicate, OrderSpecifier<?>... orderSpecifiers) {
    return queryByPredicateOrdered(entityPath, predicate, null, null, orderSpecifiers);
  }

  protected <T> JPAQuery<T> queryByPredicateOrdered(
      EntityPath<T> entityPath,
      Predicate predicate,
      Long limit,
      Long offset,
      OrderSpecifier<?>... orderSpecifiers) {
    JPAQuery<T> query = queryByPredicate(entityPath, predicate, limit, offset);

    if (orderSpecifiers != null && orderSpecifiers.length > 0) {
      query.orderBy(orderSpecifiers);
    }

    return query;
  }

  protected <T> JPAQuery<T> queryByPredicate(EntityPath<T> entityPath, Predicate predicate) {
    return queryByPredicate(entityPath, predicate, null, null);
  }

  protected <T> JPAQuery<T> queryByPredicate(
      EntityPath<T> entityPath, Predicate predicate, Long limit, Long offset) {
    JPAQuery<T> query = new JPAQuery<>(getEntityManager());
    query.select(entityPath).from(entityPath);

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
