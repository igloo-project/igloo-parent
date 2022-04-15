package org.igloo.storage.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.igloo.storage.api.IStorageHousekeepingService;
import org.igloo.storage.api.IStorageService;
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
	private final Duration defaultCheckDelay = Duration.ofDays(10);
	private final Duration defaultCheckChecksumDelay = Duration.ofDays(30);

	/**
	 * Perform housekeeping jobs for storage module.
	 * 
	 * <ul>
	 * <li>Consistency check (with/without checksum) and reporting</li>
	 * <li>{@link StorageUnit} creation</li>
	 * <li>Invalidated file cleaning</li>
	 * <li>Transient outdated file cleaning</li>
	 * </ul>
	 * 
	 * @param transactionManager if null, this service does nothing to manage transaction. Else database calls are
	 *        wrapped in transactions.
	 * @param storageService
	 * @param databaseOperations
	 */
	public StorageHousekeepingService(PlatformTransactionManager transactionManager, IStorageService storageService, DatabaseOperations databaseOperations) {
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
	}

	@Override
	public void housekeeping() {
		checkConsistency(defaultCheckDelay, defaultCheckChecksumDelay);
		splitStorageUnits();
	}

	/**
	 * Create automatically a new {@link StorageUnit} for overflowing and auto-creation enabled {@link StorageUnit}
	 * 
	 * TODO: add @see
	 */
	public void splitStorageUnits() {
		
	}

	public StorageUnitCheckType checkConsistencyType(StorageUnit unit, Duration defaultCheckDelay,
			Duration defaultCheckChecksumDelay) {
		return StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				LocalDateTime.now(),
				unit::getCheckType,
				() -> readOnlyTransaction.execute(t -> databaseOperations.getLastCheck(unit).getCheckFinishedOn()),
				() -> readOnlyTransaction.execute(t -> databaseOperations.getLastCheckChecksum(unit).getCheckFinishedOn()),
				() -> Optional.ofNullable(unit.getCheckDelay()).orElse(defaultCheckDelay),
				() -> Optional.ofNullable(unit.getCheckChecksumDelay()).orElse(defaultCheckChecksumDelay)
		);
	}

	/**
	 * <p>For a unit with automatic check enabled, verify history to decide if check must be performed now, and if
	 * checksum check must be enabled.</p>
	 * 
	 * <p>Static and supplier arguments are used to allow testability.</p>
	 * 
	 * @param unitLog String used for logging message
	 * @param checkType Expected check type for storage unit. Supplier result must be not null.
	 * @param lastCheck Last check date for unit. Supplier result may be null (if no check exists).
	 * @param lastCheckChecksum Last checksum-enabled check date for unit. Supplier result may be null (if no check exists).
	 * @param checkDelay Delay for a basic check. Supplier result must be not null. 
	 * @param checkChecksumDelay Delay for a checksum check. Supplier result must be not null.
	 * @see #checkConsistency(Duration, Duration)
	 */
	public static StorageUnitCheckType getCheckConsistencyType(@Nonnull Supplier<String> unitLog,
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
				LOGGER.debug("Checking {} skipped. Last check on {}, next check expected with delay {}", unitLog.get(), lastCheck.get(), checkDelay.get());
				return StorageUnitCheckType.NONE;
			} else {
				if (StorageUnitCheckType.LISTING_SIZE_CHECKSUM.equals(checkType.get()) && isExpectedCheckDateElapsed(lastCheckChecksum, checkChecksumDelay, reference)) {
					// checksum is needed and delay is elapsed
					LOGGER.debug("Checking {} with checksum. Last checksum check on {}, next check expected with delay {}", unitLog.get(), lastCheckChecksum.get(), checkChecksumDelay.get());
					return StorageUnitCheckType.LISTING_SIZE_CHECKSUM;
				}
				// checksum recent enough; only perform a simple check
				return StorageUnitCheckType.LISTING_SIZE;
			}
		}
	}

	/**
	 * <p>Static to ensure method is stateless and testable.</p>
	 */
	public static boolean isExpectedCheckDateElapsed(Supplier<LocalDateTime> lastCheck, Supplier<Duration> delay, LocalDateTime reference) {
		return Optional.ofNullable(lastCheck.get()).map(d -> d.plus(delay.get())).map(reference::isAfter).orElse(true);
	}

	/**
	 * Perform whole consistency check:
	 * <ul>
	 * <li>List all consistency check enabled {@link StorageUnit}</li>
	 * <li>Check if consistency check is needed: can be disabled or recent enough not to be rechecked</li>
	 * <li>Perform consistency check if needed</li>
	 * </ul>
	 * 
	 * @see StorageUnit#getCheckType()
	 * @see StorageUnit#getCheckDelay()
	 * @see StorageUnit#getCheckChecksumDelay()
	 */
	public void checkConsistency(Duration defaultCheckDelay, Duration defaultCheckChecksumDelay) {
		List<StorageUnit> units = readOnlyTransaction.execute(t -> databaseOperations.listStorageUnits());
		for (StorageUnit unit : units) {
			LOGGER.debug("Checking {}. Verifying if check must be processed", unit);
			StorageUnitCheckType typeNeeded = checkConsistencyType(unit, defaultCheckDelay, defaultCheckChecksumDelay);
			if (StorageUnitCheckType.NONE.equals(typeNeeded)) {
				LOGGER.info("Checking {} skipped. Disabled on StorageUnit", unit);
			} else {
				boolean checksumEnabled = StorageUnitCheckType.LISTING_SIZE_CHECKSUM.equals(typeNeeded);
				LOGGER.info("Checking {}. Processing (checksum={})", unit, checksumEnabled);
				writeTransaction.execute(t -> storageService.checkConsistency(unit, checksumEnabled));
				LOGGER.info("Checking {}. Done", unit);
			}
		}
	}
}
