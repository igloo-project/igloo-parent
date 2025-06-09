package org.igloo.storage.tools.util.action;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;

public class DbListFichiersAction implements IDbAction<List<Fichier>> {

  private final List<Long> fichierIds;

  public DbListFichiersAction(List<Long> fichierIds) {
    this.fichierIds = fichierIds;
  }

  @Override
  public List<Fichier> perform(EntityManager entityManager, DatabaseOperations databaseOperations) {
    TypedQuery<Fichier> query =
        entityManager.createQuery(
            "SELECT f FROM Fichier f WHERE f.id IN (:ids) ORDER BY f.id ASC", Fichier.class);
    query.setParameter("ids", fichierIds);
    return query.getResultList();
  }
}
