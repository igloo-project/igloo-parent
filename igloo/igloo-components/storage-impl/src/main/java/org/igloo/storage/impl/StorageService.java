package org.igloo.storage.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.igloo.storage.api.IMimeTypeResolver;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistency;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.base.Supplier;
import com.google.common.collect.ComparisonChain;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.CountingInputStream;

public class StorageService implements IStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

	// TODO: split data and handler
	private static final Class<?> EVENTS_RESOURCE_KEY = StorageService.class;

	private final EntityManagerFactory entityManagerFactory;
	private final StorageOperations operations;
	private final StorageTransactionHandler handler;
	private final Set<IStorageUnitType> storageUnitTypeCandidates;
	private final IMimeTypeResolver mimeTypeResolver;
	private final Supplier<Path> storageUnitPathSupplier;
	private final SequenceGenerator sequenceGenerator;

	public StorageService(EntityManagerFactory entityManagerFactory, SequenceGenerator sequenceGenerator, Set<IStorageUnitType> storageUnitTypeCandidates, StorageOperations operations, Supplier<Path> storageUnitPathSupplier) {
		this(entityManagerFactory, sequenceGenerator, storageUnitTypeCandidates, operations, storageUnitPathSupplier, new MimeTypeResolver());
	}

	public StorageService(EntityManagerFactory entityManagerFactory, SequenceGenerator sequenceGenerator, Set<IStorageUnitType> storageUnitTypeCandidates, StorageOperations operations, Supplier<Path> storageUnitPathSupplier, IMimeTypeResolver mimeTypeResolver) {
		this.entityManagerFactory = entityManagerFactory;
		this.sequenceGenerator = sequenceGenerator;
		this.storageUnitTypeCandidates = storageUnitTypeCandidates;
		this.mimeTypeResolver = mimeTypeResolver;
		this.operations = operations;
		this.storageUnitPathSupplier = storageUnitPathSupplier;
		handler = new StorageTransactionHandler(operations);
	}

	@Override
	public StorageUnit createStorageUnit(@Nonnull IStorageUnitType type) {
		EntityManager entityManager = entityManager();
		StorageUnit unit = new StorageUnit();
		unit.setCreationDate(LocalDateTime.now());
		unit.setType(type);
		unit.setStatus(StorageUnitStatus.ALIVE);

		entityManager.persist(unit);
		entityManager.flush();

		unit.setPath(storageUnitPathSupplier.get().toAbsolutePath()
			.resolve(String.format("%s-%s", type.getPath(), unit.getId().toString()))
				.toString());

		return unit;
	}

	@Override
	@Nonnull
	public Fichier addFichier(@Nullable String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream) {
		EntityManager entityManager = entityManager();
		StorageUnit unit = selectStorageUnit(entityManager, fichierType);
		Fichier fichier = new Fichier();
		fichier.setId(sequenceGenerator.generate(entityManager));
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.ALIVE);
		fichier.setFichierType(fichierType);
		fichier.setStorageUnit(unit);
		fichier.setName(filename);
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setMimetype(mimeTypeResolver.resolve(fichier.getFilename()));
		fichier.setCreationDate(LocalDateTime.now());

		entityManager.persist(fichier);

		fichier.setRelativePath(unit.getType().getFichierPathStrategy().getPath(fichier));
		Path absolutePath = getAbsolutePath(fichier);
		try (
				HashingInputStream hashingInputStream = new HashingInputStream(Hashing.sha256(), inputStream);
				CountingInputStream countingInputStream = new CountingInputStream(hashingInputStream)) {
			operations.copy(countingInputStream, absolutePath);
			fichier.setChecksum(hashingInputStream.hash().toString());
			fichier.setSize(countingInputStream.getCount());
		} catch (IOException e) {
			// TODO use custom runtime exception
			throw new IllegalStateException(e);
		}
		StorageTransactionAdapter adapter = prepareAdapter();

		adapter.addEvent(fichier.getId(), StorageEventType.ADD, absolutePath);
		return fichier;
	}

	@Override
	public void removeFichier(@Nonnull Fichier fichier) {
		// TODO : vérifier qu'on est dans un état DELETED ?
		entityManager().remove(fichier);
		prepareAdapter().addEvent(fichier.getId(), StorageEventType.DELETE, getAbsolutePath(fichier));
	}

	@Override
	public void invalidateFichier(@Nonnull Fichier fichier) {
		if (Objects.equals(fichier.getStatus(), FichierStatus.ALIVE)) {
			fichier.setStatus(FichierStatus.INVALIDATED);
			fichier.setDeletionDate(LocalDateTime.now());
		} else {
			LOGGER.warn("[invalidate] Fichier {} is alread marked DELETED; no action", fichier.getId());
		}
	}

	@Override
	@Nonnull
	public File getFile(@Nonnull Fichier fichier) {
		// TODO : gérer file manquant
		return operations.getFile(getAbsolutePath(fichier));
	}

	// TODO MPI : à mettre dans un service spécifique aux vérifications de cohérence ?
	@Override
	public List<StorageConsistency> checkConsistency(@Nonnull StorageUnit unit) {
		EntityManager entityManager = entityManager();
		List<StorageConsistency> consistencies = new ArrayList<>();

		List<IFichierType> existingFichierTypes = entityManager.createQuery("SELECT DISTINCT f.fichierType FROM Fichier f", IFichierType.class)
			.getResultStream()
			.sorted((o1, o2) -> ComparisonChain.start().compare(o1.getName(), o2.getName()).result())
			.collect(Collectors.toList());

		for (IFichierType fichierType : existingFichierTypes) {
			StorageConsistency consistency = new StorageConsistency(unit, fichierType);

			Collection<File> files = operations.listRecursively(getAbsolutePath(unit, fichierType));
			if (files != null) {
				consistency.setFsFileCount(files.size());
			}

			Long fichierCount = entityManager.createQuery("SELECT COUNT(f) FROM Fichier f where f.storageUnit = :unit AND f.status = :status AND f.fichierType = :type ORDER BY f.id DESC", Long.class)
				.setParameter("unit", unit)
				.setParameter("type", fichierType)
				.setParameter("status", FichierStatus.ALIVE)
				.getSingleResult();
			consistency.setDbFichierCount(fichierCount.intValue()); // TODO MPI : on conserve le cast en int ou on passe en long ?

			consistencies.add(consistency);
		}

		return consistencies;
	}



	private Path getAbsolutePath(Fichier fichier) {
		return Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()).toAbsolutePath();
	}

	private Path getAbsolutePath(StorageUnit unit, IFichierType fichierType) {
		return Path.of(unit.getPath(), fichierType.getPath()).toAbsolutePath();
	}

	@Nonnull
	private StorageUnit selectStorageUnit(EntityManager entityManager, IFichierType fichierType) {
		return storageUnitTypeCandidates.stream()
				.filter(c -> c.accept(fichierType))
				.findFirst()
				.map(this::loadAliveStorageUnit)
				.orElseThrow();
	}

	private StorageUnit loadAliveStorageUnit(IStorageUnitType storageUnitType) {
		return entityManager()
			.createQuery("SELECT s FROM StorageUnit s WHERE s.type = :type AND s.status = :status ORDER BY s.id DESC", StorageUnit.class)
			.setParameter("type", storageUnitType)
			.setParameter("status", StorageUnitStatus.ALIVE)
			.getResultStream()
			.findFirst()
			.orElseThrow();
	}

	@Nonnull
	private EntityManager entityManager() {
		return Optional.ofNullable(EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)).orElseThrow();
	}

	private StorageTransactionAdapter prepareAdapter() {
		StorageTransactionAdapter adapter;
		Optional<TransactionSynchronization> currentAdapter =
			TransactionSynchronizationManager.getSynchronizations().stream()
				.filter(StorageTransactionAdapter.class::isInstance).findAny();
		if (currentAdapter.isEmpty()) {
			adapter = new StorageTransactionAdapter(handler);
			TransactionSynchronizationManager.registerSynchronization(adapter);
		} else {
			adapter = (StorageTransactionAdapter) currentAdapter.get();
		}
		return adapter;
	}

}
