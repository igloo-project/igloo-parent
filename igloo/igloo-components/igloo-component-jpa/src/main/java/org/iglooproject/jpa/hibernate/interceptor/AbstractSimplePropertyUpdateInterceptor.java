package org.iglooproject.jpa.hibernate.interceptor;

import java.io.Serializable;
import org.hibernate.type.Type;

public abstract class AbstractSimplePropertyUpdateInterceptor<T>
    extends AbstractChainableInterceptor {

  @SuppressWarnings("unchecked")
  @Override
  public boolean onFlushDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {

    for (int i = 0; i < propertyNames.length; ++i) {
      if (propertyNames[i].equals(getObservedFieldName())) {

        boolean dirty = false;
        if (currentState[i] == null) {
          if (previousState[i] != null) {
            dirty = true;
          }
        } else if (!currentState[i].equals(previousState[i])) {
          dirty = true;
        }

        if (dirty) {
          return onChange((T) entity, currentState, previousState, propertyNames, types);
        }

        return false;
      }
    }

    return false;
  }

  @Override
  public boolean applyTo(Object entity) {
    return isOfClass(entity, getObservedClass());
  }

  protected abstract Class<T> getObservedClass();

  protected abstract String getObservedFieldName();

  protected abstract boolean onChange(
      T entity,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types);
}
