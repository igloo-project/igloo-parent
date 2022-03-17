package org.igloo.storage.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.IFichierType;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class StorageService implements IStorageService {

	// TODO: split data and handler
	private static final Class<?> TASKS_RESOURCE_KEY = StorageService.class;

	private final StorageTransactionHandler handler = new StorageTransactionHandler(new StorageOperations());

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

	private StorageUnit selectStorageUnit(EntityManager entityManager, IFichierType fichierType) {
		// TODO dynamic switch
		return entityManager.createQuery("FROM StorageUnit", StorageUnit.class).getSingleResult();
	}

}
