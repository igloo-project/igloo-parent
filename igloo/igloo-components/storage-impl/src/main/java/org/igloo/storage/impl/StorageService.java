package org.igloo.storage.impl;

import java.io.File;
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
import javax.persistence.EntityManagerFactory;

import org.igloo.storage.api.IMimeTypeResolver;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistency;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
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
	final Supplier<Path> storageUnitPathSupplier;
	final DatabaseOperations databaseOperations;
	private final StorageTransactionAdapter adapter;

	public StorageService(EntityManagerFactory entityManagerFactory, Integer transactionSynchronizationOrder, DatabaseOperations databaseOperations, Set<IStorageUnitType> storageUnitTypeCandidates, StorageOperations operations, Supplier<Path> storageUnitPathSupplier) {
		this(entityManagerFactory, transactionSynchronizationOrder, databaseOperations, storageUnitTypeCandidates, operations, storageUnitPathSupplier, new MimeTypeResolver());
	}

	public StorageService(EntityManagerFactory entityManagerFactory, Integer transactionSynchronizationOrder, DatabaseOperations databaseOperations, Set<IStorageUnitType> storageUnitTypeCandidates, StorageOperations operations, Supplier<Path> storageUnitPathSupplier, IMimeTypeResolver mimeTypeResolver) {
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
		fichier.setFichierType(fichierType);
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
			LOGGER.warn("[validate] Fichier {} is alread marked ALIVE; no action", fichier.getId());
		}
	}

	@Override
	public void invalidateFichier(@Nonnull Fichier fichier) {
		if (Objects.equals(fichier.getStatus(), FichierStatus.ALIVE)) {
			fichier.setStatus(FichierStatus.INVALIDATED);
			fichier.setInvalidationDate(LocalDateTime.now());
		} else {
			LOGGER.warn("[invalidate] Fichier {} is alread marked DELETED; no action", fichier.getId());
		}
	}

	@Override
	public void removeFichier(@Nonnull Fichier fichier) {
		// TODO : vérifier qu'on est dans un état DELETED ?
		databaseOperations.removeFichier(fichier);
		addEvent(fichier.getId(), StorageEventType.DELETE, getAbsolutePath(fichier));
	}

	@Override
	@Nonnull
	public File getFile(@Nonnull Fichier fichier) {
		// TODO : gérer file manquant
		return storageOperations.getFile(getAbsolutePath(fichier));
	}

	@Override
	public StorageUnit createStorageUnit(IStorageUnitType type) {
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
	public List<StorageConsistency> checkConsistency(@Nonnull StorageUnit unit, boolean checksumValidation) {
		List<StorageConsistency> consistencies = new ArrayList<>();
		
		StorageConsistency consistency = new StorageConsistency(unit);
		
		Set<Path> files = storageOperations.listUnitContent(unit);
		consistency.setFsFileCount(files.size());
		
		Map<Path, Fichier> result = databaseOperations.listUnitAliveFichiers().stream().collect(Collectors.toMap(f -> Path.of(f.getRelativePath()), Function.identity()));
		consistency.setDbFichierCount(result.size()); // TODO MPI : on conserve le cast en int ou on passe en long ?

		Set<Path> missingEntities = Sets.difference(files, result.keySet());
		List<Fichier> missingFiles = Sets.difference(result.keySet(), files).stream().map(result::get).collect(Collectors.toUnmodifiableList());
		
		for (Path missingEntity : missingEntities) {
			StorageFailure failure = StorageFailure.ofMissingEntity(Path.of(unit.getPath()).relativize(missingEntity).toString(), unit);
			databaseOperations.createFailure(failure);
		}
		for (Fichier missingFile : missingFiles) {
			StorageFailure failure = StorageFailure.ofMissingFile(missingFile, unit);
			databaseOperations.createFailure(failure);
		}
		Map<Path, Fichier> okEntitiesFiles = Sets.intersection(files, result.keySet()).stream().collect(Collectors.toMap(Function.identity(), result::get));
		if (checksumValidation) {
			for (Fichier fichier : okEntitiesFiles.values()) {
				Path root = Path.of(unit.getPath()).resolve(fichier.getRelativePath());
				try {
					String checksum = storageOperations.checksum(root);
					if (checksum.equals(fichier.getChecksum())) {
						databaseOperations.createFailure(StorageFailure.ofChecksumMismatch(fichier, unit));
					}
				} catch (RuntimeException e) {
					LOGGER.error("Checksum calculation error on {}/{}", fichier.getId(), fichier.getUuid(), e);
				}
			}
		}

		consistencies.add(consistency);

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

}
