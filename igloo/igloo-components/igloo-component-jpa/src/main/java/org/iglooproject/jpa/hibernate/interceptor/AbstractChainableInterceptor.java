package org.iglooproject.jpa.hibernate.interceptor;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * Apart from providing a couple of utility methods, the main point of this class it to make final
 * all the methods which are not supported by ChainedPropertyInterceptor.
 */
public abstract class AbstractChainableInterceptor implements Interceptor {

  public abstract boolean applyTo(Object entity);

  protected boolean isOfClass(Object entity, Class<?>... classes) {
    if (entity == null) {
      return false;
    }
    for (Class<?> clazz : classes) {
      if (clazz.isAssignableFrom(Hibernate.getClass(entity))) {
        return true;
      }
    }
    return false;
  }

  protected void updatePropertyValues(
      Map<String, Object> propertyValuesMap, String[] propertyNames, Object[] currentState) {
    Set<String> propertiesToUpdate = Sets.newTreeSet(propertyValuesMap.keySet());
    for (int i = 0; i < propertyNames.length; ++i) {
      if (propertyValuesMap.containsKey(propertyNames[i])) {
        currentState[i] = propertyValuesMap.get(propertyNames[i]);
        propertiesToUpdate.remove(propertyNames[i]);
      }
      if (propertiesToUpdate.isEmpty()) {
        break;
      }
    }
  }

  @Override
  public boolean onFlushDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types)
      throws CallbackException {
    return false;
  }

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {
    return false;
  }

  @Override
  public void onDelete(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {}

  @Override
  public final boolean onLoad(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {
    return false;
  }

  @Override
  public final void onCollectionRecreate(Object collection, Serializable key)
      throws CallbackException {}

  @Override
  public final void onCollectionRemove(Object collection, Serializable key)
      throws CallbackException {}

  @Override
  public final void onCollectionUpdate(Object collection, Serializable key)
      throws CallbackException {}

  @SuppressWarnings("rawtypes")
  @Override
  public final void preFlush(Iterator entities) throws CallbackException {}

  @SuppressWarnings("rawtypes")
  @Override
  public final void postFlush(Iterator entities) throws CallbackException {}

  @Override
  public final Boolean isTransient(Object entity) {
    return null;
  }

  @Override
  public final int[] findDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {
    return null;
  }

  @Override
  public final Object instantiate(String entityName, EntityMode entityMode, Serializable id)
      throws CallbackException {
    return null;
  }

  @Override
  public final String getEntityName(Object object) throws CallbackException {
    return null;
  }

  @Override
  public final Object getEntity(String entityName, Serializable id) throws CallbackException {
    return null;
  }

  @Override
  public final void afterTransactionBegin(Transaction tx) {}

  @Override
  public final void beforeTransactionCompletion(Transaction tx) {}

  @Override
  public final void afterTransactionCompletion(Transaction tx) {}

  @Override
  public final String onPrepareStatement(String sql) {
    return sql;
  }
}
