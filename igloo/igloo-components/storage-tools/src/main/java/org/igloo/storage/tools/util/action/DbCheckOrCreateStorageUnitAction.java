package org.igloo.storage.tools.util.action;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.igloo.storage.tools.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbCheckOrCreateStorageUnitAction implements IDbAction<StorageUnit> {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(DbCheckOrCreateStorageUnitAction.class);

  private final Path path;
  private final IStorageUnitType type;

  public DbCheckOrCreateStorageUnitAction(Path path, IStorageUnitType type) {
    this.path = path;
    this.type = type;
  }

  @Override
  public StorageUnit perform(EntityManager entityManager, DatabaseOperations databaseOperations) {
    TypedQuery<StorageUnit> query =
        entityManager.createQuery(
            "SELECT s FROM StorageUnit s WHERE s.path = :path", StorageUnit.class);
    query.setParameter("path", path.toString());
    List<StorageUnit> storageUnits = query.getResultList();
    if (storageUnits.isEmpty()) {
      LOGGER.info("No StorageUnit found for path {}", path);
      StorageUnit storageUnit = new StorageUnit();
      storageUnit.setCreationDate(LocalDateTime.now());
      storageUnit.setPath(path.toString());
      storageUnit.setStatus(StorageUnitStatus.ARCHIVED);
      storageUnit.setType(type);
      storageUnit.setId(databaseOperations.generateStorageUnit());
      databaseOperations.createStorageUnit(storageUnit);
      entityManager.flush();
      LOGGER.info("StorageUnit created {}", storageUnit);
      return storageUnit;
    } else if (storageUnits.size() == 1) {
      StorageUnit storageUnit = storageUnits.get(0);
      LOGGER.info("Existing StorageUnit {} found", storageUnit);
      if (!type.equals(storageUnit.getType())) {
        LOGGER.warn(
            "StorageUnit {} type mismatch ({} != {})",
            storageUnit,
            storageUnit.getType().getName(),
            type.getName());
      }
      return storageUnit;
    } else {
      throw new ConfigurationException(
          "More than one storage unit found for path %s".formatted(path));
    }
  }
}
