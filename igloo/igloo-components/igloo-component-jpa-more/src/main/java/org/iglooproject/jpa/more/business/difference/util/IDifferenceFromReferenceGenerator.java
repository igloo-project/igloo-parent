package org.iglooproject.jpa.more.business.difference.util;

import java.util.concurrent.Callable;
import org.iglooproject.jpa.more.business.difference.model.Difference;

public interface IDifferenceFromReferenceGenerator<T> extends IDifferenceGenerator<T> {

  Difference<T> diffFromReference(T value);

  /**
   * When we have to compute the differences for a lot of objects, this allows to get the references
   * in only one transaction, instead of opening a transaction for each difference computation.
   *
   * <p>The returned reference, to be usable by the method {@link #diff(Object, Object)}, should be
   * passed through the method {@link #initializeReference(Object)}
   *
   * @see #initializeReference(Object)
   * @see #diff(Object, Object)
   */
  Callable<T> getReferenceProvider(T value);

  /**
   * Must be called on an object returned by {@link #retrieveReference(Object)} to make it usable by
   * the method {@link #diff(Object, Object)}.
   *
   * @see #retrieveReference(Object)
   * @see #diff(Object, Object)
   */
  void initializeReference(T reference);
}
