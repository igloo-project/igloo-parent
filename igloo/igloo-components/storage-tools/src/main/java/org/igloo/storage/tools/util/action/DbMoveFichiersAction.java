package org.igloo.storage.tools.util.action;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.tools.util.FichierUtil.MoveResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbMoveFichiersAction implements IDbAction<Boolean> {
  private static final Logger LOGGER = LoggerFactory.getLogger(DbMoveFichiersAction.class);

  private final StorageUnit detachedTargetStorageUnit;
  private final Map<Long, MoveResult> results;

  public DbMoveFichiersAction(
      StorageUnit detachedTargetStorageUnit, Map<Long, MoveResult> results) {
    this.detachedTargetStorageUnit = detachedTargetStorageUnit;
    this.results = results;
  }

  @Override
  public Boolean perform(EntityManager entityManager, DatabaseOperations databaseOperations) {
    StorageUnit freshStorageUnit =
        databaseOperations.getStorageUnit(detachedTargetStorageUnit.getId());
    List<Fichier> fichiers = listFichiers(entityManager, results.keySet());
    if (results.size() != fichiers.size()) {
      LOGGER.warn("Move result and database Fichier count mismatch.");
    }
    for (Fichier fichier : fichiers) {
      if (!results.containsKey(fichier.getId())) {
        // Not possible
        LOGGER.warn(
            "Move result and database Fichier mismatch. Database {} not found in result", fichier);
      }
      switch (results.get(fichier.getId())) {
        case ALREADY_MOVED, MOVED, MISSING:
          fichier.setStorageUnit(freshStorageUnit);
          break;
        case MOVE_FAILED, UNTOUCHED:
          break;
        default:
          throw new IllegalStateException();
      }
    }
    return true;
  }

  protected List<Fichier> listFichiers(EntityManager entityManager, Collection<Long> fichierIds) {
    TypedQuery<Fichier> query =
        entityManager.createQuery("SELECT f FROM Fichier f WHERE f.id IN (:ids)", Fichier.class);
    query.setParameter("ids", fichierIds);
    return query.getResultList();
  }

  public Map<Long, MoveResult> getResults() {
    return results;
  }
}
