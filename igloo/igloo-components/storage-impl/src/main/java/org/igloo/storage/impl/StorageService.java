package org.igloo.storage.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.google.common.io.CountingInputStream;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
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

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;

public class StorageService implements IStorageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

	// TODO: split data and handler
	private static final Class<?> TASKS_RESOURCE_KEY = StorageService.class;

	private final EntityManagerFactory entityManagerFactory;
	private final StorageOperations operations = new StorageOperations();
	private final StorageTransactionHandler handler = new StorageTransactionHandler(operations);
	private final Set<IStorageUnitType> storageUnitTypeCandidates;

	public StorageService(EntityManagerFactory entityManagerFactory, Set<IStorageUnitType> storageUnitTypeCandidates) {
		this.entityManagerFactory = entityManagerFactory;
		this.storageUnitTypeCandidates = storageUnitTypeCandidates;
	}

	@Override
	@Nonnull
	public Fichier addFichier(IFichierType fichierType, InputStream inputStream) {
		EntityManager entityManager = entityManager();
		StorageUnit unit = selectStorageUnit(entityManager, fichierType);
		Fichier fichier = new Fichier();
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.ALIVE);
		fichier.setFichierType(fichierType);
		fichier.setStorageUnit(unit);
		fichier.setRelativePath("relative-path");
		fichier.setName("filename");
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setCreationDate(new Date());
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
		entityManager.persist(fichier);
		entityManager.flush();
		adapter.addTask(fichier.getId(), StorageTaskType.ADD, absolutePath);
		return fichier;
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

	@Override
	public void removeFichier(@Nonnull Fichier fichier) {
		entityManager().remove(fichier);
		prepareAdapter().addTask(fichier.getId(), StorageTaskType.DELETE, getAbsolutePath(fichier));
	}

	@Override
	@Nonnull
	public File getFile(@Nonnull Fichier fichier) {
		// TODO : gérer file manquant
		return getAbsolutePath(fichier).toFile();
	}

	private Path getAbsolutePath(Fichier fichier) {
		return Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()).toAbsolutePath();
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

}
