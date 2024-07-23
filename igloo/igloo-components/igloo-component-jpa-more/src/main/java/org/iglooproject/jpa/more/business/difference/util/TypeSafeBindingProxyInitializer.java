package org.iglooproject.jpa.more.business.difference.util;

import com.google.common.collect.ImmutableList;
import org.bindgen.BindingRoot;
import org.iglooproject.jpa.util.HibernateUtils;

public class TypeSafeBindingProxyInitializer<R> implements IProxyInitializer<R> {
  private final Iterable<? extends BindingRoot<? extends R, ?>> bindings;

  @SafeVarargs
  public TypeSafeBindingProxyInitializer(BindingRoot<? extends R, ?>... functions) {
    super();
    this.bindings = ImmutableList.copyOf(functions);
  }

  public TypeSafeBindingProxyInitializer(
      Iterable<? extends BindingRoot<? extends R, ?>> functions) {
    super();
    this.bindings = ImmutableList.copyOf(functions);
  }

  @Override
  public void initialize(R value) {
    R unwrapped = HibernateUtils.unwrap(value);
    for (BindingRoot<? extends R, ?> binding : bindings) {
      HibernateUtils.initialize(getTypeSafe(binding, unwrapped));
    }
  }

  @SuppressWarnings("unchecked")
  private <R2 extends R, T> T getTypeSafe(BindingRoot<R2, T> binding, R unwrapped) {
    if (unwrapped != null
        && binding.getRootBinding().getType().isAssignableFrom(unwrapped.getClass())) {
      return binding.getSafelyWithRoot((R2) unwrapped);
    } else {
      return null;
    }
  }
}
