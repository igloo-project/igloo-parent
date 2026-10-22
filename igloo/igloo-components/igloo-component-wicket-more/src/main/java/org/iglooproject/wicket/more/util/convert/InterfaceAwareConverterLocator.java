package org.iglooproject.wicket.more.util.convert;

import java.util.Objects;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.jpa.more.rendering.IRenderable;
import org.iglooproject.jpa.more.rendering.RenderableTypes;

/**
 * Allows to get converters registered on an interface extending {@link IRenderable} or on a
 * superclass of the requested type.
 *
 * <p>Lookup order: exact type, then interfaces extending {@link IRenderable} (and their
 * super-interfaces), then superclasses (recursively). When nothing is registered, the delegate's
 * default converter is returned.
 *
 * <p>The delegate must be a {@link ConverterLocator} because {@link
 * IConverterLocator#getConverter(Class)} never returns {@code null}: only {@link
 * ConverterLocator#get(Class)} allows to know whether a converter is actually registered. This
 * locator should therefore wrap the {@link ConverterLocator} directly, inside any other wrapper.
 */
public class InterfaceAwareConverterLocator implements IConverterLocator {

  private static final long serialVersionUID = 1L;

  private final ConverterLocator delegate;

  public InterfaceAwareConverterLocator(ConverterLocator delegate) {
    this.delegate = delegate;
  }

  @Override
  public <C> IConverter<C> getConverter(Class<C> type) {
    if (type == null) {
      return delegate.getConverter(type);
    }
    IConverter<C> converter = delegate.get(type);
    if (converter != null) {
      return converter;
    }
    converter = findConverterInHierarchy(type);
    if (converter != null) {
      return converter;
    }
    return delegate.getConverter(type);
  }

  @SuppressWarnings("unchecked")
  private <C> IConverter<C> findConverterInHierarchy(Class<?> type) {
    return RenderableTypes.hierarchy(type)
        .map(candidate -> (IConverter<C>) delegate.get(candidate))
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
}
