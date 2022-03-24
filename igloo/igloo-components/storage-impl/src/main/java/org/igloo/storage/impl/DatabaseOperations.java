package org.igloo.storage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

public class DatabaseOperations {

	private final EntityManagerFactory entityManagerFactory;

	private final String fichierSequenceName;

	private final String storageUnitSequenceName;

	public DatabaseOperations(EntityManagerFactory entityManagerFactory, String fichierSequenceName, String storageUnitSequenceName) {
		this.entityManagerFactory = entityManagerFactory;
		this.fichierSequenceName = fichierSequenceName;
		this.storageUnitSequenceName = storageUnitSequenceName;
	}

	public long generateStorageUnit() {
		return entityManager().unwrap(Session.class).doReturningWork(c -> {
			PreparedStatement statement = c.prepareStatement("SELECT nextval(?)");
			statement.setString(1, storageUnitSequenceName);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getLong(1);
		});
	}

	public long generateFichier() {
		return entityManager().unwrap(Session.class).doReturningWork(c -> {
			PreparedStatement statement = c.prepareStatement("SELECT nextval(?)");
			statement.setString(1, fichierSequenceName);
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getLong(1);
		});
	}

	@Nonnull
	private EntityManager entityManager() {
		return Optional.ofNullable(EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)).orElseThrow();
	}

	public void createStorageUnit(StorageUnit unit) {
		entityManager().persist(unit);
	}

	@Nonnull
	public Set<Fichier> listUnitAliveFichiers() {
		return entityManager().createQuery("SELECT f FROM Fichier f where f.storageUnit = :unit AND f.status = :status ORDER BY f.id DESC", Fichier.class)
				.setParameter("unit", this)
				.setParameter("status", FichierStatus.ALIVE)
				.getResultStream()
				.collect(Collectors.toUnmodifiableSet());
	}

	public StorageUnit loadAliveStorageUnit(IStorageUnitType storageUnitType) {
		return entityManager()
			.createQuery("SELECT s FROM StorageUnit s WHERE s.type = :type AND s.status = :status ORDER BY s.id DESC", StorageUnit.class)
			.setParameter("type", storageUnitType)
			.setParameter("status", StorageUnitStatus.ALIVE)
			.getResultStream()
			.findFirst()
			.orElseThrow();
	}

	public void createFailure(StorageFailure failure) {
		entityManager().persist(failure);
	}

	public void createFichier(Fichier fichier) {
		entityManager().persist(fichier);
	}

	public void removeFichier(Fichier fichier) {
		entityManager().remove(fichier);
	}

}
