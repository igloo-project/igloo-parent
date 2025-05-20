package org.igloo.storage.tools.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.igloo.storage.tools.ConfigurationException;
import org.igloo.storage.tools.EntityManagerHelper;
import org.igloo.storage.tools.commands.ExecutorMixin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

public class FichierUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(FichierUtil.class);

  private final EntityManagerHelper entityManagerHelper;
  private final DatabaseOperations databaseOperations;

  public FichierUtil(
      EntityManagerHelper entityManagerHelper, DatabaseOperations databaseOperations) {
    this.entityManagerHelper = entityManagerHelper;
    this.databaseOperations = databaseOperations;
  }

  /**
   * Load {@link Fichier} ids from a provided list or a query file. Ids are put in a set to detect
   * duplicates.
   */
  public List<Long> loadFichierIdsFromQuery(Path queryFile, List<Long> paramFichiersIds) {
    final List<Long> fichierIds;
    if (paramFichiersIds != null && !paramFichiersIds.isEmpty()) {
      fichierIds = paramFichiersIds;
    } else {
      fichierIds = loadFichierIdsFromQuery(queryFile);
    }

    // deduplicate and keep order
    LinkedHashSet<Long> fichierIdsSet = new LinkedHashSet<>();
    fichierIdsSet.addAll(fichierIds);

    if (!Objects.equals(fichierIdsSet.size(), fichierIds.size())) {
      LOGGER.warn(
          "{} distinct ids loaded (from {} ids - there is duplicates).",
          fichierIdsSet.size(),
          fichierIds.size());
    } else {
      LOGGER.info("{} distinct ids loaded.", fichierIdsSet.size());
    }

    fichierIds.clear();
    fichierIds.addAll(fichierIdsSet);

    return fichierIds;
  }

  /** Create or find an existing {@link StorageUnit}. */
  public StorageUnit checkOrCreateStorageUnit(Path path, IStorageUnitType type) {
    if (!path.isAbsolute()) {
      throw new ConfigurationException("Path %s must be an absolute path.".formatted(path));
    }
    return entityManagerHelper.doWithReadWriteTransaction(
        e -> {
          TypedQuery<StorageUnit> query =
              e.createQuery("SELECT s FROM StorageUnit s WHERE s.path = :path", StorageUnit.class);
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
            e.flush();
            LOGGER.info("StorageUnit created {}", storageUnit);
            return storageUnit;
          } else if (storageUnits.size() == 1) {
            StorageUnit storageUnit = storageUnits.get(0);
            LOGGER.info("Existing StorateUnit %s found".formatted(storageUnit));
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
        });
  }

  /** Move files. */
  public void processMove(
      ExecutorMixin executor,
      RunMode runMode,
      StorageUnit targetStorageUnit,
      List<Long> fichierIds) {
    ArchivingProgressMonitor monitor = createMonitor(fichierIds.size());
    if (RunMode.DRY_RUN.equals(runMode)) {
      LOGGER.warn("****** Archive is DRY-RUN mode ******");
    }
    LOGGER.info(
        "Fichier id listings (missing.txt, moved-not-updated.txt) written in {}",
        monitor.getTempDirectory());
    ExecutorService executorService = Executors.newFixedThreadPool(executor.parallelism);

    List<List<Long>> partitions = Lists.partition(fichierIds, executor.batchSize);
    for (List<Long> partition : partitions) {
      executorService.submit(
          () -> processMovePartition(monitor, runMode, targetStorageUnit, partition));
    }

    try {
      executorService.shutdown();
      while (!executorService.awaitTermination(executor.printDelayInSeconds, TimeUnit.SECONDS)) {
        LOGGER.info("Current status...");
        monitor.printShortStatus();
      }
      LOGGER.info("Process finished.");
      monitor.printStatus();
    } catch (InterruptedException e) {
      LOGGER.error("Processing interrupted. Stopping...");
      executorService.shutdownNow();
      LOGGER.error("Processing interrupted. Stopped.");
      Thread.currentThread().interrupt();
    }
  }

  protected ArchivingProgressMonitor createMonitor(int total) {
    try {
      return new ArchivingProgressMonitor(total);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /** Load {@link Fichier} ids from a query. */
  protected List<Long> loadFichierIdsFromQuery(Path queryFile) {
    try {
      String query = Files.readString(queryFile, StandardCharsets.UTF_8);
      return entityManagerHelper.doWithReadOnlyTransaction(
          e -> {
            @SuppressWarnings("unchecked")
            List<Long> result = e.createNativeQuery(query, Long.class).getResultList();
            return result;
          });
    } catch (IOException e) {
      throw new ConfigurationException("Query cannot be extracted from %s".formatted(queryFile), e);
    } catch (DataAccessException e) {
      throw new ConfigurationException(
          "Ids cannot be loaded from query loaded from file %s".formatted(queryFile), e);
    }
  }

  /** Move files. */
  protected void processMovePartition(
      ArchivingProgressMonitor monitor,
      RunMode runMode,
      StorageUnit targetStorageUnit,
      List<Long> fichierIds) {
    try {
      // load
      List<Fichier> fichiers =
          entityManagerHelper.doWithReadWriteTransaction(e -> listFichiers(e, fichierIds));

      // move
      Map<Long, MoveResult> results = Maps.newHashMap();
      for (Fichier fichier : fichiers) {
        results.put(fichier.getId(), move(monitor, runMode, targetStorageUnit, fichier));
      }

      transactionalUpdateAndFeedback(monitor, runMode, targetStorageUnit, results);

    } catch (RuntimeException e) {
      LOGGER.error("Error during batch processing.", e);
    }
  }

  protected void transactionalUpdateAndFeedback(
      ArchivingProgressMonitor monitor,
      RunMode runMode,
      StorageUnit targetStorageUnit,
      Map<Long, MoveResult> results) {
    // update database accordingly
    try {
      if (RunMode.REAL.equals(runMode)) {
        entityManagerHelper.doWithReadWriteTransaction(
            e -> updateFichiersMove(e, targetStorageUnit, results));
      } else {
        entityManagerHelper.doWithReadOnlyTransaction(
            e -> updateFichiersMove(e, targetStorageUnit, results));
      }
      monitor.databaseSuccess(results);
    } catch (RuntimeException e) {
      monitor.databaseFailed(results);
    }
  }

  protected List<Fichier> listFichiers(EntityManager entityManager, Collection<Long> fichierIds) {
    TypedQuery<Fichier> query =
        entityManager.createQuery("SELECT f FROM Fichier f WHERE f.id IN (:ids)", Fichier.class);
    query.setParameter("ids", fichierIds);
    return query.getResultList();
  }

  protected boolean updateFichiersMove(
      EntityManager entityManager,
      StorageUnit detachedSargetStorageUnit,
      Map<Long, MoveResult> results) {
    StorageUnit freshStorageUnit =
        databaseOperations.getStorageUnit(detachedSargetStorageUnit.getId());
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

  /**
   * Move <code>fichier</code> and provides {@link MoveResult} accordingly. <code>monitor</code> is
   * called for interactive feedback.
   *
   * @see MoveResult
   */
  protected MoveResult move(
      ArchivingProgressMonitor monitor,
      RunMode runMode,
      StorageUnit targetStorageUnit,
      Fichier fichier) {
    StorageUnit sourceStorageUnit = fichier.getStorageUnit();
    IFichierPathStrategy sourcePathStrategy = sourceStorageUnit.getType().getFichierPathStrategy();
    Path source = Path.of(sourceStorageUnit.getPath(), sourcePathStrategy.getPath(fichier));
    IFichierPathStrategy targetPathStrategy = targetStorageUnit.getType().getFichierPathStrategy();
    Path target = Path.of(targetStorageUnit.getPath(), targetPathStrategy.getPath(fichier));
    // item is already archived (file may be missing !)
    if (fichier.getStorageUnit().equals(targetStorageUnit)) {
      if (source.toFile().exists()) {
        monitor.alreadyMoved();
      } else {
        monitor.missing(fichier);
      }
      return MoveResult.UNTOUCHED;
    }
    // file is already at target location, database need an update
    if (target.toFile().exists()) {
      monitor.alreadyMoved();
      return MoveResult.ALREADY_MOVED;
    }
    // source file is missing
    if (!source.toFile().exists()) {
      monitor.missing(fichier);
      return MoveResult.MISSING;
    }
    // file must be moved
    try {
      if (RunMode.REAL.equals(runMode)) {
        FileUtils.forceMkdirParent(target.getParent().toFile());
        FileUtils.moveFile(source.toFile(), target.toFile());
      }
      monitor.moved();
      return MoveResult.MOVED;
    } catch (IOException | RuntimeException e) {
      monitor.moveFailed();
      return MoveResult.MOVE_FAILED;
    }
  }

  public enum MoveResult {
    /** source == target, no move operation done. File may be missing. */
    UNTOUCHED,
    /** File is successfully moved. Database update is needed. */
    MOVED,
    /** File is already at target location. Database update is needed. */
    ALREADY_MOVED,
    /** File is missing. */
    MISSING,
    /** Move operation failed. */
    MOVE_FAILED;
  }

  public enum RunMode {
    REAL,
    DRY_RUN;
  }
}
