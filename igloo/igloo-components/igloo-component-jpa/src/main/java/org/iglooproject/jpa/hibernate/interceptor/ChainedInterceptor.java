package org.iglooproject.jpa.hibernate.interceptor;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * Hibernate only allows one interceptor per session. While it might seem like a sensible choice to
 * some people, it's a common use to be able to apply different interceptors to different types of
 * entities - or even, several interceptors changing different properties.
 *
 * <p>Note that we don't implement all the methods of Interceptor as it's probably wise to think
 * twice about it before adding a method here. Not all interceptor methods might be good candidates
 * for this chain mechanism.
 *
 * <p>This interceptor allows to chain several interceptors.
 */
public class ChainedInterceptor extends EmptyInterceptor {

  private List<AbstractChainableInterceptor> interceptors = Lists.newArrayList();

  private static final long serialVersionUID = 3326023814493672159L;

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    boolean result = false;

    for (AbstractChainableInterceptor interceptor : interceptors) {
      if (interceptor.applyTo(entity)) {
        boolean modified = interceptor.onSave(entity, id, state, propertyNames, types);
        result = result || modified;
      }
    }
    return result;
  }

  @Override
  public boolean onFlushDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {
    boolean result = false;

    for (AbstractChainableInterceptor interceptor : interceptors) {
      if (interceptor.applyTo(entity)) {
        boolean modified =
            interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        result = result || modified;
      }
    }
    return result;
  }

  @Override
  public void onDelete(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    for (AbstractChainableInterceptor interceptor : interceptors) {
      if (interceptor.applyTo(entity)) {
        interceptor.onDelete(entity, id, state, propertyNames, types);
      }
    }
  }

  public ChainedInterceptor add(AbstractChainableInterceptor interceptor) {
    interceptors.add(interceptor);
    return this;
  }
}
