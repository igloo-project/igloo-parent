package org.iglooproject.test.jpa.util.bean;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.iglooproject.functional.Function2;

/**
 * Convert from a {@link DynaBean} to a POJO.
 *
 * @author Laurent Almeras
 */
public class DynaBeanConverter<T> implements Function2<DynaBean, T> {

  private final Class<T> targetType;

  public DynaBeanConverter(Class<T> targetType) {
    super();
    this.targetType = targetType;
  }

  @Override
  public T apply(DynaBean input) {
    if (input == null) {
      return null;
    }

    try {
      T newInstance = targetType.getConstructor().newInstance();
      BeanUtils.copyProperties(newInstance, input);
      return newInstance;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(
          String.format("newInstance must be available on %s", targetType.getSimpleName()), e);
    } catch (IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
      throw new IllegalStateException(
          String.format("error copying properties on %s", targetType.getSimpleName()), e);
    }
  }

  public DynaBeanConverter<T> as(Class<T> targetType) {
    return new DynaBeanConverter<>(targetType);
  }
}
