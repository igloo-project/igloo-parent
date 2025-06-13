package org.igloo.storage.tools.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.tools.ConfigurationException;
import org.igloo.storage.tools.commands.ExecutorMixin;
import org.igloo.storage.tools.util.action.DbCheckOrCreateStorageUnitAction;
import org.igloo.storage.tools.util.action.DbListFichiersAction;
import org.igloo.storage.tools.util.action.DbLoadFichierIdsAction;
import org.igloo.storage.tools.util.action.DbMoveFichiersAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

public class FichierUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(FichierUtil.class);

  private final EntityManagerHelper entityManagerHelper;
  private final FsUtil fsUtil;

  public FichierUtil(EntityManagerHelper entityManagerHelper, FsUtil fsUtil) {
    this.entityManagerHelper = entityManagerHelper;
    this.fsUtil = fsUtil;
  }

  /**
   * Load {@link Fichier} ids from a provided list or a query file. Ids are put in a set to detect
   * duplicates.
   */
  public List<Long> loadFichierIdsFromArgs(Path queryFile, List<Long> paramFichiersIds) {
    final List<Long> fichierIds = new ArrayList<>();
    if (paramFichiersIds != null && !paramFichiersIds.isEmpty()) {
      fichierIds.addAll(paramFichiersIds);
    } else {
      fichierIds.addAll(loadFichierIdsFromQuery(queryFile));
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
        new DbCheckOrCreateStorageUnitAction(path, type));
  }

  /** Move files. */
  public void processMove(
      ExecutorMixin executor,
      RunMode runMode,
      SwitchToUnavailable switchToUnavailable,
      StorageUnit targetStorageUnit,
      List<Long> fichierIds) {
    processMove(
        executor,
        runMode,
        switchToUnavailable,
        targetStorageUnit,
        fichierIds,
        this::processMovePartition);
  }

  /** Move files. */
  @VisibleForTesting
  public void processMove(
      ExecutorMixin executor,
      RunMode runMode,
      SwitchToUnavailable switchToUnavailable,
      StorageUnit targetStorageUnit,
      List<Long> fichierIds,
      ExecutorCallback callback) {
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
          () ->
              callback.processMovePartition(
                  monitor, runMode, switchToUnavailable, targetStorageUnit, partition));
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
      return entityManagerHelper.doWithReadOnlyTransaction(new DbLoadFichierIdsAction(query));
    } catch (IOException e) {
      throw new ConfigurationException("Query cannot be extracted from %s".formatted(queryFile), e);
    } catch (DataAccessException e) {
      throw new ConfigurationException(
          "Ids cannot be loaded from query loaded from file %s".formatted(queryFile), e);
    }
  }

  /** Move files. */
  @VisibleForTesting
  public void processMovePartition(
      ArchivingProgressMonitor monitor,
      RunMode runMode,
      SwitchToUnavailable switchToUnavailable,
      StorageUnit targetStorageUnit,
      List<Long> fichierIds) {
    try {
      // load
      List<Fichier> fichiers =
          entityManagerHelper.doWithReadOnlyTransaction(new DbListFichiersAction(fichierIds));

      // move
      Map<Long, MoveResult> results = Maps.newHashMap();
      for (Fichier fichier : fichiers) {
        results.put(fichier.getId(), fsUtil.move(monitor, runMode, targetStorageUnit, fichier));
      }

      transactionalUpdateAndFeedback(
          monitor, runMode, switchToUnavailable, targetStorageUnit, results);

    } catch (RuntimeException e) {
      LOGGER.error("Error during batch processing.", e);
    }
  }

  protected void transactionalUpdateAndFeedback(
      ArchivingProgressMonitor monitor,
      RunMode runMode,
      SwitchToUnavailable switchToUnavailable,
      StorageUnit targetStorageUnit,
      Map<Long, MoveResult> results) {
    // update database accordingly
    try {
      DbMoveFichiersAction action =
          new DbMoveFichiersAction(switchToUnavailable, targetStorageUnit, results);
      if (RunMode.REAL.equals(runMode)) {
        entityManagerHelper.doWithReadWriteTransaction(action);
      } else {
        entityManagerHelper.doWithReadOnlyTransaction(action);
      }
      monitor.databaseSuccess(results);
    } catch (RuntimeException e) {
      monitor.databaseFailed(results);
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

  public enum SwitchToUnavailable {
    YES,
    NO;
  }

  @VisibleForTesting
  public interface ExecutorCallback {
    void processMovePartition(
        ArchivingProgressMonitor monitor,
        RunMode runMode,
        SwitchToUnavailable switchToUnavailable,
        StorageUnit targetStorageUnit,
        List<Long> fichierIds);
  }
}
