package org.iglooproject.jpa.more.business.difference.util;

import org.jspecify.annotations.Nullable;

public class CompositeProxyInitializer<T> implements IProxyInitializer<T> {
  private final Iterable<? extends IProxyInitializer<? super T>> delegates;

  public CompositeProxyInitializer(Iterable<? extends IProxyInitializer<? super T>> delegates) {
    super();
    this.delegates = delegates;
  }

  @Override
  public void initialize(@Nullable T value) {
    for (IProxyInitializer<? super T> delegate : delegates) {
      delegate.initialize(value);
    }
  }
}
