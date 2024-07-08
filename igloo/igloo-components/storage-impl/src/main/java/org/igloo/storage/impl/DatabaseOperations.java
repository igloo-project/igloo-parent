package org.igloo.storage.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import com.google.common.base.Suppliers;
import com.google.common.io.Resources;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class DatabaseOperations {

	private static final String PARAMETER_FAILURE_STATUS = "failureStatus";
	private static final String PARAMETER_FAILURE_TYPE = "failureType";
	private static final String PARAMETER_SIZE = "size";
	private static final String PARAMETER_COUNT = "count";
	private static final String PARAMETER_FICHIER_STATUS = "fichierStatus";
	private static final String PARAMETER_FICHIER_TYPE = "fichierType";
	private static final String PARAMETER_STORAGE_UNIT_TYPE = "storageUnitType";
	private static final String PARAMETER_STORAGE_UNIT_ID = "storageUnitId";
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseOperations.class);

	private Supplier<String> lastCheckStatisticsQuery = Suppliers.memoize(() -> readSqlResource("last-check-statistics.sql"));
	private Supplier<String> orphanStatisticsQuery = Suppliers.memoize(() -> readSqlResource("orphan-statistics.sql"));
	private Supplier<String> unitStatisticsQuery = Suppliers.memoize(() -> readSqlResource("unit-statistics.sql"));
	private Supplier<String> failureStatisticsQuery = Suppliers.memoize(() -> readSqlResource("failure-statistics.sql"));
	private Supplier<String> storageUnitSplitQuery = Suppliers.memoize(() -> readSqlResource("storage-unit-split.sql"));

	private final EntityManagerFactory entityManagerFactory;

	private final String fichierSequenceName;

	private final String storageUnitSequenceName;

	public DatabaseOperations(EntityManagerFactory entityManagerFactory, String fichierSequenceName, String storageUnitSequenceName) {
		this.entityManagerFactory = entityManagerFactory;
		this.fichierSequenceName = fichierSequenceName;
		this.storageUnitSequenceName = storageUnitSequenceName;
	}

	public Fichier getFichierById(Long id) {
		List<Fichier> fichiers = entityManager().createQuery("SELECT f from Fichier f where f.id = :id", Fichier.class)
			.setParameter("id", id)
			.getResultList();
		
		// Different from getSingleResult as it does not throw an exception if not Fichier is found.
		if (fichiers == null || fichiers.isEmpty()) {
			return null;
		} else if (fichiers.size() > 1) {
			throw new NonUniqueResultException();
		}
		return fichiers.get(0);
	}

	public Fichier getAttachedFichier(Fichier fichier) {
		if (entityManager().contains(fichier)) {
			return fichier;
		} else {
			return entityManager().find(Fichier.class, fichier.getId());
		}
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

	public StorageUnit splitStorageUnit(StorageUnit original, Long id, String newPath) {
		if (!entityManager().contains(original)) {
			original = entityManager().find(StorageUnit.class, original.getId());
		}
		if (!StorageUnitStatus.ALIVE.equals(original.getStatus())) {
			throw new IllegalStateException(String.format("Original status %s unexpected for StorageUnit split", original.getStatus()));
		}
		StorageUnit newUnit = new StorageUnit();
		newUnit.setId(id);
		newUnit.setCheckChecksumDelay(original.getCheckChecksumDelay());
		newUnit.setCheckDelay(original.getCheckDelay());
		newUnit.setCheckType(original.getCheckType());
		newUnit.setCreationDate(LocalDateTime.now());
		newUnit.setPath(newPath);
		newUnit.setSplitDuration(original.getSplitDuration());
		newUnit.setSplitSize(original.getSplitSize());
		newUnit.setStatus(StorageUnitStatus.ALIVE);
		newUnit.setType(original.getType());
		original.setStatus(StorageUnitStatus.ARCHIVED);
		entityManager().persist(newUnit);
		return newUnit;
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

	@Nullable
	public StorageConsistencyCheck getLastCheck(StorageUnit unit) {
		try {
			return entityManager().createQuery("SELECT s FROM StorageConsistencyCheck s WHERE storageUnit = :storageUnit ORDER BY s.checkFinishedOn DESC", StorageConsistencyCheck.class)
					.setParameter("storageUnit", unit)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Nullable
	public StorageConsistencyCheck getLastCheckChecksum(StorageUnit unit) {
		try {
			return entityManager().createQuery("SELECT s FROM StorageConsistencyCheck s WHERE checkType = :checkType AND storageUnit = :storageUnit ORDER BY s.checkFinishedOn DESC", StorageConsistencyCheck.class)
					.setParameter("checkType", StorageUnitCheckType.LISTING_SIZE_CHECKSUM)
					.setParameter("storageUnit", unit)
					.setMaxResults(1)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Nonnull
	public List<Fichier> listInvalidated(@Nullable Integer limit) {
		TypedQuery<Fichier> query = entityManager().createQuery("SELECT f FROM Fichier f WHERE f.status = :invalidatedStatus ORDER BY f.id ASC", Fichier.class);
		query.setParameter("invalidatedStatus", FichierStatus.INVALIDATED);
		if (limit != null) {
			query.setMaxResults(limit);
		}
		return query.getResultList();
	}

	@Nonnull
	public List<Fichier> listTransient(@Nullable Integer limit, @Nonnull LocalDateTime maxCreationDate) {
		Objects.requireNonNull(maxCreationDate, "maxCreationDate must not be null");
		TypedQuery<Fichier> query = entityManager().createQuery("SELECT f FROM Fichier f WHERE f.status = :transientStatus AND f.creationDate < :maxCreationDate ORDER BY f.id ASC", Fichier.class);
		query.setParameter("transientStatus", FichierStatus.TRANSIENT);
		query.setParameter("maxCreationDate", maxCreationDate);
		if (limit != null) {
			query.setMaxResults(limit);
		}
		return query.getResultList();
	}

	/**
	 * List {@link StorageUnit} that need to be split. Listing is based upon {@link StorageUnit#getSplitSize()} or
	 * {@link StorageUnit#getSplitDuration()}. Null values implies NO automatic split.
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public List<StorageUnit> listStorageUnitsToSplit() {
		return entityManager().createNativeQuery(storageUnitSplitQuery.get(), StorageUnit.class).getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<StorageStatistic> getStorageStatistics() {
		return ((NativeQuery<StorageStatistic>) entityManager().createNativeQuery(unitStatisticsQuery.get()))
				.addScalar(PARAMETER_STORAGE_UNIT_ID, StandardBasicTypes.LONG)
				.addAttributeResult(PARAMETER_STORAGE_UNIT_TYPE, StorageUnit.class, "type")
				.addAttributeResult(PARAMETER_FICHIER_TYPE, Fichier.class, "type")
				.addAttributeResult(PARAMETER_FICHIER_STATUS, Fichier.class, "status")
				.addScalar(PARAMETER_COUNT, StandardBasicTypes.INTEGER)
				.addScalar(PARAMETER_SIZE, StandardBasicTypes.LONG)
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageStatistic.class))
				.getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<StorageFailureStatistic> getStorageFailureStatistics() {
		return ((NativeQuery<StorageFailureStatistic>) entityManager().createNativeQuery(failureStatisticsQuery.get()))
				.addScalar(PARAMETER_STORAGE_UNIT_ID, StandardBasicTypes.LONG)
				.addAttributeResult(PARAMETER_STORAGE_UNIT_TYPE, StorageUnit.class, "type")
				.addAttributeResult(PARAMETER_FICHIER_TYPE, Fichier.class, "type")
				.addAttributeResult(PARAMETER_FICHIER_STATUS, Fichier.class, "status")
				.addAttributeResult(PARAMETER_FAILURE_TYPE, StorageFailure.class, "type")
				.addAttributeResult(PARAMETER_FAILURE_STATUS, StorageFailure.class, "status")
				.addScalar(PARAMETER_COUNT, StandardBasicTypes.INTEGER)
				.addScalar(PARAMETER_SIZE, StandardBasicTypes.LONG)
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageFailureStatistic.class))
				.getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<StorageOrphanStatistic> getStorageOrphanStatistics() {
		return ((NativeQuery<StorageOrphanStatistic>) entityManager().createNativeQuery(orphanStatisticsQuery.get()))
				.addScalar(PARAMETER_STORAGE_UNIT_ID, StandardBasicTypes.LONG)
				.addAttributeResult(PARAMETER_STORAGE_UNIT_TYPE, StorageUnit.class, "type")
				.addAttributeResult(PARAMETER_FAILURE_STATUS, StorageFailure.class, "status")
				.addScalar(PARAMETER_COUNT, StandardBasicTypes.INTEGER)
				.setParameter("missingEntityFailureType", StorageFailureType.MISSING_ENTITY.name())
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageOrphanStatistic.class))
				.getResultList();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<StorageCheckStatistic> getStorageCheckStatistics() {
		if (!isPostgresqlBackend()) {
			throw new IllegalStateException("getStorageCheckStatistics need postgresql backend");
		}
		return ((NativeQuery<StorageCheckStatistic>) entityManager().createNativeQuery(lastCheckStatisticsQuery.get()))
				.addScalar(PARAMETER_STORAGE_UNIT_ID, StandardBasicTypes.LONG)
				.addAttributeResult(PARAMETER_STORAGE_UNIT_TYPE, StorageUnit.class, "type")
				.addScalar("lastOn", StandardBasicTypes.LOCAL_DATE_TIME)
				.addScalar("lastChecksumOn", StandardBasicTypes.LOCAL_DATE_TIME)
				.addScalar("lastDuration", StandardBasicTypes.DURATION)
				.addScalar("lastChecksumDuration", StandardBasicTypes.DURATION)
				.addScalar("lastAge", StandardBasicTypes.DURATION)
				.addScalar("lastChecksumAge", StandardBasicTypes.DURATION)
				// deprecated, but JPA SqlResultSetMapping provides no way to map enum/interfaces with custom types
				.setResultTransformer(Transformers.aliasToBean(StorageCheckStatistic.class))
				.getResultList();
	}

	private boolean isPostgresqlBackend() {
		return ((String) entityManager().getEntityManagerFactory().
				unwrap(SessionFactory.class)
				.getProperties()
				.get("jakarta.persistence.jdbc.driver"))
			.toLowerCase().contains("postgresql");
	}

	@Nonnull
	private EntityManager entityManager() {
		return Optional.ofNullable(EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)).orElseThrow();
	}

	/**
	 * Read query from a resource file; remove full line comment (i.e. line begins with --)
	 */
	private static String readSqlResource(String sqlFilename) {
		try {
			List<String> content = Resources.readLines(Resources.getResource("igloo-storage/" + sqlFilename), StandardCharsets.UTF_8);
			return content.stream().filter(l -> !l.strip().startsWith("--")).collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
