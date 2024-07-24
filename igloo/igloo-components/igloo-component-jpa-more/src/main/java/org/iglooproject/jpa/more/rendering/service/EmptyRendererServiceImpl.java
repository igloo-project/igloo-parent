package org.iglooproject.jpa.more.rendering.service;

import java.util.Locale;
import org.iglooproject.commons.util.context.ExecutionContexts;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.commons.util.rendering.IRenderer;
import org.iglooproject.functional.Joiners;

/** Mock implementation, only used to fill the dependency. */
public class EmptyRendererServiceImpl implements IRendererService {

  protected enum DefaultRenderer implements IRenderer<Object> {
    INSTANCE;

    @Override
    public String render(Object value, Locale locale) {
      return String.valueOf(value);
    }
  }

  @Override
  public IExecutionContext context() {
    return ExecutionContexts.noOp();
  }

  @Override
  public IExecutionContext context(Locale locale) {
    return ExecutionContexts.noOp();
  }

  @Override
  public <T> IRenderer<? super T> findRenderer(Class<T> valueType) {
    return DefaultRenderer.INSTANCE;
  }

  @Override
  public <T> IRenderer<? super T> findRenderer(Class<?> rootType, Class<T> valueType) {
    return DefaultRenderer.INSTANCE;
  }

  @Override
  public <T> IRenderer<? super T> findRenderer(
      Class<?> rootType, FieldPath path, Class<T> valueType) {
    return DefaultRenderer.INSTANCE;
  }

  @Override
  public String localize(String resourceKey) {
    return resourceKey;
  }

  @Override
  public String localize(
      String resourceKey, Object namedParameters, Object... positionalParameters) {
    return resourceKey
        + "("
        + namedParameters
        + ", ["
        + Joiners.onComma().join(positionalParameters)
        + "])";
  }

  @Override
  public String localize(Enum<?> enumValue, String prefix, String suffix) {
    return Joiners.onDot().join(prefix, enumValue, suffix);
  }

  @Override
  public <T> String localize(Class<T> clazz, T value) {
    return clazz.getName() + " / " + value;
  }

  @Override
  public String localize(String resourceKey, Locale locale) {
    return localize(resourceKey);
  }

  @Override
  public String localize(
      String resourceKey, Locale locale, Object namedParameters, Object... positionalParameters) {
    return localize(resourceKey, namedParameters, positionalParameters);
  }

  @Override
  public String localize(Enum<?> enumValue, String prefix, String suffix, Locale locale) {
    return localize(enumValue, prefix, suffix);
  }

  @Override
  public <T> String localize(Class<T> clazz, T value, Locale locale) {
    return localize(clazz, value);
  }
}
