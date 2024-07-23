package org.iglooproject.jpa.more.business.difference.util;

import com.google.common.collect.ImmutableList;
import org.iglooproject.functional.Function2;
import org.iglooproject.jpa.util.HibernateUtils;

public class FunctionProxyInitializer<T> implements IProxyInitializer<T> {
  private final Iterable<? extends Function2<? super T, ?>> functions;

  @SafeVarargs
  public FunctionProxyInitializer(Function2<? super T, ?>... functions) {
    super();
    this.functions = ImmutableList.copyOf(functions);
  }

  public FunctionProxyInitializer(Iterable<? extends Function2<? super T, ?>> functions) {
    super();
    this.functions = ImmutableList.copyOf(functions);
  }

  @Override
  public void initialize(T value) {
    HibernateUtils.initialize(value);
    for (Function2<? super T, ?> function : functions) {
      HibernateUtils.initialize(function.apply(value));
    }
  }
}
