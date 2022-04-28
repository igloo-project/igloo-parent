package org.igloo.storage.integration;

import java.time.Duration;
import java.util.Set;

import org.igloo.storage.model.atomic.IStorageUnitType;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.springframework.scheduling.support.CronTrigger;

public class StoragePropertyIds extends AbstractPropertyIds {

	public static final ImmutablePropertyId<String> PATH = immutable("storage.path");
	public static final ImmutablePropertyId<Integer> TRANSACTION_SYNCHRONIZATION_ORDER = immutable("storage.transactionSynchronizationOrder");
	public static final ImmutablePropertyId<String> DB_FICHIER_SEQUENCE_NAME = immutable("storage.db.fichierSequenceName");
	public static final ImmutablePropertyId<String> DB_STORAGE_UNIT_SEQUENCE_NAME = immutable("storage.db.storageUnitSequenceName");
	public static final ImmutablePropertyId<Set<IStorageUnitType>> STORAGE_UNIT_TYPE_CANDIDATES = immutable("storage.storageUnitTypeCandidates");
	public static final ImmutablePropertyId<Boolean> JOB_DISABLED = immutable("storage.job.disabled");
	public static final ImmutablePropertyId<CronTrigger> JOB_CLEANING_CRON = immutable("storage.job.cleaning.cron");
	public static final ImmutablePropertyId<CronTrigger> JOB_HOUSEKEEPING_CRON = immutable("storage.job.housekeeping.cron");
	public static final ImmutablePropertyId<Duration> JOB_CHECK_DEFAULT_DELAY = immutable("storage.job.check.defaultDelay");
	public static final ImmutablePropertyId<Duration> JOB_CHECK_CHECKSUM_DEFAULT_DELAY = immutable("storage.job.checkChecksum.defaultDelay");
	public static final ImmutablePropertyId<Duration> JOB_CLEAN_TRANSIENT_DELAY = immutable("storage.job.cleanTransient.delay");
	public static final ImmutablePropertyId<Integer> JOB_CLEAN_LIMIT = immutable("storage.job.clean.limit");

}
