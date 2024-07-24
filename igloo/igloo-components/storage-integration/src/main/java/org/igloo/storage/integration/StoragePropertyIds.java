package org.igloo.storage.integration;

import java.time.Duration;
import java.util.Set;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.springframework.scheduling.support.CronTrigger;

public class StoragePropertyIds extends AbstractPropertyIds {

  /** Root path for new storage unit creation */
  public static final ImmutablePropertyId<String> PATH = immutable("storage.path");

  /** Mount path for web resource. */
  public static final ImmutablePropertyId<String> WEB_URL = immutable("storage.web.url");

  /** Mount path for web resource (download variant). */
  public static final ImmutablePropertyId<String> WEB_DOWNLOAD_URL =
      immutable("storage.web.downloadUrl");

  /**
   * Order to add storage transaction synchronization. This synchronization remove added file on
   * rollback event, and perform effective removal of delete file on commit event. Keep default
   * should be fine.
   */
  public static final ImmutablePropertyId<Integer> TRANSACTION_SYNCHRONIZATION_ORDER =
      immutable("storage.transactionSynchronizationOrder");

  /** Sequence to use for Fichier creation. Project is responsible for sequence creation. */
  public static final ImmutablePropertyId<String> DB_FICHIER_SEQUENCE_NAME =
      immutable("storage.db.fichierSequenceName");

  /** Sequence to use for StorageUnit creation. Project is responsible for sequence creation. */
  public static final ImmutablePropertyId<String> DB_STORAGE_UNIT_SEQUENCE_NAME =
      immutable("storage.db.storageUnitSequenceName");

  /** {@link IStorageUnitType} candidates to consider when a new file is added. */
  public static final ImmutablePropertyId<Set<IStorageUnitType>> STORAGE_UNIT_TYPE_CANDIDATES =
      immutable("storage.storageUnitTypeCandidates");

  /** Disable all jobs; no trigger is created. */
  public static final ImmutablePropertyId<Boolean> JOB_DISABLED = immutable("storage.job.disabled");

  /** Disable transient cleaning job. */
  public static final ImmutablePropertyId<Boolean> JOB_CLEANING_TRANSIENT_DISABLED =
      immutable("storage.job.cleaningTransient.disabled");

  /** Disable invalidated cleaning job. */
  public static final ImmutablePropertyId<Boolean> JOB_CLEANING_INVALIDATED_DISABLED =
      immutable("storage.job.cleaningInvalidated.disabled");

  /** Disable consistency check job. */
  public static final ImmutablePropertyId<Boolean> JOB_CONSISTENCY_DISABLED =
      immutable("storage.job.consistency.disabled");

  /** Disable {@link StorageUnit} split job. */
  public static final ImmutablePropertyId<Boolean> JOB_SPLIT_STORAGE_UNIT_DISABLED =
      immutable("storage.job.splitStorageUnit.disabled");

  /** Cron for cleaning job (transient and invalidated removal). */
  public static final ImmutablePropertyId<CronTrigger> JOB_CLEANING_CRON =
      immutable("storage.job.cleaning.cron");

  /** Cron for housekeeping (consistency and {@link StorageUnit} split). */
  public static final ImmutablePropertyId<CronTrigger> JOB_HOUSEKEEPING_CRON =
      immutable("storage.job.housekeeping.cron");

  /**
   * Maximum number of {@link StorageUnit} to check for a job. This allows to smooth {@link
   * StorageUnit} check (not checked {@link StorageUnit} will be processed in a later run). 0 for
   * unlimited.
   */
  public static final ImmutablePropertyId<Integer> JOB_CONSISTENCY_STORAGE_UNIT_LIMIT =
      immutable("storage.job.consistency.storageUnitLimit");

  /**
   * Check delay to use if {@link StorageUnit#getCheckDelay()} is null. This controls consistency
   * trigger.
   */
  public static final ImmutablePropertyId<Duration> JOB_CHECK_DEFAULT_DELAY =
      immutable("storage.job.check.defaultDelay");

  /**
   * Check delay to use if {@link StorageUnit#getCheckChecksumDelay()} is null. This controls
   * checksum consistency trigger.
   */
  public static final ImmutablePropertyId<Duration> JOB_CHECK_CHECKSUM_DEFAULT_DELAY =
      immutable("storage.job.checkChecksum.defaultDelay");

  /**
   * Delay (from creation date) before transient cleaning. It ensures that transient files are
   * removed after a minimal delay.
   */
  public static final ImmutablePropertyId<Duration> JOB_CLEAN_TRANSIENT_DELAY =
      immutable("storage.job.cleanTransient.delay");

  /** Batch size for cleaning jobs. */
  public static final ImmutablePropertyId<Integer> JOB_CLEAN_LIMIT =
      immutable("storage.job.clean.limit");

  /** Enable monitoring stack. */
  public static final ImmutablePropertyId<Boolean> MONITORING_ENABLED =
      immutable("storage.monitoring.enabled");

  /** Enable monitoring pages. */
  public static final ImmutablePropertyId<Boolean> MONITORING_WICKET_ENABLED =
      immutable("storage.monitoring.wicket.enabled");
}
