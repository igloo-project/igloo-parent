package org.igloo.storage.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageConsistencyCheckResult;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.igloo.storage.model.hibernate.StorageHibernateConstants;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

public class DatabaseOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseOperations.class);

	private final EntityManagerFactory entityManagerFactory;

	private final String fichierSequenceName;

	private final String storageUnitSequenceName;

	private final Type fichierTypeType;
	private final Type storageUnitTypeType;
	private final Type fichierStatusType;
	private final Type failureTypeType;
	private final Type failureStatusType;

	public DatabaseOperations(EntityManagerFactory entityManagerFactory, String fichierSequenceName, String storageUnitSequenceName) {
		this.entityManagerFactory = entityManagerFactory;
		this.fichierSequenceName = fichierSequenceName;
		this.storageUnitSequenceName = storageUnitSequenceName;
		fichierTypeType = entityManagerFactory.unwrap(SessionFactory.class).getTypeHelper().basic(StorageHibernateConstants.TYPE_FICHIER_TYPE);
		storageUnitTypeType = entityManagerFactory.unwrap(SessionFactory.class).getTypeHelper().basic(StorageHibernateConstants.TYPE_STORAGE_UNIT_TYPE);
		Properties fichierTypeTypeProperties = new Properties();
		fichierTypeTypeProperties.setProperty(EnumType.ENUM, FichierStatus.class.getName());
		fichierTypeTypeProperties.setProperty(EnumType.NAMED, Boolean.TRUE.toString());
		fichierStatusType = entityManagerFactory.unwrap(SessionFactory.class).getTypeHelper().custom(EnumType.class, fichierTypeTypeProperties);
		Properties failureTypeTypeProperties = new Properties();
		failureTypeTypeProperties.setProperty(EnumType.ENUM, StorageFailureType.class.getName());
		failureTypeTypeProperties.setProperty(EnumType.NAMED, Boolean.TRUE.toString());
		failureTypeType = entityManagerFactory.unwrap(SessionFactory.class).getTypeHelper().custom(EnumType.class, failureTypeTypeProperties);
		Properties failureStatusTypeProperties = new Properties();
		failureStatusTypeProperties.setProperty(EnumType.ENUM, StorageFailureStatus.class.getName());
		failureStatusTypeProperties.setProperty(EnumType.NAMED, Boolean.TRUE.toString());
		failureStatusType = entityManagerFactory.unwrap(SessionFactory.class).getTypeHelper().custom(EnumType.class, failureStatusTypeProperties);
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
	public Set<Fichier> listUnitAliveFichiers(@Nonnull StorageUnit unit) {
		Objects.requireNonNull(unit, "unit cannot be null");
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
			@Nonnull
			StorageFailure storedFailure = entityManager().createQuery("SELECT f FROM StorageFailure f WHERE f.path = :path ORDER BY f.id DESC", StorageFailure.class)
				.setParameter("path", failure.getPath())
				.getSingleResult();
			// failure is known and acknowledged; we ignore subsequent checks
			// except type failure change
			if (failure.getType().equals(storedFailure.getType()) && StorageFailureStatus.ACKNOWLEDGED.equals(storedFailure.getStatus())) {
				LOGGER.debug("Failure {} already acknowledged. Ignored.", failure);
				return;
			}
			// a failure must not be reported on multiple unit; only misconfiguration may trigger this case
			// (an absolute path cannot be linked to multiple units)
			if (!failure.getConsistencyCheck().getStorageUnit().equals(storedFailure.getConsistencyCheck().getStorageUnit())) {
				throw new IllegalStateException(String.format("Failure reported for %s in unit %d and already stored for unit %d", failure.getPath(), failure.getConsistencyCheck().getStorageUnit().getId(), storedFailure.getConsistencyCheck().getStorageUnit().getId()));
			}
			// else storedFailure is updated
			if (StorageFailureStatus.ALIVE.equals(storedFailure.getStatus())) {
				LOGGER.debug("Failure {} updates an already existing failure.", failure);
				// if failure is already detected, keep creation time
				failure.setCreationTime(storedFailure.getCreationTime());
			} else {
				LOGGER.debug("Failure {} replaces an already fixed or acknowlegded failure", failure);
			}
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
	 * @param alsoCleanChecksumMismatch <code>true</code>, cleaned {@link StorageFailureStatus} includes checksum
	 * mismatch failure. If <code>false</code>, there are excluded.
	 */
	public Integer cleanFailures(StorageConsistencyCheck consistencyCheck, boolean alsoCleanChecksumMismatch) {
		Map<String, Object> params = new HashMap<>();
		String query = "UPDATE StorageFailure SET status = :fixedStatus "
				// match same unit
				+ "WHERE id IN (SELECT f.id FROM StorageFailure f JOIN StorageConsistencyCheck c ON f.consistencyCheck_id = c.id WHERE c.storageUnit_id = :unitId) "
				// other consistencyCheck and status ALIVE
				+ "AND consistencyCheck_id != :consistencyCheckId AND status = :aliveStatus";
		params.put("fixedStatus", StorageFailureStatus.FIXED.name());
		params.put("aliveStatus", StorageFailureStatus.ALIVE.name());
		params.put("unitId", consistencyCheck.getStorageUnit().getId());
		params.put("consistencyCheckId", consistencyCheck.getId());
		if (!alsoCleanChecksumMismatch) {
			query += " AND type != :checksumMismatchType";
			params.put("checksumMismatchType", StorageFailureType.CHECKSUM_MISMATCH.name());
		}
		Query nativeQuery = entityManager().createNativeQuery(query);
		params.forEach(nativeQuery::setParameter);
		return nativeQuery.executeUpdate();
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

	public List<StorageUnit> listStorageUnits() {
		return entityManager().createQuery("SELECT s FROM StorageUnit s ORDER BY s.id ASC", StorageUnit.class).getResultList();
	}

	public StorageConsistencyCheck getLastCheck(StorageUnit unit) {
		try {
			return entityManager().createQuery("SELECT s FROM StorageConsistencyCheck s WHERE status != :checkStatus AND storageUnit = :storageUnit ORDER BY s.checkFinishedOn DESC", StorageConsistencyCheck.class)
					.setParameter("checkStatus", StorageConsistencyCheckResult.UNKNOWN)
					.setParameter("storageUnit", unit)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public StorageConsistencyCheck getLastCheckChecksum(StorageUnit unit) {
		try {
			return entityManager().createQuery("SELECT s FROM StorageConsistencyCheck s WHERE status != :checkStatus AND checkType = :checkType AND storageUnit = :storageUnit ORDER BY s.checkFinishedOn DESC", StorageConsistencyCheck.class)
					.setParameter("checkStatus", StorageConsistencyCheckResult.UNKNOWN)
					.setParameter("checkType", StorageUnitCheckType.LISTING_SIZE_CHECKSUM)
					.setParameter("storageUnit", unit)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<StorageStatistic> getStorageStatistics() {
		return ((NativeQuery<StorageStatistic>) entityManager().createNativeQuery(
				"SELECT s.id AS \"storageUnitId\", s.type AS \"storageUnitType\", f.type AS \"fichierType\", f.status AS \"fichierStatus\", " // dimensions
				+ "count(distinct f.id) AS \"count\", sum(f.size) AS \"size\" " // metrics
				+ "FROM StorageUnit s "
				+ "JOIN Fichier f ON f.storageUnit_id = s.id "
				+ "GROUP BY s.id, s.type, f.type, f.status "
				+ "ORDER BY s.id ASC, s.type ASC, f.type ASC, f.status ASC, \"count\" ASC, size ASC "))
				.addScalar("storageUnitId", LongType.INSTANCE)
				.addScalar("storageUnitType", storageUnitTypeType)
				.addScalar("fichierType", fichierTypeType)
				.addScalar("fichierStatus", fichierStatusType)
				.addScalar("count", IntegerType.INSTANCE)
				.addScalar("size", LongType.INSTANCE)
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageStatistic.class))
				.getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<StorageFailureStatistic> getStorageFailureStatistics() {
		return ((NativeQuery<StorageFailureStatistic>) entityManager().createNativeQuery(
				"SELECT s.id AS \"storageUnitId\", s.type AS \"storageUnitType\", f.type AS \"fichierType\", f.status AS \"fichierStatus\", fa.type AS \"failureType\", fa.status AS \"failureStatus\", " // dimensions
				+ "count(distinct f.id) AS \"count\", sum(f.size) AS \"size\" " // metrics
				+ "FROM StorageUnit s "
				+ "JOIN Fichier f ON f.storageUnit_id = s.id "
				+ "JOIN StorageFailure fa ON fa.fichier_id = f.id "
				+ "WHERE fa.fichier_id IS NOT NULL "
				+ "GROUP BY s.id, s.type, f.type, f.status, fa.type, fa.status "
				+ "ORDER BY s.id ASC, s.type ASC, f.type ASC, f.status ASC, fa.type ASC, fa.status ASC, \"count\" ASC, size ASC "))
				.addScalar("storageUnitId", LongType.INSTANCE)
				.addScalar("storageUnitType", storageUnitTypeType)
				.addScalar("fichierType", fichierTypeType)
				.addScalar("fichierStatus", fichierStatusType)
				.addScalar("failureType", failureTypeType)
				.addScalar("failureStatus", failureStatusType)
				.addScalar("count", IntegerType.INSTANCE)
				.addScalar("size", LongType.INSTANCE)
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageFailureStatistic.class))
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<StorageOrphanStatistic> getStorageOrphanStatistics() {
		return ((NativeQuery<StorageOrphanStatistic>) entityManager().createNativeQuery(
				"SELECT s.id AS \"storageUnitId\", s.type AS \"storageUnitType\", fa.status AS \"failureStatus\", " // dimensions
				+ "count(distinct fa.id) AS \"count\" " // metrics
				+ "FROM StorageUnit s "
				+ "JOIN StorageConsistencyCheck c ON c.storageUnit_id = s.id "
				+ "JOIN StorageFailure fa ON c.id = fa.consistencyCheck_id "
				+ "WHERE fa.type = :missingEntityFailureType "
				+ "GROUP BY s.id, s.type, fa.type, fa.status "
				+ "ORDER BY s.id ASC, s.type ASC, fa.type ASC, fa.status ASC, \"count\" ASC "))
				.addScalar("storageUnitId", LongType.INSTANCE)
				.addScalar("storageUnitType", storageUnitTypeType)
				.addScalar("failureStatus", failureStatusType)
				.addScalar("count", IntegerType.INSTANCE)
				.setParameter("missingEntityFailureType", StorageFailureType.MISSING_ENTITY, failureTypeType)
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageOrphanStatistic.class))
				.getResultList();
	}

	@Nonnull
	private EntityManager entityManager() {
		return Optional.ofNullable(EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)).orElseThrow();
	}

}
