package org.igloo.storage.impl;

import java.io.File;
import java.io.FileOutputStream;
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

import org.apache.commons.io.IOUtils;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class StorageService implements IStorageService {

	// TODO: split data and handler
	private static final Class<?> TASKS_RESOURCE_KEY = StorageService.class;

	private final EntityManagerFactory entityManagerFactory;
	private final StorageTransactionHandler handler = new StorageTransactionHandler(new StorageOperations());
	private final Set<IStorageUnitType> storageUnitTypeCandidates;

	public StorageService(EntityManagerFactory entityManagerFactory, Set<IStorageUnitType> storageUnitTypeCandidates) {
		this.entityManagerFactory = entityManagerFactory;
		this.storageUnitTypeCandidates = storageUnitTypeCandidates;
	}

	@Override
	public Fichier addFichier(EntityManager entityManager, IFichierType fichierType, InputStream inputStream) {
		StorageUnit unit = selectStorageUnit(entityManager, fichierType);
		Fichier fichier = new Fichier();
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.ALIVE);
		fichier.setFichierType(fichierType);
		fichier.setStorageUnit(unit);
		fichier.setRelativePath("relative-path");
		fichier.setName("filename");
		fichier.setSize(25);
		fichier.setChecksum("123");
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setCreationDate(new Date());
		Path absolutePath = Path.of(unit.getPath(), fichier.getRelativePath());
		try (FileOutputStream fos = new FileOutputStream(absolutePath.toString())) {
			IOUtils.copy(inputStream, fos);
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
	public Fichier removeFichier(Fichier fichier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFile(Fichier fichier) {
		// TODO Auto-generated method stub
		return null;
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
			.createQuery("FROM StorageUnit s WHERE s.type = :type AND s.status = :status ORDER BY s.id DESC", StorageUnit.class)
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
