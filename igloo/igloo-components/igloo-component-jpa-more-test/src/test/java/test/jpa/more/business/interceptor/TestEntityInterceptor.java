package test.jpa.more.business.interceptor;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import org.hibernate.CallbackException;
import org.hibernate.type.Type;
import org.iglooproject.jpa.hibernate.interceptor.AbstractChainableInterceptor;
import test.jpa.more.business.entity.model.TestEntity;

public class TestEntityInterceptor extends AbstractChainableInterceptor {

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
      throws CallbackException {
    Map<String, Object> changes = Maps.newHashMap();
    changes.put("classicInterceptorSave", "interceptor");

    updatePropertyValues(changes, propertyNames, state);

    return true;
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
    Map<String, Object> changes = Maps.newHashMap();
    changes.put("classicInterceptorFlushDirty", "interceptor");

    updatePropertyValues(changes, propertyNames, currentState);

    return true;
  }

  @Override
  public boolean applyTo(Object entity) {
    return isOfClass(entity, TestEntity.class);
  }
}
