package igloo.difference;

import de.danielbechler.diff.comparison.ComparisonStrategy;
import de.danielbechler.diff.differ.BeanDiffer;
import de.danielbechler.diff.introspection.PropertyAccessor;
import java.lang.reflect.Method;
import org.hibernate.proxy.HibernateProxy;
import org.iglooproject.jpa.util.HibernateUtils;

/**
 * The only difference with {@link PropertyAccessor} is the unwrap step for {@link HibernateProxy}
 * objects.
 *
 * <p>This is mandatory to get the right {@link ComparisonStrategy} in {@link
 * BeanDiffer#compareUsingAppropriateMethod}. Without it, type extraction can return an {@link
 * HibernateProxy} type if both previous / after values are wrapped, and it defeats comparison
 * strategy lookup.
 *
 * <p>By unwrapping values, we ensure that real entity type is used.
 */
public class HibernateProxyAwarePropertyAccessor extends PropertyAccessor {

  public HibernateProxyAwarePropertyAccessor(
      String propertyName, Method readMethod, Method writeMethod) {
    super(propertyName, readMethod, writeMethod);
  }

  @Override
  public Object get(Object target) {
    Object object = super.get(target);
    if (object == null) {
      return null;
    }
    if (HibernateProxy.class.isAssignableFrom(object.getClass())) {
      object = HibernateUtils.unwrap(object);
    }
    return object;
  }
}
