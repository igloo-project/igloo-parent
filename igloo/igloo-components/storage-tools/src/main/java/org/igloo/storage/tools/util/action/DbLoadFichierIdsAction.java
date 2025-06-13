package org.igloo.storage.tools.util.action;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.igloo.storage.impl.DatabaseOperations;

public class DbLoadFichierIdsAction implements IDbAction<List<Long>> {
  private final String query;

  public DbLoadFichierIdsAction(String query) {
    this.query = query;
  }

  @Override
  public List<Long> perform(EntityManager entityManager, DatabaseOperations databaseOperations) {
    @SuppressWarnings("unchecked")
    List<Long> result = entityManager.createNativeQuery(query, Long.class).getResultList();
    return result;
  }
}
