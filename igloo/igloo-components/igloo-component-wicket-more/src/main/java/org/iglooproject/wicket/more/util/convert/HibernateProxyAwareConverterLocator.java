package org.iglooproject.wicket.more.util.convert;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.IConverter;
import org.hibernate.proxy.HibernateProxy;

/** Allows to get converters even for hibernate-proxyfied objects. */
public class HibernateProxyAwareConverterLocator implements IConverterLocator {

  private static final long serialVersionUID = -8086786353007241785L;

  private final IConverterLocator delegate;

  public HibernateProxyAwareConverterLocator(IConverterLocator delegate) {
    this.delegate = delegate;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C> IConverter<C> getConverter(Class<C> type) {
    if (HibernateProxy.class.isAssignableFrom(type)) {
      return (IConverter<C>) delegate.getConverter(type.getSuperclass()); // NOSONAR
    } else {
      return delegate.getConverter(type);
    }
  }
}
