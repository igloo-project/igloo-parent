package org.iglooproject.jpa.more.business.difference.util;

import org.iglooproject.jpa.more.business.difference.model.Difference;

public interface IDifferenceGenerator<T> {

  Difference<T> diff(T modifie, T reference);
}
