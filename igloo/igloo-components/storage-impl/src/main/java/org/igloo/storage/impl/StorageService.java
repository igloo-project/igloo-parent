package org.igloo.storage.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.igloo.storage.api.IMimeTypeResolver;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageConsistencyCheckResult;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.CountingInputStream;

public class StorageService implements IStorageService, IStorageTransactionResourceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

	private static final Class<?> EVENTS_RESOURCE_KEY = StorageService.class;

	private final StorageOperations storageOperations;
	private final StorageTransactionHandler handler;
	private final Set<IStorageUnitType> storageUnitTypeCandidates;
	private final IMimeTypeResolver mimeTypeResolver;
	private final Supplier<Path> storageUnitPathSupplier;
	private final DatabaseOperations databaseOperations;
	private final StorageTransactionAdapter adapter;
	private final Duration defaultCheckDelay = Duration.ofDays(10);
	private final Duration defaultCheckChecksumDelay = Duration.ofDays(30);

	public StorageService(Integer transactionSynchronizationOrder, DatabaseOperations databaseOperations, Set<IStorageUnitType> storageUnitTypeCandidates, StorageOperations operations, Supplier<Path> storageUnitPathSupplier) {
		this(transactionSynchronizationOrder, databaseOperations, storageUnitTypeCandidates, operations, storageUnitPathSupplier, new MimeTypeResolver());
	}

	public StorageService(Integer transactionSynchronizationOrder, DatabaseOperations databaseOperations, Set<IStorageUnitType> storageUnitTypeCandidates, StorageOperations operations, Supplier<Path> storageUnitPathSupplier, IMimeTypeResolver mimeTypeResolver) {
		this.databaseOperations = databaseOperations;
		this.storageUnitTypeCandidates = storageUnitTypeCandidates;
		this.mimeTypeResolver = mimeTypeResolver;
		this.storageOperations = operations;
		this.storageUnitPathSupplier = storageUnitPathSupplier;
		handler = new StorageTransactionHandler(operations);
		adapter = new StorageTransactionAdapter(transactionSynchronizationOrder, handler, this);
	}

	@Override
	@Nonnull
	public Fichier addFichier(@Nonnull String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream) {
		StorageUnit unit = selectStorageUnit(fichierType);
		Fichier fichier = new Fichier();
		fichier.setId(databaseOperations.generateFichier());
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.TRANSIENT);
		fichier.setType(fichierType);
		fichier.setStorageUnit(unit);
		fichier.setName(filename);
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setMimetype(mimeTypeResolver.resolve(fichier.getFilename()));
		fichier.setCreationDate(LocalDateTime.now());

		fichier.setRelativePath(unit.getType().getFichierPathStrategy().getPath(fichier));
		Path absolutePath = getAbsolutePath(fichier);
		try (
				HashingInputStream hashingInputStream = new HashingInputStream(Hashing.sha256(), inputStream);
				CountingInputStream countingInputStream = new CountingInputStream(hashingInputStream)) {
			storageOperations.copy(countingInputStream, absolutePath);
			fichier.setChecksum(hashingInputStream.hash().toString());
			fichier.setSize(countingInputStream.getCount());
		} catch (IOException e) {
			// TODO use custom runtime exception
			throw new IllegalStateException(e);
		}
		databaseOperations.createFichier(fichier);

		addEvent(fichier.getId(), StorageEventType.ADD, absolutePath);
		return fichier;
	}

	@Override
	public void validateFichier(@Nonnull Fichier fichier) {
		if (Objects.equals(fichier.getStatus(), FichierStatus.TRANSIENT)) {
			fichier.setStatus(FichierStatus.ALIVE);
			fichier.setValidationDate(LocalDateTime.now());
		} else {
			LOGGER.warn("[validate] Fichier {} is already marked ALIVE; no action", fichier.getId());
		}
	}

	@Override
	public void invalidateFichier(@Nonnull Fichier fichier) {
		if (Objects.equals(fichier.getStatus(), FichierStatus.ALIVE)) {
			fichier.setStatus(FichierStatus.INVALIDATED);
			fichier.setInvalidationDate(LocalDateTime.now());
		} else {
			LOGGER.warn("[invalidate] Fichier {} is already marked INVALIDATED; no action", fichier.getId());
		}
	}

	@Override
	public void removeFichier(@Nonnull Fichier fichier) {
		// TODO : vérifier qu'on est dans un état INVALIDATED ?
		databaseOperations.removeFichier(fichier);
		addEvent(fichier.getId(), StorageEventType.DELETE, getAbsolutePath(fichier));
	}

	@Override
	@Nonnull
	public File getFile(@Nonnull Fichier fichier) throws FileNotFoundException {
		// TODO : gérer file manquant
		return storageOperations.getFile(getAbsolutePath(fichier));
	}

	@Override
	@Nonnull
	public StorageUnit createStorageUnit(@Nonnull IStorageUnitType type) {
		StorageUnit unit = new StorageUnit();
		unit.setCreationDate(LocalDateTime.now());
		unit.setId(databaseOperations.generateStorageUnit());
		unit.setType(type);
		unit.setStatus(StorageUnitStatus.ALIVE);
		unit.setPath(storageUnitPathSupplier.get().toAbsolutePath()
			.resolve(String.format("%s-%s", type.getPath(), unit.getId().toString()))
				.toString());
		databaseOperations.createStorageUnit(unit);
		return unit;
	}

	// TODO MPI : à mettre dans un service spécifique aux vérifications de cohérence ?
	@Override
	@Nonnull
	public List<StorageConsistencyCheck> checkConsistency(@Nonnull StorageUnit unit, boolean checksumValidation) {
		// reload entity
		final StorageUnit persistedUnit = databaseOperations.getStorageUnit(unit.getId());
		List<StorageConsistencyCheck> consistencies = new ArrayList<>();
		StorageConsistencyCheck consistencyCheck = new StorageConsistencyCheck(persistedUnit);
		databaseOperations.createConsistencyCheck(consistencyCheck);
		
		// we need to collect all this data during check
		Integer fileCount = 0;
		Integer fichierCount = 0;
		Long fileSize = 0l;
		Long fichierSize;
		Integer missingFichierCount = 0;
		Integer missingFileCount = 0;
		Integer contentMismatchCount = 0;
		
		// list db and filesystem
		Set<Path> files = storageOperations.listUnitContent(persistedUnit);
		Map<Path, Fichier> result = databaseOperations.listUnitAliveFichiers(persistedUnit).stream()
				.collect(Collectors.toMap(f -> Path.of(persistedUnit.getPath()).resolve(f.getRelativePath()), Function.identity()));
		// get general data
		fileCount = files.size();
		fichierCount = result.size();
		fichierSize = result.values().stream().map(Fichier::getSize).reduce((a, b) -> a + b).orElse(0l);

		// perform two-way comparison
		Set<Path> missingEntities = Sets.difference(files, result.keySet());
		List<Fichier> missingFiles = Sets.difference(result.keySet(), files).stream().map(result::get).collect(Collectors.toUnmodifiableList());
		missingFichierCount = missingEntities.size();
		missingFileCount = missingFiles.size();
		
		// trigger missing entity (calculate fs size simultaneously)
		for (Path missingEntity : missingEntities) {
			StorageFailure failure = StorageFailure.ofMissingEntity(Path.of(persistedUnit.getPath()).resolve(missingEntity).toString(), consistencyCheck);
			databaseOperations.triggerFailure(failure);
			try {
				fileSize += storageOperations.length(missingEntity);
			} catch (FileNotFoundException|RuntimeException e) {
				LOGGER.error("Size check error on {}", missingEntity, e);
			}
		}
		// trigger missing file
		for (Fichier missingFile : missingFiles) {
			StorageFailure failure = StorageFailure.ofMissingFile(Path.of(persistedUnit.getPath()).resolve(missingFile.getRelativePath()), missingFile, consistencyCheck);
			databaseOperations.triggerFailure(failure);
		}
		
		// collect file/fichier pairs to perform basic or extended content check (size or size+checksum)
		Map<Path, Fichier> okEntitiesFiles = Sets.intersection(files, result.keySet()).stream().collect(Collectors.toMap(Function.identity(), result::get));
		
		for (Fichier fichier : okEntitiesFiles.values()) {
			Path filePath = Path.of(persistedUnit.getPath()).resolve(fichier.getRelativePath());
			try {
				// check size
				Long size = storageOperations.length(filePath);
				fileSize += size;
				if (!size.equals(fichier.getSize())) {
					databaseOperations.triggerFailure(StorageFailure.ofContentMismatch(filePath, fichier, consistencyCheck));
					contentMismatchCount += 1;
				} else if (checksumValidation && !ChecksumType.NONE.equals(fichier.getChecksumType())) {
					// conditionally perform checksum comparison
					String checksum = storageOperations.checksum(filePath);
					if (!checksum.equals(fichier.getChecksum())) {
						databaseOperations.triggerFailure(StorageFailure.ofContentMismatch(filePath, fichier, consistencyCheck));
						contentMismatchCount += 1;
					}
				}
			} catch (FileNotFoundException|RuntimeException e) {
				LOGGER.error("Content check error on {}/{}", fichier.getId(), fichier.getUuid(), e);
			}
		}

		// update consistency object
		consistencyCheck.setDbFichierCount(fichierCount);
		consistencyCheck.setFsFileCount(fileCount);
		consistencyCheck.setMissingFichierCount(missingFichierCount);
		consistencyCheck.setMissingFileCount(missingFileCount);
		consistencyCheck.setDbFichierSize(fichierSize);
		consistencyCheck.setFsFileSize(fileSize);
		consistencyCheck.setContentMismatchCount(contentMismatchCount);
		consistencyCheck.setCheckFinishedOn(LocalDateTime.now());
		if (consistencyCheck.getContentMismatchCount() > 0 || consistencyCheck.getMissingFichierCount() > 0 || consistencyCheck.getMissingFileCount() > 0) {
			consistencyCheck.setStatus(StorageConsistencyCheckResult.FAILED);
		} else {
			consistencyCheck.setStatus(StorageConsistencyCheckResult.OK);
		}
		consistencies.add(consistencyCheck);
		return consistencies;
	}

	@Override
	public void addEvent(Long id, StorageEventType type, Path path) {
		prepareAdapter().addEvent(id, type, path);
	}

	@Override
	public void housekeeping() {
		checkConsistency(defaultCheckDelay, defaultCheckChecksumDelay);
		splitStorageUnits();
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
		List<StorageUnit> units = databaseOperations.listStorageUnits();
		for (StorageUnit unit : units) {
			LOGGER.debug("Checking {}. Verifying if check must be processed", unit);
			StorageUnitCheckType typeNeeded = checkConsistency(unit, defaultCheckDelay, defaultCheckChecksumDelay);
			if (StorageUnitCheckType.NONE.equals(typeNeeded)) {
				LOGGER.info("Checking {} skipped. Disabled on StorageUnit", unit);
			} else {
				boolean checksumEnabled = StorageUnitCheckType.LISTING_SIZE_CHECKSUM.equals(typeNeeded);
				LOGGER.info("Checking {}. Processing (checksum={})", unit, checksumEnabled);
				checkConsistency(unit, checksumEnabled);
				LOGGER.info("Checking {}. Done", unit);
			}
		}
	}

	public StorageUnitCheckType checkConsistency(StorageUnit unit, Duration defaultCheckDelay,
			Duration defaultCheckChecksumDelay) {
		return getCheckConsistencyType(unit::toString,
				LocalDateTime.now(),
				unit::getCheckType,
				() -> databaseOperations.getLastCheck(unit).getCheckFinishedOn(),
				() -> databaseOperations.getLastCheckChecksum(unit).getCheckFinishedOn(),
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
	 * Create automatically a new {@link StorageUnit} for overflowing and auto-creation enabled {@link StorageUnit}
	 * 
	 * TODO: add @see
	 */
	public void splitStorageUnits() {
		
	}

	private Path getAbsolutePath(Fichier fichier) {
		return Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()).toAbsolutePath();
	}

	@Nonnull
	private StorageUnit selectStorageUnit(IFichierType fichierType) {
		return storageUnitTypeCandidates.stream()
				.filter(c -> c.accept(fichierType))
				.findFirst()
				.map(databaseOperations::loadAliveStorageUnit)
				.orElseThrow();
	}

	private StorageTransactionPayload prepareAdapter() {
		if (!TransactionSynchronizationManager.isActualTransactionActive()) {
			throw new IllegalStateException("No active transaction");
		}
		Optional<StorageTransactionPayload> payload = getPayloadResource();
		if (payload.isEmpty()) {
			TransactionSynchronizationManager.registerSynchronization(adapter);
			StorageTransactionPayload payloadObject = new StorageTransactionPayload();
			TransactionSynchronizationManager.bindResource(EVENTS_RESOURCE_KEY, payloadObject);
			return payloadObject;
		} else {
			return payload.get();
		}
	}

	private Optional<StorageTransactionPayload> getPayloadResource() {
		return Optional.ofNullable(TransactionSynchronizationManager.getResource(EVENTS_RESOURCE_KEY))
				.map(StorageTransactionPayload.class::cast);
	}

	@Override
	public List<StorageEvent> getEvents() {
		return getPayloadResource()
				.map(StorageTransactionPayload::getEvents)
				.orElseGet(Collections::emptyList);
	}

	@Override
	public void unbind() {
		TransactionSynchronizationManager.unbindResourceIfPossible(EVENTS_RESOURCE_KEY);
	}

}
