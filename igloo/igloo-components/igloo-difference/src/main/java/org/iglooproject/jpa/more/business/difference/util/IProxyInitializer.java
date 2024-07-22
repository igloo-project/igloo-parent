package org.iglooproject.jpa.more.business.difference.util;

/**
 * An interface for the objects responsible for initializing the entities loaded from the database
 * before the transaction end. Allows to use fields which are lazily loaded.
 */
public interface IProxyInitializer<T> {
  void initialize(T value);
}
