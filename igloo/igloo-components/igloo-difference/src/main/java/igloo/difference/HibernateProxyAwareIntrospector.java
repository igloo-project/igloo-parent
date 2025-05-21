package igloo.difference;

import de.danielbechler.diff.instantiation.TypeInfo;
import de.danielbechler.diff.introspection.PropertyAccessor;
import de.danielbechler.diff.introspection.StandardIntrospector;
import de.danielbechler.util.Assert;
import de.danielbechler.util.Exceptions;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * The only difference with {@link StandardIntrospector} is the use of {@link
 * HibernateProxyAwarePropertyAccessor} in {@link #internalIntrospect(Class)}
 *
 * <p>As we override private methods, this class must be updated accordingly if parent is modified.
 */
public class HibernateProxyAwareIntrospector extends StandardIntrospector {

  /**
   * Method copied from StandardInstrospector but calling our overridden {@link #internalIntrospect}
   * method
   */
  @Override
  public TypeInfo introspect(Class<?> type) {
    Assert.notNull(type, "type");
    try {
      return internalIntrospect(type);
    } catch (final IntrospectionException e) {
      throw Exceptions.escalate(e);
    }
  }

  /**
   * Method copied from StandardInstrospector but with {@link HibernateProxyAwarePropertyAccessor}
   * usage instead of {@link PropertyAccessor}. This private method is not called from other
   * superclass method, so overloading introspect and private internalInstrospect is fine
   */
  private TypeInfo internalIntrospect(final Class<?> type) throws IntrospectionException {
    final TypeInfo typeInfo = new TypeInfo(type);
    final PropertyDescriptor[] descriptors = getBeanInfo(type).getPropertyDescriptors();
    for (final PropertyDescriptor descriptor : descriptors) {
      if (shouldSkip(descriptor)) {
        continue;
      }
      final String propertyName = descriptor.getName();
      final Method readMethod = descriptor.getReadMethod();
      final Method writeMethod = descriptor.getWriteMethod();
      final HibernateProxyAwarePropertyAccessor accessor =
          new HibernateProxyAwarePropertyAccessor(propertyName, readMethod, writeMethod);
      typeInfo.addPropertyAccessor(accessor);
    }
    return typeInfo;
  }

  /**
   * Method copied from StandardInstrospector only to reproduce {@link #internalIntrospect(Class)}.
   */
  private static boolean shouldSkip(final PropertyDescriptor descriptor) {
    if (descriptor.getName().equals("class")) // Java & Groovy
    {
      return true;
    }
    if (descriptor.getName().equals("metaClass")) // Groovy
    {
      return true;
    }
    if (descriptor.getReadMethod() == null) {
      return true;
    }
    return false;
  }
}
