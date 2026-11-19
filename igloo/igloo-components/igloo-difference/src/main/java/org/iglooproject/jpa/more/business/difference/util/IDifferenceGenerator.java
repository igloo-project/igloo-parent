package org.iglooproject.jpa.more.business.difference.util;

import org.iglooproject.jpa.more.business.difference.model.Difference;
import org.jspecify.annotations.Nullable;

public interface IDifferenceGenerator<T> {

  Difference<T> diff(T modifie, @Nullable T reference);
}
