package org.igloo.storage.tools.util.action;

import jakarta.persistence.EntityManager;
import org.igloo.storage.impl.DatabaseOperations;

public interface IDbAction<T> {
  T perform(EntityManager entityManager, DatabaseOperations databaseOperations);
}
