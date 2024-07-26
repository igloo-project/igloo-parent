package test.jpa.more.business.interceptor;

import com.google.common.collect.Maps;
import java.util.Map;
import org.hibernate.type.Type;
import org.iglooproject.jpa.hibernate.interceptor.AbstractSimplePropertyUpdateInterceptor;
import test.jpa.more.business.entity.model.TestEntity;

public class TestEntitySimplePropertyUpdateInterceptor
    extends AbstractSimplePropertyUpdateInterceptor<TestEntity> {

  @Override
  protected Class<TestEntity> getObservedClass() {
    return TestEntity.class;
  }

  @Override
  protected String getObservedFieldName() {
    return "simplePropertyUpdate";
  }

  @Override
  protected boolean onChange(
      TestEntity entity,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {
    Map<String, Object> changes = Maps.newHashMap();
    changes.put("simplePropertyUpdateInterceptor", "interceptor");

    updatePropertyValues(changes, propertyNames, currentState);

    return true;
  }
}
