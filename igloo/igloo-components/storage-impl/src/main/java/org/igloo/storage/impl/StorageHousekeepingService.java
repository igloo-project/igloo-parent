package org.igloo.storage.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.igloo.storage.api.IStorageHousekeepingService;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

public class StorageHousekeepingService implements IStorageHousekeepingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageHousekeepingService.class);

  private final TransactionOperations readOnlyTransaction;
  private final TransactionOperations writeTransaction;
  private final IStorageService storageService;
  private final DatabaseOperations databaseOperations;

  /** Expected duration since last check to relaunch a basic check */
  private final Duration defaultCheckDelay;

  /** Expected duration since last checksum to relaunch a checksum check */
  private final Duration defaultCheckChecksumDelay;

  /** Expected duration since creation to trigger transient file deletion */
  private final Duration transientCleaningDelay;

  /** database limit for {@link Fichier} listing when performing cleaning tasks */
  private final Integer cleanLimit;

  /**
   * {@link StorageUnit} limit for consistency check. It allows to smooth consistency check jobs.
   * <code>null</code> for unlimited.
   */
  private final Integer consistencyStorageUnitLimit;

  /** Manage exclusive jobs */
  private final Semaphore jobSemaphore = new Semaphore(1, true);

  public StorageHousekeepingService(
      PlatformTransactionManager transactionManager,
      IStorageService storageService,
      DatabaseOperations databaseOperations) {
    this(
        transactionManager,
        storageService,
        databaseOperations,
        Duration.ofDays(10),
        Duration.ofDays(30),
        Duration.ofDays(1),
        500,
        null);
  }

  /**
   * Perform housekeeping jobs for storage module.
   *
   * <ul>
   *   <li>Consistency check (with/without checksum) and reporting
   *   <li>{@link StorageUnit} creation
   *   <li>Invalidated file cleaning
   *   <li>Transient outdated file cleaning
   * </ul>
   *
   * @param transactionManager if null, this service does nothing to manage transaction. Else
   *     database calls are wrapped in transactions.
   * @param storageService
   * @param databaseOperations
   */
  public StorageHousekeepingService(
      PlatformTransactionManager transactionManager,
      IStorageService storageService,
      DatabaseOperations databaseOperations, // NOSONAR
      Duration defaultCheckDelay,
      Duration defaultCheckChecksumDelay,
      Duration transientCleaningDelay,
      Integer cleanLimit,
      Integer consistencyStorageUnitLimit) {
    checkConsistencyStorageUnitLimit(consistencyStorageUnitLimit);
    if (transactionManager != null) {
      DefaultTransactionAttribute writeAttribute = new DefaultTransactionAttribute();
      writeAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      writeAttribute.setReadOnly(false);
      writeTransaction = new TransactionTemplate(transactionManager);
      DefaultTransactionAttribute readAttribute = new DefaultTransactionAttribute();
      readAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      readAttribute.setReadOnly(true);
      readOnlyTransaction = new TransactionTemplate(transactionManager, readAttribute);
    } else {
      LOGGER.warn("StorageHousekeepingService initialized without transactionManager");
      writeTransaction = TransactionOperations.withoutTransaction();
      readOnlyTransaction = TransactionOperations.withoutTransaction();
    }
    this.storageService = storageService;
    this.databaseOperations = databaseOperations;
    this.defaultCheckDelay = defaultCheckDelay;
    this.defaultCheckChecksumDelay = defaultCheckChecksumDelay;
    this.transientCleaningDelay = transientCleaningDelay;
    this.cleanLimit = cleanLimit;
    this.consistencyStorageUnitLimit = consistencyStorageUnitLimit;
  }

  @Override
  public void housekeeping(boolean consistencyDisabled, boolean splitStorageUnitDisabled) {
    try {
      doExclusiveJob(
          () -> {
            if (!consistencyDisabled) {
              checkConsistency(defaultCheckDelay, defaultCheckChecksumDelay);
              checkInterruption();
            }
            if (!splitStorageUnitDisabled) {
              splitStorageUnits();
            }
          },
          "Housekeeping");
    } catch (Interruption e) {
      LOGGER.warn("Housekeeping job gracefully interrupted.");
    }
  }

  @Override
  public void cleaning(boolean cleaningInvalidatedDisabled, boolean cleaningTransientDisabled) {
    try {
      if (!cleaningTransientDisabled) {
        doExclusiveJob(this::cleanTransient, "Cleaning transient");
        checkInterruption();
      }
      if (!cleaningInvalidatedDisabled) {
        doExclusiveJob(this::cleanInvalidated, "Cleaning invalidated");
      }
    } catch (Interruption e) {
      LOGGER.warn("Cleaning job gracefully interrupted.");
    }
  }

  private void doExclusiveJob(Runnable runnable, Object jobName) {
    boolean acquire = false;
    try {
      acquire = jobSemaphore.tryAcquire(30, TimeUnit.MINUTES);
      if (acquire) {
        runnable.run();
      } else {
        LOGGER.error(
            "Job {} skipped as another job is using exclusive lock. Aborted after a 30 minutes wait. Please check job scheduling.",
            jobName);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (acquire) {
        jobSemaphore.release();
      }
    }
  }

  public void cleanInvalidated() {
    clean(() -> readOnlyTransaction.execute(t -> databaseOperations.listInvalidated(cleanLimit)));
  }

  public void cleanTransient() {
    LocalDateTime maxCreationDate = LocalDateTime.now().minus(transientCleaningDelay);
    clean(
        () ->
            readOnlyTransaction.execute(
                t -> databaseOperations.listTransient(cleanLimit, maxCreationDate)));
  }

  public void clean(Supplier<List<Fichier>> source) throws Interruption {
    List<Fichier> fichiers = source.get();
    for (Fichier fichier : fichiers) {
      try {
        writeTransaction.executeWithoutResult(t -> storageService.removeFichier(fichier));
      } catch (RuntimeException e) {
        LOGGER.error("Error removing {}.", fichier, e);
      }
      checkInterruption();
    }
  }

  /**
   * Create automatically a new {@link StorageUnit} for overflowing and auto-creation enabled {@link
   * StorageUnit}
   */
  public void splitStorageUnits() {
    List<StorageUnit> storageUnits =
        readOnlyTransaction.execute(t -> databaseOperations.listStorageUnitsToSplit());
    checkInterruption();
    for (StorageUnit storageUnit : storageUnits) {
      LOGGER.info("Splitting StorageUnit {}", storageUnit);
      storageService.splitStorageUnit(storageUnit);
      checkInterruption();
    }
  }

  public StorageUnitCheckType checkConsistencyType(
      StorageUnit unit, Duration defaultCheckDelay, Duration defaultCheckChecksumDelay) {
    return StorageHousekeepingService.getCheckConsistencyType(
        unit::toString,
        LocalDateTime.now(),
        unit::getCheckType,
        // last check may be null
        () ->
            readOnlyTransaction.execute(
                t ->
                    Optional.ofNullable(databaseOperations.getLastCheck(unit))
                        .map(StorageConsistencyCheck::getCheckFinishedOn)
                        .orElse(null)),
        () ->
            readOnlyTransaction.execute(
                t ->
                    Optional.ofNullable(databaseOperations.getLastCheckChecksum(unit))
                        .map(StorageConsistencyCheck::getCheckFinishedOn)
                        .orElse(null)),
        () -> Optional.ofNullable(unit.getCheckDelay()).orElse(defaultCheckDelay),
        () -> Optional.ofNullable(unit.getCheckChecksumDelay()).orElse(defaultCheckChecksumDelay));
  }

  /**
   * Perform whole consistency check:
   *
   * <ul>
   *   <li>List all consistency check enabled {@link StorageUnit}
   *   <li>Check if consistency check is needed: can be disabled or recent enough not to be
   *       rechecked
   *   <li>Perform consistency check if needed
   * </ul>
   *
   * @see StorageUnit#getCheckType()
   * @see StorageUnit#getCheckDelay()
   * @see StorageUnit#getCheckChecksumDelay()
   */
  public void checkConsistency(Duration defaultCheckDelay, Duration defaultCheckChecksumDelay) {
    checkConsistency(
        consistencyStorageUnitLimit,
        () -> readOnlyTransaction.execute(t -> databaseOperations.listStorageUnits()),
        unit -> checkConsistencyType(unit, defaultCheckDelay, defaultCheckChecksumDelay),
        (unit, checksumEnabled) ->
            writeTransaction.executeWithoutResult(
                t -> storageService.checkConsistency(unit, checksumEnabled)));
  }

  /**
   * For a unit with automatic check enabled, verify history to decide if check must be performed
   * now, and if checksum check must be enabled.
   *
   * <p>Static and supplier arguments are used to allow testability.
   *
   * @param unitLog String used for logging message
   * @param checkType Expected check type for storage unit. Supplier result must be not null.
   * @param lastCheck Last check date for unit. Supplier result may be null (if no check exists).
   * @param lastCheckChecksum Last checksum-enabled check date for unit. Supplier result may be null
   *     (if no check exists).
   * @param checkDelay Delay for a basic check. Supplier result must be not null.
   * @param checkChecksumDelay Delay for a checksum check. Supplier result must be not null.
   * @see #checkConsistency(Duration, Duration)
   */
  public static StorageUnitCheckType getCheckConsistencyType(
      @Nonnull Supplier<String> unitLog,
      @Nonnull LocalDateTime reference,
      @Nonnull Supplier<StorageUnitCheckType> checkType,
      @Nonnull Supplier<LocalDateTime> lastCheck,
      @Nonnull Supplier<LocalDateTime> lastCheckChecksum,
      @Nonnull Supplier<Duration> checkDelay,
      @Nonnull Supplier<Duration> checkChecksumDelay) {
    // 3 steps:
    // - is check enabled ?
    // - is check old enough ?
    // - is checksum enabled and old enough to be rechecked
    if (StorageUnitCheckType.NONE.equals(checkType.get())) {
      LOGGER.info("Checking {} skipped. Disabled on StorageUnit", unitLog.get());
      return StorageUnitCheckType.NONE;
    } else {
      if (!isExpectedCheckDateElapsed(lastCheck, checkDelay, reference)) {
        // last check is recent enough
        LOGGER.debug(
            "Checking {} skipped. Last check on {}, next check expected with delay {}",
            unitLog.get(),
            lastCheck.get(),
            checkDelay.get());
        return StorageUnitCheckType.NONE;
      } else {
        if (StorageUnitCheckType.LISTING_SIZE_CHECKSUM.equals(checkType.get())
            && isExpectedCheckDateElapsed(lastCheckChecksum, checkChecksumDelay, reference)) {
          // checksum is needed and delay is elapsed
          LOGGER.debug(
              "Checking {} with checksum. Last checksum check on {}, next check expected with delay {}",
              unitLog.get(),
              lastCheckChecksum.get(),
              checkChecksumDelay.get());
          return StorageUnitCheckType.LISTING_SIZE_CHECKSUM;
        }
        // checksum recent enough; only perform a simple check
        return StorageUnitCheckType.LISTING_SIZE;
      }
    }
  }

  /** Static to ensure method is stateless and testable. */
  public static boolean isExpectedCheckDateElapsed(
      Supplier<LocalDateTime> lastCheck, Supplier<Duration> delay, LocalDateTime reference) {
    return Optional.ofNullable(lastCheck.get())
        .map(d -> d.plus(delay.get()))
        .map(reference::isAfter)
        .orElse(true);
  }

  public static void checkConsistency(
      Integer consistencyStorageUnitLimit,
      Supplier<List<StorageUnit>> storageUnitsSupplier,
      Function<StorageUnit, StorageUnitCheckType> storageUnitCheckPolicySupplier,
      BiConsumer<StorageUnit, Boolean> storageUnitChecker) {
    checkConsistencyStorageUnitLimit(consistencyStorageUnitLimit);
    List<StorageUnit> units = storageUnitsSupplier.get();
    int batchSize = 0;
    for (StorageUnit unit : units) {
      LOGGER.debug("Checking {}. Verifying if check must be processed", unit);
      StorageUnitCheckType typeNeeded = storageUnitCheckPolicySupplier.apply(unit);
      if (StorageUnitCheckType.NONE.equals(typeNeeded)) {
        LOGGER.info("Checking {} skipped. Disabled on StorageUnit", unit);
      } else if (consistencyStorageUnitLimit != null && batchSize >= consistencyStorageUnitLimit) {
        LOGGER.info("Consistency check ignored due to configured limit");
      } else {
        boolean checksumEnabled = StorageUnitCheckType.LISTING_SIZE_CHECKSUM.equals(typeNeeded);
        LOGGER.info("Checking {}. Processing (checksum={})", unit, checksumEnabled);
        storageUnitChecker.accept(unit, checksumEnabled);
        LOGGER.info("Checking {}. Done", unit);
        batchSize++;
      }
      checkInterruption();
    }
  }

  private static void checkConsistencyStorageUnitLimit(Integer consistencyStorageUnitLimit) {
    if (consistencyStorageUnitLimit != null && consistencyStorageUnitLimit <= 0) {
      throw new IllegalStateException(
          "consistencyStorageUnitLimit must be strictly positive or null");
    }
  }

  public static void checkInterruption() {
    if (Thread.currentThread().isInterrupted()) {
      throw new Interruption();
    }
  }

  private static class Interruption extends RuntimeException {

    private static final long serialVersionUID = -8913127134427780961L;

    public Interruption() {
      super();
    }
  }
}
