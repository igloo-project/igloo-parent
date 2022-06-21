package org.igloo.storage.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
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
import javax.annotation.Nullable;

import org.igloo.storage.api.IMimeTypeResolver;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.api.IStorageStatisticsService;
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
import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.LongEntityReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.CountingInputStream;

public class StorageService implements IStorageService, IStorageStatisticsService, IStorageTransactionResourceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

	private static final Class<?> EVENTS_RESOURCE_KEY = StorageService.class;

	private final StorageOperations storageOperations;
	private final StorageTransactionHandler handler;
	private final Set<IStorageUnitType> storageUnitTypeCandidates;
	private final IMimeTypeResolver mimeTypeResolver;
	private final Supplier<Path> storageUnitPathSupplier;
	private final DatabaseOperations databaseOperations;
	private final StorageTransactionAdapter adapter;

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
	public Fichier addFichier(@Nonnull String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream, @Nullable GenericEntity<Long, ?> author) {
		StorageUnit unit = selectStorageUnit(fichierType);
		Fichier fichier = new Fichier();
		fichier.setId(databaseOperations.generateFichier());
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.TRANSIENT);
		fichier.setType(fichierType);
		fichier.setStorageUnit(unit);
		fichier.setFilename(filename);
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setMimetype(mimeTypeResolver.resolve(fichier.getFilename()));
		fichier.setCreationDate(LocalDateTime.now());
		fichier.setCreatedBy(LongEntityReference.ofLongEntity(author));

		fichier.setRelativePath(unit.getType().getFichierPathStrategy().getPath(fichier));
		Path absolutePath = getAbsolutePath(fichier);
		try (
				HashingInputStream hashingInputStream = new HashingInputStream(Hashing.sha256(), inputStream);
				CountingInputStream countingInputStream = new CountingInputStream(hashingInputStream)) {
			storageOperations.copy(countingInputStream, absolutePath);
			fichier.setChecksum(hashingInputStream.hash().toString());
			fichier.setSize(countingInputStream.getCount());
		} catch (IOException e) {
			throw new IllegalStateException(String.format("Fichier content cannot be saved to %s", absolutePath), e);
		}
		databaseOperations.createFichier(fichier);

		addEvent(fichier.getId(), StorageEventType.ADD, absolutePath);
		return fichier;
	}

	@Override
	@Nonnull
	public Fichier addFichier(@Nonnull String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream) {
		return addFichier(filename, fichierType, inputStream, null);
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
		Fichier attachedFichier = databaseOperations.getAttachedFichier(fichier);
		if (attachedFichier == null) {
			LOGGER.warn("Fichier {} cannot be retrieved in database to be deleted.", fichier);
			return;
		}
		if (FichierStatus.ALIVE.equals(fichier.getStatus())) {
			throw new IllegalStateException(String.format("Fichier %d, status=%s cannot be removed due to its current status.", fichier.getId(), fichier.getStatus().name()));
		}
		databaseOperations.removeFichier(attachedFichier);
		addEvent(attachedFichier.getId(), StorageEventType.DELETE, getAbsolutePath(attachedFichier));
	}

	@Override
	public Fichier getFichierById(@Nonnull Long id) {
		return databaseOperations.getFichierById(id);
	}

	@Override
	@Nonnull
	public File getFile(@Nonnull Fichier fichier) throws FileNotFoundException {
		return getFile(fichier, true, true);
	}

	@Override
	@Nonnull
	public File getFile(@Nonnull Fichier fichier, boolean checkInvalidated, boolean checkExists) throws FileNotFoundException {
		if (checkInvalidated && FichierStatus.INVALIDATED.equals(fichier.getStatus())) {
			throw new FileNotFoundException(String.format("Fichier %d is INVALIDATED; access not available", fichier.getId()));
		}
		return storageOperations.getFile(getAbsolutePath(fichier), checkExists);
	}

	@Override
	@Nonnull
	public StorageUnit createStorageUnit(@Nonnull IStorageUnitType type, @Nonnull StorageUnitCheckType checkType) {
		StorageUnit unit = new StorageUnit();
		unit.setCreationDate(LocalDateTime.now());
		unit.setId(databaseOperations.generateStorageUnit());
		unit.setType(type);
		unit.setStatus(StorageUnitStatus.ALIVE);
		unit.setCheckType(checkType);
		unit.setPath(storageUnitPathSupplier.get().toAbsolutePath()
			.resolve(String.format("%s-%s", type.getPath(), unit.getId().toString()))
				.toString());
		databaseOperations.createStorageUnit(unit);
		return unit;
	}

	@Override
	@Nonnull
	public StorageUnit splitStorageUnit(@Nonnull StorageUnit original) {
		Long id = databaseOperations.generateStorageUnit();
		String path = storageUnitPathSupplier.get().toAbsolutePath()
			.resolve(String.format("%s-%s", original.getType().getPath(), id.toString()))
				.toString();
		return databaseOperations.splitStorageUnit(original, id, path);
	}

	@Override
	@Nonnull
	@java.lang.SuppressWarnings("java:S3776")
	public List<StorageConsistencyCheck> checkConsistency(@Nonnull StorageUnit unit, boolean checksumValidation) {
		// reload entity
		final StorageUnit persistedUnit = databaseOperations.getStorageUnit(unit.getId());
		List<StorageConsistencyCheck> consistencies = new ArrayList<>();
		StorageConsistencyCheck consistencyCheck = new StorageConsistencyCheck(persistedUnit);
		consistencyCheck.setCheckType(checksumValidation ? StorageUnitCheckType.LISTING_SIZE_CHECKSUM : StorageUnitCheckType.LISTING_SIZE);
		databaseOperations.createConsistencyCheck(consistencyCheck);
		
		// we need to collect all this data during check
		Integer fileCount = 0;
		Integer fichierCount = 0;
		Long fileSize = 0l;
		Long fichierSize;
		Integer missingFichierCount = 0;
		Integer missingFileCount = 0;
		Integer sizeMismatchCount = 0;
		Integer checksumMismatchCount = 0;
		
		// list db and filesystem
		// list database then filesystem
		// -> when entity is available in database, file has to be created -> listed entities must be on FS
		// -> we need to stop unit cleaning during check so we do not have deleted file for an existing entity
		Map<Path, Fichier> result = databaseOperations.listUnitAliveFichiers(persistedUnit).stream()
				.collect(Collectors.toMap(f -> Path.of(persistedUnit.getPath()).resolve(f.getRelativePath()), Function.identity()));
		Set<Path> files = storageOperations.listUnitContent(persistedUnit);
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
					databaseOperations.triggerFailure(StorageFailure.ofSizeMismatch(filePath, fichier, consistencyCheck));
					sizeMismatchCount += 1;
				} else if (checksumValidation && !ChecksumType.NONE.equals(fichier.getChecksumType())) {
					// conditionally perform checksum comparison
					String checksum = storageOperations.checksum(filePath);
					if (!checksum.equals(fichier.getChecksum())) {
						databaseOperations.triggerFailure(StorageFailure.ofChecksumMismatch(filePath, fichier, consistencyCheck));
						checksumMismatchCount += 1;
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
		consistencyCheck.setSizeMismatchCount(sizeMismatchCount);
		consistencyCheck.setChecksumMismatchCount(checksumMismatchCount);
		consistencyCheck.setCheckFinishedOn(LocalDateTime.now());
		if (consistencyCheck.getSizeMismatchCount() > 0 || consistencyCheck.getChecksumMismatchCount() > 0
				|| consistencyCheck.getMissingFichierCount() > 0 || consistencyCheck.getMissingFileCount() > 0) {
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

	@Override
	public List<StorageStatistic> getStorageStatistics() {
		return databaseOperations.getStorageStatistics();
	}

	@Override
	public List<StorageFailureStatistic> getStorageFailureStatistics() {
		return databaseOperations.getStorageFailureStatistics();
	}

	@Override
	public List<StorageOrphanStatistic> getStorageOrphanStatistics() {
		return databaseOperations.getStorageOrphanStatistics();
	}

	@Override
	public List<StorageCheckStatistic> getStorageCheckStatistics() {
		return databaseOperations.getStorageCheckStatistics();
	}

}
