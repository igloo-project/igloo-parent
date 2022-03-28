package org.igloo.storage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
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

	public void createStorageUnit(StorageUnit unit) {
		entityManager().persist(unit);
	}

	@Nonnull
	public Set<Fichier> listUnitAliveFichiers(StorageUnit unit) {
		return entityManager().createQuery("SELECT f FROM Fichier f where f.storageUnit = :unit AND f.status != :invalidatedStatus ORDER BY f.id DESC", Fichier.class)
				.setParameter("unit", unit)
				.setParameter("invalidatedStatus", FichierStatus.INVALIDATED)
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

	/**
	 * Create or update an existing failure.
	 */
	public void triggerFailure(StorageFailure failure) {
		try {
			StorageFailure storedFailure = entityManager().createQuery("SELECT f FROM StorageFailure f WHERE f.path = :path ORDER BY f.id DESC", StorageFailure.class)
				.setParameter("path", failure.getPath())
				.getSingleResult();
			// failure is known and acknowledged; we ignore subsequent checks
			// except type failure change
			if (failure.getType().equals(storedFailure.getType()) && StorageFailureStatus.ACKNOWLEDGED.equals(failure.getStatus())) {
				return;
			}
			// a failure must not be reported on multiple unit; only misconfiguration may trigger this case
			// (an absolute path cannot be linked to multiple units)
			if (!failure.getConsistencyCheck().getStorageUnit().equals(storedFailure.getConsistencyCheck().getStorageUnit())) {
				throw new IllegalStateException(String.format("Failure reported for %s in unit %d and already stored for unit %d", failure.getPath(), failure.getConsistencyCheck().getStorageUnit().getId(), storedFailure.getConsistencyCheck().getStorageUnit().getId()));
			}
			// else storedFailure is updated, except creation time
			failure.setCreationTime(storedFailure.getCreationTime());
			failure.setId(storedFailure.getId());
			entityManager().merge(failure);
		} catch (NoResultException e) {
			entityManager().persist(failure);
		}
	}

	/**
	 * <p>Clean current failures linked with {@link StorageUnit} targetted by <code>consistencyCheck</code> and not
	 * linked with this <code>consistencyCheck</code>. It allows to resolve as {@link StorageFailureStatus#FIXED} all
	 * failures not updated by the provided {@link StorageConsistencyCheck}.</p>
	 * 
	 * @param if <code>true</code>, cleaned {@link StorageFailureStatus} includes checksum mismatch failure. If <code>false</code>,
	 * there are excluded.
	 */
	public void cleanFailures(StorageConsistencyCheck consistencyCheck, boolean alsoCleanChecksumMismatch) {
		Map<String, Object> params = new HashMap<>();
		String query = "UPDATE StorageFailure SET status = :fixedStatus "
				// link failure -> check -> unit
				+ "FROM StorageUnit u JOIN StorageConsistencyCheck c ON c.unit_id = u.id "
				// match same unit, other check and status ALIVE
				+ "WHERE u.id = :unitId AND consistencyCheck_id != :consistencyCheckId AND status = :aliveStatus";
		params.put("aliveStatus", StorageFailureStatus.ALIVE);
		params.put("unitId", consistencyCheck.getStorageUnit().getId());
		params.put("consistencyCheckId", consistencyCheck.getId());
		if (!alsoCleanChecksumMismatch) {
			query += " AND type != :checksumMismatchType";
			params.put("checksumMismatchType", StorageFailureType.CONTENT_MISMATCH);
		}
		entityManager().createNativeQuery(query, Integer.class).executeUpdate();
	}

	public void createFichier(Fichier fichier) {
		entityManager().persist(fichier);
	}

	public void removeFichier(Fichier fichier) {
		entityManager().remove(fichier);
	}

	public StorageUnit getStorageUnit(Long storageUnitId) {
		return entityManager().find(StorageUnit.class, storageUnitId);
	}

	public void createConsistencyCheck(StorageConsistencyCheck consistencyCheck) {
		entityManager().persist(consistencyCheck);
	}

	@Nonnull
	private EntityManager entityManager() {
		return Optional.ofNullable(EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)).orElseThrow();
	}

}
