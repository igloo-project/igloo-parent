package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.api.Assertions.byLessThan;
import static test.StorageAssertions.assertThat;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.hibernate.Session;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.jpa.test.JdbcDriver;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.StorageConsistencyCheckResult;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.igloo.storage.model.statistics.StorageCheckStatistic;
import org.igloo.storage.model.statistics.StorageFailureStatistic;
import org.igloo.storage.model.statistics.StorageOrphanStatistic;
import org.igloo.storage.model.statistics.StorageStatistic;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.LongEntityReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.postgresql.Driver;

import com.google.common.base.Strings;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Tuple;
import test.model.FichierType1;
import test.model.FichierType2;
import test.model.StorageUnitType;

class TestDatabaseOperations extends AbstractTest {

	private static final String SELECT_STORAGE_FAILURE_ID_ASC = "SELECT f FROM StorageFailure f ORDER BY f.id ASC";

	@RegisterExtension
	EntityManagerFactoryExtension extension = AbstractTest.initEntityManagerExtension();

	private DatabaseOperations databaseOperations;

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory) {
		databaseOperations = new DatabaseOperations(entityManagerFactory, "fichier_id_seq", "storageunit_id_seq");
	}

	/**
	 * Check that hibernate maps correctly {@link IFichierType}
	 */
	@Test
	void testFichierTypePersist(EntityManager entityManager, EntityTransaction transaction) {
		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setId(1l);
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		Fichier fichier1 = new Fichier();
		fichier1.setId(1l);
		fichier1.setUuid(UUID.randomUUID());
		fichier1.setStatus(FichierStatus.ALIVE);
		fichier1.setType(FichierType1.CONTENT1);
		fichier1.setStorageUnit(storageUnit);
		fichier1.setRelativePath("/relative-path");
		fichier1.setFilename("filename");
		fichier1.setSize(25l);
		fichier1.setChecksum("123");
		fichier1.setMimetype("application/octet-stream");
		fichier1.setChecksumType(ChecksumType.SHA_256);
		fichier1.setCreationDate(LocalDateTime.now());

		Fichier fichier2 = new Fichier();
		fichier2.setId(2l);
		fichier2.setUuid(UUID.randomUUID());
		fichier2.setStatus(FichierStatus.ALIVE);
		fichier2.setType(FichierType2.CONTENT3);
		fichier2.setStorageUnit(storageUnit);
		fichier2.setRelativePath("/relative-path");
		fichier2.setFilename("filename");
		fichier2.setSize(25l);
		fichier2.setChecksum("123");
		fichier2.setMimetype("application/octet-stream");
		fichier2.setChecksumType(ChecksumType.SHA_256);
		fichier2.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.persist(fichier1);
		entityManager.persist(fichier2);
		entityManager.flush();
		entityManager.clear();

		assertThat(entityManager.find(Fichier.class, fichier1.getId()).getType()).isEqualTo(FichierType1.CONTENT1);
		assertThat(entityManager.find(Fichier.class, fichier2.getId()).getType()).isEqualTo(FichierType2.CONTENT3);

		Session session = entityManager.unwrap(Session.class);
		session.doWork(c -> {
			try (PreparedStatement s = c.prepareStatement("select id, type from fichier order by id")) {
				ResultSet rs = s.executeQuery();
				assertThat(rs.next()).isTrue();
				assertThat(rs.getString(2)).isEqualTo("CONTENT1");
				assertThat(rs.next()).isTrue();
				assertThat(rs.getString(2)).isEqualTo("CONTENT3");
				assertThat(rs.next()).isFalse();
			}
		});
	}


	/**
	 * Persist {@link Fichier} with minimal attribute initialized
	 */
	@Test
	void testMinimalFichierPersist(EntityManager entityManager, EntityTransaction transaction) {
		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setId(1l);
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		Fichier fichier = new Fichier();
		fichier.setId(1l);
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.ALIVE);
		fichier.setType(FichierType1.CONTENT1);
		fichier.setStorageUnit(storageUnit);
		fichier.setRelativePath("/relative-path");
		fichier.setFilename("filename");
		fichier.setSize(25l);
		fichier.setChecksum("123");
		fichier.setMimetype("application/octet-stream");
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.persist(fichier);
		entityManager.flush();
		entityManager.clear();

		assertThat(entityManager.find(Fichier.class, fichier.getId())).isNotNull();
	}

	/**
	 * Check {@link Fichier#uuid} unicity
	 */
	@Test
	void testFichierUuidUnicityPersist(EntityManager entityManager, EntityTransaction transaction, @JdbcDriver Class<?> jdbcDriver) {
		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setId(1l);
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		UUID uuid = UUID.randomUUID();
		Fichier fichier1 = new Fichier();
		fichier1.setId(1l);
		fichier1.setUuid(uuid);
		fichier1.setStatus(FichierStatus.ALIVE);
		fichier1.setType(FichierType1.CONTENT1);
		fichier1.setStorageUnit(storageUnit);
		fichier1.setRelativePath("/relative-path");
		fichier1.setFilename("filename");
		fichier1.setSize(25l);
		fichier1.setChecksum("123");
		fichier1.setMimetype("application/octet-stream");
		fichier1.setChecksumType(ChecksumType.SHA_256);
		fichier1.setCreationDate(LocalDateTime.now());

		Fichier fichier2 = new Fichier();
		fichier2.setId(2l);
		fichier2.setUuid(uuid);
		fichier2.setStatus(FichierStatus.ALIVE);
		fichier2.setType(FichierType1.CONTENT1);
		fichier2.setStorageUnit(storageUnit);
		fichier2.setRelativePath("/relative-path");
		fichier2.setFilename("filename");
		fichier2.setSize(25l);
		fichier2.setChecksum("123");
		fichier2.setMimetype("application/octet-stream");
		fichier2.setChecksumType(ChecksumType.SHA_256);
		fichier2.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.persist(fichier1);
		entityManager.persist(fichier2);
		assertThatThrownBy(() -> entityManager.flush())
			.isInstanceOf(PersistenceException.class)
			.hasMessageContaining(jdbcDriver.equals(Driver.class) ?
					"duplicate key value violates": // postgresql
					"Unique index or primary key violation"); // h2
	}

	/**
	 * Persist {@link StorageUnit} and {@link StorageUnitStatistics}
	 */
	@Test
	void testStorageUnitAndStatisticsPersist(EntityManager entityManager, EntityTransaction transaction) {
		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setId(1l);
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.flush();
		entityManager.clear();

		assertThat(entityManager.find(StorageUnit.class, storageUnit.getId())).isNotNull();
	}

	@Test
	void testGenerateStorageId(EntityManagerFactory entityManagerFactory) throws InterruptedException, ExecutionException {
		long id1 = doInReadTransaction(entityManagerFactory, () -> databaseOperations.generateStorageUnit());
		assertThat(doInReadTransaction(entityManagerFactory, () -> databaseOperations.generateStorageUnit())).isEqualTo(id1 + 1);
		
		// test concurrency : ids are distinct event for simultaneous transaction
		// (this is an expected behavior as we use sequence).
		CountDownLatch latch1 = new CountDownLatch(1);
		CountDownLatch latch2 = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<Long> id3 = executor.submit(() -> doInReadTransaction(entityManagerFactory, () -> {
			// wait for id4 generation
			try {
				latch1.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
			Long id = databaseOperations.generateStorageUnit();
			// wait for id4 transaction end
			latch2.countDown();
			return id;
		}));
		Future<Long> id4 = executor.submit(() -> doInReadTransaction(entityManagerFactory, () -> {
			// generate
			Long id = databaseOperations.generateStorageUnit();
			// trigger and wait id3 generation
			latch1.countDown();
			try {
				latch2.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
			return id;
		}));
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);
		
		assertThat(id3.isDone()).isTrue();
		assertThat(id4.isDone()).isTrue();
		assertThat(id3.get()).isNotEqualTo(id4.get());
		assertThat(id3.get() - id4.get()).as("id3 is generated after id4 in sequence").isEqualTo(1);
	}

	@Test
	void testGenerateFichierId(EntityManagerFactory entityManagerFactory) throws InterruptedException, ExecutionException {
		long id1 = doInReadTransaction(entityManagerFactory, () -> databaseOperations.generateFichier());
		assertThat(doInReadTransaction(entityManagerFactory, () -> databaseOperations.generateFichier())).isEqualTo(id1 + 1);
		
		// test concurrency : ids are distinct event for simultaneous transaction
		// (this is an expected behavior as we use sequence).
		CountDownLatch latch1 = new CountDownLatch(1);
		CountDownLatch latch2 = new CountDownLatch(1);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<Long> id3 = executor.submit(() -> doInReadTransaction(entityManagerFactory, () -> {
			// wait for id4 generation
			try {
				latch1.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
			Long id = databaseOperations.generateFichier();
			// wait for id4 transaction end
			latch2.countDown();
			return id;
		}));
		Future<Long> id4 = executor.submit(() -> doInReadTransaction(entityManagerFactory, () -> {
			// generate
			Long id = databaseOperations.generateFichier();
			// trigger and wait id3 generation
			latch1.countDown();
			try {
				latch2.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
			return id;
		}));
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);
		
		assertThat(id3.isDone()).isTrue();
		assertThat(id4.isDone()).isTrue();
		assertThat(id3.get()).isNotEqualTo(id4.get());
		assertThat(id3.get() - id4.get()).as("id3 is generated after id4 in sequence").isEqualTo(1);
	}

	@Test
	void testListUnitAliveFichiers(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageUnit storageUnit2 = createStorageUnit(entityManagerFactory, 2l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);

		assertThatCode(() -> databaseOperations.listUnitAliveFichiers(null)).isInstanceOf(NullPointerException.class);
		
		{
			Set<Fichier> fichiers = doInReadTransactionEntityManager(entityManagerFactory, (em) -> {
				StorageUnit unit = em.find(StorageUnit.class, storageUnit1.getId());
				return databaseOperations.listUnitAliveFichiers(unit);
			});
			assertThat(fichiers).as("There is no Fichier in database").isEmpty();
		}

		{
			Fichier fichierAlive = createFichier(entityManagerFactory, storageUnit2, 1l, FichierStatus.ALIVE);
			Set<Fichier> fichiersUnit1 = doInReadTransactionEntityManager(entityManagerFactory, (em) -> {
				StorageUnit unit = em.find(StorageUnit.class, storageUnit1.getId());
				return databaseOperations.listUnitAliveFichiers(unit);
			});
			Set<Fichier> fichiersUnit2 = doInReadTransactionEntityManager(entityManagerFactory, (em) -> {
				StorageUnit unit = em.find(StorageUnit.class, storageUnit2.getId());
				return databaseOperations.listUnitAliveFichiers(unit);
			});
			assertThat(fichiersUnit1).as("There is no Fichier in storage unit 1 in database").isEmpty();
			assertThat(fichiersUnit2).as("There is 1 Fichier associated with unit")
				.hasSize(1)
				.contains(fichierAlive);
		}

		{
			Fichier fichierAlive = createFichier(entityManagerFactory, storageUnit1, 2l, FichierStatus.ALIVE);
			Fichier fichierInvalidated = createFichier(entityManagerFactory, storageUnit1, 3l, FichierStatus.INVALIDATED);
			Fichier fichierTransient = createFichier(entityManagerFactory, storageUnit1, 4l, FichierStatus.TRANSIENT);
			Set<Fichier> fichiers = doInReadTransactionEntityManager(entityManagerFactory, (em) -> {
				StorageUnit unit = em.find(StorageUnit.class, storageUnit1.getId());
				return databaseOperations.listUnitAliveFichiers(unit);
			});
			assertThat(fichiers).as("There is 3 Fichier associated with unit, and only 2 are OK")
				.hasSize(2)
				.contains(fichierAlive)
				.contains(fichierTransient)
				.doesNotContain(fichierInvalidated);
		}
	}

	@Test
	void testLoadAliveStorageUnitNoUnit(EntityManagerFactory entityManagerFactory) {
		assertThatCode(() -> doInReadTransaction(entityManagerFactory, () -> {
			return databaseOperations.loadAliveStorageUnit(StorageUnitType.TYPE_1);
		})).as("No storage unit available").isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void testLoadAliveStorageUnitNoAliveUnit(EntityManagerFactory entityManagerFactory) {
		createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ARCHIVED);
		assertThatCode(() -> doInReadTransaction(entityManagerFactory, () -> {
			return databaseOperations.loadAliveStorageUnit(StorageUnitType.TYPE_1);
		})).as("No storage unit with the right status available").isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void testLoadAliveStorageUnitNoTypeUnit(EntityManagerFactory entityManagerFactory) {
		createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE);
		assertThatCode(() -> doInReadTransaction(entityManagerFactory, () -> {
			return databaseOperations.loadAliveStorageUnit(StorageUnitType.TYPE_1);
		})).as("No storage unit with the right type available").isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void testLoadAliveStorageUnitAvailable(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		assertThat(doInReadTransaction(entityManagerFactory, () -> {
			return databaseOperations.loadAliveStorageUnit(StorageUnitType.TYPE_1);
		})).as("There is 1 available storage unit").isEqualTo(storageUnit);
	}

	@Test
	void testLoadAliveStorageUnitMultipleAvailable(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit2 = createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		assertThat(doInReadTransaction(entityManagerFactory, () -> {
			return databaseOperations.loadAliveStorageUnit(StorageUnitType.TYPE_1);
		})).as("Last by id matching unit expected").isEqualTo(storageUnit2);
	}

	@Test
	void testTriggerFailure(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test"), fichier1, check));
		LocalDateTime afterTrigger = LocalDateTime.now();
		assertThat(listFailures(entityManagerFactory))
			.hasSize(1)
			.satisfies(f -> {
				// check failure is here with expected dates (before after)
				assertThat(f)
					.id(a -> a.isNotNull())
					.fixedOn(a -> a.isNull())
					.type(a -> a.isEqualTo(StorageFailureType.MISSING_FILE))
					.status(a -> a.isEqualTo(StorageFailureStatus.ALIVE))
					.consistencyCheck(a -> a.isEqualTo(check))
					.fichier(a -> a.isEqualTo(fichier1))
					.path(a -> a.isEqualTo("test"))
					.creationTime(a -> a.isBefore(afterTrigger))
					.lastFailureOn(a -> a.isEqualTo(f.getCreationTime()))
					.acknowledgedOn(a -> a.isNull())
					.fixedOn(a -> a.isNull());
			}, Assertions.atIndex(0));
	}

	@Test
	void testTriggerFailureAlreadyExistingAlive(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		
		// trigger first failure
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test"), fichier1, check1));
		
		// trigger second check and new failure
		LocalDateTime afterTrigger = LocalDateTime.now();
		StorageConsistencyCheck check2 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		StorageFailure failure2 = StorageFailure.ofMissingFile(Path.of("test"), fichier1, check2);
		triggerFailure(entityManagerFactory, failure2);
		assertThat(listFailures(entityManagerFactory))
			.hasSize(1)
			.satisfies(f -> {
				assertThat(f)
					.id(a -> a.isNotNull())
					.fixedOn(a -> a.isNull())
					.type(a -> a.isEqualTo(StorageFailureType.MISSING_FILE))
					.status(a -> a.isEqualTo(StorageFailureStatus.ALIVE))
					.consistencyCheck(a -> a.isEqualTo(check2))
					.fichier(a -> a.isEqualTo(fichier1))
					.path(a -> a.isEqualTo("test"))
					.creationTime(a -> a.isBefore(afterTrigger))
					.lastFailureOn(a -> a.isAfter(afterTrigger))
					.acknowledgedOn(a -> a.isNull())
					.fixedOn(a -> a.isNull());
			}, Assertions.atIndex(0));
	}

	@Test
	void testTriggerFailureAlreadyExistingFixed(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		
		// trigger first failure and fix
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test"), fichier1, check1));
		updateSingleFailure(entityManagerFactory, f -> {
			f.setStatus(StorageFailureStatus.FIXED);
		});
		
		// trigger second check and new failure
		LocalDateTime afterTrigger = LocalDateTime.now();
		StorageConsistencyCheck check2 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		StorageFailure failure2 = StorageFailure.ofMissingFile(Path.of("test"), fichier1, check2);
		triggerFailure(entityManagerFactory, failure2);
		assertThat(listFailures(entityManagerFactory))
			.hasSize(1)
			.satisfies(
					f -> {
						// new failure replaces fixed one
						assertThat(f)
							.id(a -> a.isNotNull())
							.fixedOn(a -> a.isNull())
							.type(a -> a.isEqualTo(StorageFailureType.MISSING_FILE))
							.status(a -> a.isEqualTo(StorageFailureStatus.ALIVE))
							.consistencyCheck(a -> a.isEqualTo(check2))
							.fichier(a -> a.isEqualTo(fichier1))
							.path(a -> a.isEqualTo("test"))
							.creationTime(a -> a.isAfter(afterTrigger))
							.lastFailureOn(a -> a.isAfter(afterTrigger))
							.acknowledgedOn(a -> a.isNull())
							.fixedOn(a -> a.isNull());
					}, atIndex(0)
		);
	}

	@Test
	void testTriggerFailureAlreadyExistingAcknowledged(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		
		// trigger first failure and ack
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test"), fichier1, check1));
		updateSingleFailure(entityManagerFactory, f -> {
			f.setStatus(StorageFailureStatus.ACKNOWLEDGED);
			f.setAcknowledgedOn(LocalDateTime.now());
		});
		
		// trigger second check and new failure
		LocalDateTime afterAcknowledged = LocalDateTime.now();
		StorageConsistencyCheck check2 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		StorageFailure failure2 = StorageFailure.ofMissingFile(Path.of("test"), fichier1, check2);
		triggerFailure(entityManagerFactory, failure2);
		assertThat(listFailures(entityManagerFactory))
			.hasSize(1)
			.satisfies(
					f -> {
						// check that acknowledged failure is here and new failure is ignored
						assertThat(f)
							.id(a -> a.isNotNull())
							.fixedOn(a -> a.isNull())
							.type(a -> a.isEqualTo(StorageFailureType.MISSING_FILE))
							.status(a -> a.isEqualTo(StorageFailureStatus.ACKNOWLEDGED))
							.consistencyCheck(a -> a.isEqualTo(check1))
							.fichier(a -> a.isEqualTo(fichier1))
							.path(a -> a.isEqualTo("test"))
							.creationTime(a -> a.isBefore(afterAcknowledged))
							.lastFailureOn(a -> a.isBefore(afterAcknowledged))
							.acknowledgedOn(a -> a.isBefore(afterAcknowledged))
							.fixedOn(a -> a.isNull());
					}, atIndex(0)
		);
	}

	@Test
	void testTriggerFailureAlreadyExistingAcknowledgedChangeType(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		
		// trigger first failure and ack
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test"), fichier1, check1));
		updateSingleFailure(entityManagerFactory, fa -> {
			fa.setStatus(StorageFailureStatus.ACKNOWLEDGED);
			fa.setAcknowledgedOn(LocalDateTime.now());
		});
		LocalDateTime afterFixed = LocalDateTime.now();
		
		// trigger second check and new failure (changing type)
		StorageConsistencyCheck check2 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		StorageFailure failure2 = StorageFailure.ofMissingEntity("test", check2);
		triggerFailure(entityManagerFactory, failure2);
		assertThat(listFailures(entityManagerFactory))
			.hasSize(1)
			.satisfies(
					f -> {
						// new failure completely replaces the acknowledged one as type change
						assertThat(f)
							.id(a -> a.isNotNull())
							.fixedOn(a -> a.isNull())
							.type(a -> a.isEqualTo(StorageFailureType.MISSING_ENTITY))
							.status(a -> a.isEqualTo(StorageFailureStatus.ALIVE))
							.consistencyCheck(a -> a.isEqualTo(check2))
							.fichier(a -> a.isNull())
							.path(a -> a.isEqualTo("test"))
							.creationTime(a -> a.isAfter(afterFixed))
							.lastFailureOn(a -> a.isEqualTo(f.getCreationTime()))
							.acknowledgedOn(a -> a.isNull())
							.fixedOn(a -> a.isNull());
					}, atIndex(0)
		);
	}

	@Test
	void testCleanFailures(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		createFichier(entityManagerFactory, unit1, 2, FichierStatus.ALIVE);
		Fichier fichier3 = createFichier(entityManagerFactory, unit1, 3, FichierStatus.ALIVE);
		Fichier fichier4 = createFichier(entityManagerFactory, unit1, 4, FichierStatus.ALIVE);
		Fichier fichier5 = createFichier(entityManagerFactory, unit1, 5, FichierStatus.ALIVE);
		Fichier fichier6 = createFichier(entityManagerFactory, unit1, 6, FichierStatus.ALIVE);
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test1"), fichier1, check1));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("test2", check1));
		triggerFailure(entityManagerFactory, StorageFailure.ofSizeMismatch(Path.of("test3"), fichier3, check1));
		triggerFailure(entityManagerFactory, StorageFailure.ofChecksumMismatch(Path.of("test4"), fichier4, check1));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test5"), fichier5, check1));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test6"), fichier6, check1));
		updateMatchingFailure(entityManagerFactory, f -> "test6".equals(f.getPath()), f -> f.setStatus(StorageFailureStatus.ACKNOWLEDGED));
		
		assertThat(listFailures(entityManagerFactory)).hasSize(6);
		
		StorageConsistencyCheck check2 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		{
			// first recheck (without checksum)
			// check without checksum -> previous checksum failures must be kept
			doInWriteTransaction(entityManagerFactory, () -> {
				databaseOperations.cleanFailures(check2, false);
				return null;
			});
			triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test5"), fichier5, check2));
			
			// if not redetected -> FIXED
			// if rededected -> associated with new check
			// if checksum -> kept
			assertThat(listFailures(entityManagerFactory)).hasSize(6)
				.satisfies(matcher(check1, "test1", StorageFailureType.MISSING_FILE, StorageFailureStatus.FIXED), atIndex(0))
				.satisfies(matcher(check1, "test2", StorageFailureType.MISSING_ENTITY, StorageFailureStatus.FIXED), atIndex(1))
				.satisfies(matcher(check1, "test3", StorageFailureType.SIZE_MISMATCH, StorageFailureStatus.FIXED), atIndex(2))
				.satisfies(matcher(check1, "test4", StorageFailureType.CHECKSUM_MISMATCH, StorageFailureStatus.ALIVE), atIndex(3))
				.satisfies(matcher(check2, "test5", StorageFailureType.MISSING_FILE, StorageFailureStatus.ALIVE), atIndex(4))
				.satisfies(matcher(check1, "test6", StorageFailureType.MISSING_FILE, StorageFailureStatus.ACKNOWLEDGED), atIndex(5));
		}
		
		StorageConsistencyCheck check3 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		{
			// second recheck (with checksum)
			// check with checksum -> previous checksum failures are marked fixed
			doInWriteTransaction(entityManagerFactory, () -> {
				databaseOperations.cleanFailures(check3, true);
				return null;
			});
			triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("test5"), fichier5, check2));
			
			assertThat(listFailures(entityManagerFactory)).hasSize(6)
				.satisfies(matcher(check1, "test1", StorageFailureType.MISSING_FILE, StorageFailureStatus.FIXED), atIndex(0))
				.satisfies(matcher(check1, "test2", StorageFailureType.MISSING_ENTITY, StorageFailureStatus.FIXED), atIndex(1))
				.satisfies(matcher(check1, "test3", StorageFailureType.SIZE_MISMATCH, StorageFailureStatus.FIXED), atIndex(2))
				.satisfies(matcher(check1, "test4", StorageFailureType.CHECKSUM_MISMATCH, StorageFailureStatus.FIXED), atIndex(3))
				.satisfies(matcher(check2, "test5", StorageFailureType.MISSING_FILE, StorageFailureStatus.ALIVE), atIndex(4))
				.satisfies(matcher(check1, "test6", StorageFailureType.MISSING_FILE, StorageFailureStatus.ACKNOWLEDGED), atIndex(5));
		}
		
		{
			// third recheck, no failure
			StorageConsistencyCheck check4 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
			// check with checksum -> previous checksum failures are marked fixed
			doInWriteTransaction(entityManagerFactory, () -> {
				databaseOperations.cleanFailures(check4, true);
				return null;
			});
			
			assertThat(listFailures(entityManagerFactory)).hasSize(6)
				.satisfies(matcher(check1, "test1", StorageFailureType.MISSING_FILE, StorageFailureStatus.FIXED), atIndex(0))
				.satisfies(matcher(check1, "test2", StorageFailureType.MISSING_ENTITY, StorageFailureStatus.FIXED), atIndex(1))
				.satisfies(matcher(check1, "test3", StorageFailureType.SIZE_MISMATCH, StorageFailureStatus.FIXED), atIndex(2))
				.satisfies(matcher(check1, "test4", StorageFailureType.CHECKSUM_MISMATCH, StorageFailureStatus.FIXED), atIndex(3))
				.satisfies(matcher(check2, "test5", StorageFailureType.MISSING_FILE, StorageFailureStatus.FIXED), atIndex(4))
				.satisfies(matcher(check1, "test6", StorageFailureType.MISSING_FILE, StorageFailureStatus.ACKNOWLEDGED), atIndex(5));
		}
		
	}

	@Test
	void testCreateRemoveFichier(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		doInWriteTransactionEntityManager(entityManagerFactory, em -> {
			Fichier fichier = new Fichier();
			fichier.setId(1l);
			fichier.setUuid(UUID.randomUUID());
			fichier.setStatus(FichierStatus.ALIVE);
			fichier.setType(FichierType1.CONTENT1);
			fichier.setStorageUnit(em.find(StorageUnit.class, storageUnit.getId()));
			fichier.setRelativePath(String.format("/relative-path-%d", fichier.getId()));
			fichier.setFilename("filename");
			fichier.setSize(25l);
			fichier.setChecksum("123");
			fichier.setMimetype("application/octet-stream");
			fichier.setChecksumType(ChecksumType.SHA_256);
			fichier.setCreationDate(LocalDateTime.now());
			databaseOperations.createFichier(fichier);
			return fichier;
		});
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(em.find(Fichier.class, 1l)).isNotNull();
			return null;
		});
		doInWriteTransactionEntityManager(entityManagerFactory, em -> {
			Fichier fichier = em.find(Fichier.class, 1l);
			databaseOperations.removeFichier(fichier);
			return fichier;
		});
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(em.find(Fichier.class, 1l)).isNull();
			return null;
		});
	}

	@Test
	void testGetStorageUnit(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getStorageUnit(storageUnit.getId())).isNotNull();
			return null;
		});
	}

	@Test
	void testGetStorageUnitEmpty(EntityManagerFactory entityManagerFactory) {
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getStorageUnit(1l)).isNull();
			return null;
		});
	}

	@Test
	void testListStorageUnit(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit1 = createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageUnit storageUnit2 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			// ordered by id
			assertThat(databaseOperations.listStorageUnits()).contains(storageUnit2, storageUnit1);
			return null;
		});
	}

	@Test
	void testGetLastCheckEmpty(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheck(unit1)).isNull();
			return null;
		});
	}

	@Test
	void testGetLastCheckFilterUnit(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageUnit unit2 = createStorageUnit(entityManagerFactory, 2l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
		createConsistencyCheck(entityManagerFactory, unit2, StorageUnitCheckType.LISTING_SIZE, LocalDateTime.now().plus(Duration.ofDays(1)), StorageConsistencyCheckResult.OK);
		
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheck(unit1)).isEqualTo(check1);
			return null;
		});
	}
	
	@Test
	void testGetLastCheckOrder(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, LocalDateTime.now().minus(Duration.ofDays(1)), StorageConsistencyCheckResult.OK);
		
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheck(unit1)).isEqualTo(check1);
			return null;
		});
	}
	
	@Test
	void testGetLastCheckAnyType(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, LocalDateTime.now().minus(Duration.ofMinutes(1)), StorageConsistencyCheckResult.OK);
		StorageConsistencyCheck check2 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
		
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheck(unit1)).isEqualTo(check2);
			return null;
		});
	}

	@Test
	void testGetLastCheckChecksumEmpty(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheck(unit1)).isNull();
			return null;
		});
	}

	@Test
	void testGetLastCheckChecksumFilterUnit(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageUnit unit2 = createStorageUnit(entityManagerFactory, 2l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
		createConsistencyCheck(entityManagerFactory, unit2, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, LocalDateTime.now().plus(Duration.ofDays(1)), StorageConsistencyCheckResult.OK);
		
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheckChecksum(unit1)).isEqualTo(check1);
			return null;
		});
	}
	
	@Test
	void testGetLastCheckChecksumOrder(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, LocalDateTime.now().minus(Duration.ofDays(1)), StorageConsistencyCheckResult.OK);
		
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheckChecksum(unit1)).isEqualTo(check1);
			return null;
		});
	}
	
	@Test
	void testGetLastCheckChecksumFilterType(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check1 = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, LocalDateTime.now().minusDays(1), StorageConsistencyCheckResult.OK);
		// ignored as it is not a checksum check
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
		
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(databaseOperations.getLastCheckChecksum(unit1)).isEqualTo(check1);
			return null;
		});
	}

	@Test
	void testCreateConsistencyCheck(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check = doInWriteTransactionEntityManager(entityManagerFactory, em -> {
			StorageConsistencyCheck c = new StorageConsistencyCheck();
			c.setCheckFinishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
			c.setCheckStartedOn(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
			c.setCheckType(StorageUnitCheckType.LISTING_SIZE);
			c.setStorageUnit(em.find(StorageUnit.class, storageUnit.getId()));
			databaseOperations.createConsistencyCheck(c);
			return c;
		});
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			assertThat(em.find(StorageConsistencyCheck.class, check.getId()))
				.usingRecursiveComparison()
				.ignoringFields("fichiers", "storageUnit.fichiers", "storageUnit.consistencyChecks").isEqualTo(check);
			return null;
		});
	}

	@Test
	void testGetStorageStatistics(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createFichier(entityManagerFactory, storageUnit1, 11l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		createFichier(entityManagerFactory, storageUnit1, 12l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		createFichier(entityManagerFactory, storageUnit1, 13l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		
		StorageUnit storageUnit2 = createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE);
		createFichier(entityManagerFactory, storageUnit2, 21l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		createFichier(entityManagerFactory, storageUnit2, 22l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		createFichier(entityManagerFactory, storageUnit2, 23l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		
		List<StorageStatistic> statistics = doInReadTransactionEntityManager(entityManagerFactory, em -> databaseOperations.getStorageStatistics());
		assertThat(statistics)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT1);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(2);
					assertThat(i.getSize()).isEqualTo(2 * 25);
				}, atIndex(0)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT2);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(1)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(2l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT2);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(3);
					assertThat(i.getSize()).isEqualTo(3 * 25);
				}, atIndex(2)
		);
	}

	@Test
	void testGetStorageFailureStatistics(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		Fichier fichier11  = createFichier(entityManagerFactory, storageUnit1, 11l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		Fichier fichier12 = createFichier(entityManagerFactory, storageUnit1, 12l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		Fichier fichier13 = createFichier(entityManagerFactory, storageUnit1, 13l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		Fichier fichier14 = createFichier(entityManagerFactory, storageUnit1, 14l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		Fichier fichier15 = createFichier(entityManagerFactory, storageUnit1, 15l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		StorageConsistencyCheck check11 = createConsistencyCheck(entityManagerFactory, storageUnit1, StorageUnitCheckType.LISTING_SIZE);
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("fichier11"), fichier11, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofSizeMismatch(Path.of("fichier12"), fichier12, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofChecksumMismatch(Path.of("fichier13"), fichier13, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofChecksumMismatch(Path.of("fichier14"), fichier14, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofChecksumMismatch(Path.of("fichier15"), fichier15, check11));
		updateMatchingFailure(entityManagerFactory, f -> Long.valueOf(14).equals(f.getFichier().getId()), f -> f.setStatus(StorageFailureStatus.ACKNOWLEDGED));
		updateMatchingFailure(entityManagerFactory, f -> Long.valueOf(15).equals(f.getFichier().getId()), f -> f.setStatus(StorageFailureStatus.FIXED));
		
		StorageUnit storageUnit2 = createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE);
		Fichier fichier21 = createFichier(entityManagerFactory, storageUnit2, 21l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		Fichier fichier22 = createFichier(entityManagerFactory, storageUnit2, 22l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		Fichier fichier23 = createFichier(entityManagerFactory, storageUnit2, 23l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		StorageConsistencyCheck check21 = createConsistencyCheck(entityManagerFactory, storageUnit2, StorageUnitCheckType.LISTING_SIZE);
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("fichier21"), fichier21, check21));
		triggerFailure(entityManagerFactory, StorageFailure.ofSizeMismatch(Path.of("fichier22"), fichier22, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofSizeMismatch(Path.of("fichier23"), fichier23, check11));
		
		List<StorageFailureStatistic> statistics = doInReadTransactionEntityManager(entityManagerFactory, em -> databaseOperations.getStorageFailureStatistics());
		assertThat(statistics)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT1);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.CHECKSUM_MISMATCH);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.FIXED);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(0)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT1);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.MISSING_FILE);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(1)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT1);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.SIZE_MISMATCH);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(2)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT2);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.CHECKSUM_MISMATCH);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ACKNOWLEDGED);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(3)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT2);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.CHECKSUM_MISMATCH);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(4)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(2l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT2);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.MISSING_FILE);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(1);
					assertThat(i.getSize()).isEqualTo(1 * 25);
				}, atIndex(5)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(2l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
					assertThat(i.getFichierType()).isEqualTo(FichierType1.CONTENT2);
					assertThat(i.getFichierStatus()).isEqualTo(FichierStatus.ALIVE);
					assertThat(i.getFailureType()).isEqualTo(StorageFailureType.SIZE_MISMATCH);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(2);
					assertThat(i.getSize()).isEqualTo(2 * 25);
				}, atIndex(6)
		);
	}

	@Test
	void testGetStorageOrphanStatistics(EntityManagerFactory entityManagerFactory) {
		StorageUnit storageUnit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		Fichier fichier11  = createFichier(entityManagerFactory, storageUnit1, 11l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		Fichier fichier12 = createFichier(entityManagerFactory, storageUnit1, 12l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		createFichier(entityManagerFactory, storageUnit1, 13l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		createFichier(entityManagerFactory, storageUnit1, 14l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		createFichier(entityManagerFactory, storageUnit1, 15l, FichierStatus.ALIVE, FichierType1.CONTENT1);
		StorageConsistencyCheck check11 = createConsistencyCheck(entityManagerFactory, storageUnit1, StorageUnitCheckType.LISTING_SIZE);
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingFile(Path.of("fichier11"), fichier11, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofSizeMismatch(Path.of("fichier12"), fichier12, check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("fichier13", check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("fichier14", check11));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("fichier15", check11));
		updateMatchingFailure(entityManagerFactory, f -> "fichier14".equals(f.getPath()), f -> f.setStatus(StorageFailureStatus.ACKNOWLEDGED));
		updateMatchingFailure(entityManagerFactory, f -> "fichier15".equals(f.getPath()), f -> f.setStatus(StorageFailureStatus.FIXED));

		StorageUnit storageUnit2 = createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE);
		createFichier(entityManagerFactory, storageUnit2, 21l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		createFichier(entityManagerFactory, storageUnit2, 22l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		createFichier(entityManagerFactory, storageUnit2, 23l, FichierStatus.ALIVE, FichierType1.CONTENT2);
		StorageConsistencyCheck check21 = createConsistencyCheck(entityManagerFactory, storageUnit2, StorageUnitCheckType.LISTING_SIZE);
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("fichier21", check21));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("fichier22", check21));
		triggerFailure(entityManagerFactory, StorageFailure.ofMissingEntity("fichier23", check21));
		
		List<StorageOrphanStatistic> statistics = doInReadTransactionEntityManager(entityManagerFactory, em -> databaseOperations.getStorageOrphanStatistics());
		
		assertThat(statistics)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ACKNOWLEDGED);
					assertThat(i.getCount()).isEqualTo(1);
				}, atIndex(0)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(1);
				}, atIndex(1)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(1l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.FIXED);
					assertThat(i.getCount()).isEqualTo(1);
				}, atIndex(2)
		)
		.satisfies(
				i -> {
					assertThat(i.getStorageUnitId()).isEqualTo(2l);
					assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
					assertThat(i.getFailureStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getCount()).isEqualTo(3);
				}, atIndex(3)
		);
	}

	@Test
	void testGetStorageCheckStatistics(EntityManagerFactory entityManagerFactory) {
		// statistic precision is limited to second
		LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		StorageUnit unit2 = createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE);
		// only run with postgresql
		Assumptions.assumeThat(
				Optional.ofNullable(System.getenv("TEST_DB_TYPE")).filter(Predicate.not(Strings::isNullOrEmpty)).filter("postgresql"::equals)
		).as("testGetStorageCheckStatistics only available with postgresql")
		.isNotEmpty();
		
		assertThat(getStorageCheckStatistics(entityManagerFactory)).hasSize(2)
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(1);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
			assertThat(i.getLastOn()).isNull();
			assertThat(i.getLastDuration()).isNull();
			assertThat(i.getLastAge()).isNull();
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(0))
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(2);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
			assertThat(i.getLastOn()).isNull();
			assertThat(i.getLastDuration()).isNull();
			assertThat(i.getLastAge()).isNull();
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(1));
		
		Duration unit1LastDuration = Duration.ofMinutes(3);
		Duration unit1LastAge = Duration.ofDays(3);
		LocalDateTime unit1LastFinishedOn = now.minus(unit1LastAge);
		LocalDateTime unit1LastStartedOn = unit1LastFinishedOn.minus(unit1LastDuration);
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, unit1LastStartedOn, unit1LastFinishedOn, StorageConsistencyCheckResult.OK);
		
		assertThat(getStorageCheckStatistics(entityManagerFactory)).hasSize(2)
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(1);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
			assertThat(i.getLastOn()).isEqualTo(unit1LastFinishedOn);
			assertThat(i.getLastDuration()).isEqualTo(unit1LastDuration);
			// bdd uses now -> we cannot have exact equality
			assertThat(i.getLastAge()).isCloseTo(unit1LastAge, Duration.ofSeconds(10));
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(0))
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(2);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
			assertThat(i.getLastOn()).isNull();
			assertThat(i.getLastDuration()).isNull();
			assertThat(i.getLastAge()).isNull();
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(1));
		
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE, unit1LastStartedOn.minus(Duration.ofDays(1)), unit1LastStartedOn.minus(Duration.ofDays(1)), StorageConsistencyCheckResult.OK);
		
		// no change as consistencyCheck is older
		assertThat(getStorageCheckStatistics(entityManagerFactory)).hasSize(2)
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(1);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
			assertThat(i.getLastOn()).isEqualTo(unit1LastFinishedOn);
			assertThat(i.getLastDuration()).isEqualTo(unit1LastDuration);
			// bdd uses now -> we cannot have exact equality
			assertThat(i.getLastAge()).isCloseTo(unit1LastAge, Duration.ofSeconds(10));
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(0))
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(2);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
			assertThat(i.getLastOn()).isNull();
			assertThat(i.getLastDuration()).isNull();
			assertThat(i.getLastAge()).isNull();
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(1));
		
		createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, unit1LastStartedOn.minus(Duration.ofDays(2)), unit1LastFinishedOn.minus(Duration.ofDays(2)), StorageConsistencyCheckResult.OK);
		
		// checksum result
		assertThat(getStorageCheckStatistics(entityManagerFactory)).hasSize(2)
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(1);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
			assertThat(i.getLastOn()).isEqualTo(unit1LastFinishedOn);
			assertThat(i.getLastDuration()).isEqualTo(unit1LastDuration);
			// bdd uses now -> we cannot have exact equality
			assertThat(i.getLastAge()).isCloseTo(unit1LastAge, Duration.ofSeconds(10));
			assertThat(i.getLastChecksumOn()).isEqualTo(unit1LastFinishedOn.minusDays(2));
			assertThat(i.getLastChecksumDuration()).isEqualTo(unit1LastDuration);
			assertThat(i.getLastChecksumAge()).isCloseTo(unit1LastAge.plusDays(2), Duration.ofSeconds(10));
		}, atIndex(0))
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(2);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
			assertThat(i.getLastOn()).isNull();
			assertThat(i.getLastDuration()).isNull();
			assertThat(i.getLastAge()).isNull();
			assertThat(i.getLastChecksumOn()).isNull();
			assertThat(i.getLastChecksumDuration()).isNull();
			assertThat(i.getLastChecksumAge()).isNull();
		}, atIndex(1));
		
		createConsistencyCheck(entityManagerFactory, unit2, StorageUnitCheckType.LISTING_SIZE_CHECKSUM, unit1LastStartedOn, unit1LastFinishedOn, StorageConsistencyCheckResult.OK);
		
		// checksum on unit2 provides result both for last basic and checksum
		assertThat(getStorageCheckStatistics(entityManagerFactory)).hasSize(2)
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(1);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_1);
			assertThat(i.getLastOn()).isEqualTo(unit1LastFinishedOn);
			assertThat(i.getLastDuration()).isEqualTo(unit1LastDuration);
			// bdd uses now -> we cannot have exact equality
			assertThat(i.getLastAge()).isCloseTo(unit1LastAge, Duration.ofSeconds(10));
			assertThat(i.getLastChecksumOn()).isEqualTo(unit1LastFinishedOn.minusDays(2));
			assertThat(i.getLastChecksumDuration()).isEqualTo(unit1LastDuration);
			assertThat(i.getLastChecksumAge()).isCloseTo(unit1LastAge.plusDays(2), Duration.ofSeconds(10));
		}, atIndex(0))
		.satisfies(i -> {
			assertThat(i.getStorageUnitId()).isEqualTo(2);
			assertThat(i.getStorageUnitType()).isEqualTo(StorageUnitType.TYPE_2);
			assertThat(i.getLastOn()).isEqualTo(unit1LastFinishedOn);
			assertThat(i.getLastDuration()).isEqualTo(unit1LastDuration);
			// bdd uses now -> we cannot have exact equality
			assertThat(i.getLastAge()).isCloseTo(unit1LastAge, Duration.ofSeconds(10));
			assertThat(i.getLastChecksumOn()).isEqualTo(unit1LastFinishedOn);
			assertThat(i.getLastChecksumDuration()).isEqualTo(unit1LastDuration);
			assertThat(i.getLastChecksumAge()).isCloseTo(unit1LastAge, Duration.ofSeconds(10));
		}, atIndex(1));
	}

	@Test
	void testGetStorageCheckStatisticsNotSupported(EntityManagerFactory entityManagerFactory, @JdbcDriver Class<?> driver) {
		// only run without postgresql
		Assumptions.assumeThat(driver.equals(Driver.class)).as("testGetStorageCheckStatistics only available with postgresql").isFalse();
		assertThatCode(() -> {
			getStorageCheckStatistics(entityManagerFactory);
		}).as("Exception explaining that method is not supported without postgresql").isInstanceOf(IllegalStateException.class)
		.hasMessageContaining("postgresql");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testFichierCreatedBy(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		Fichier fichier = createFichier(entityManagerFactory, unit, 1l, FichierStatus.ALIVE, FichierType1.CONTENT1, LongEntityReference.of(GenericEntity.class, 1l), LocalDateTime.now(), 25l);
		doInReadTransactionEntityManager(entityManagerFactory, em -> {
			List<Tuple> result = em.createNativeQuery("SELECT createdby_type, createdby_id FROM Fichier;", Tuple.class).getResultList();
			assertThat(result).hasSize(1);
			assertThat(result.get(0).get("createdby_type")).isEqualTo(GenericEntity.class.getName()).isEqualTo(fichier.getCreatedBy().getType().getName());
			// java long is stored as sql biginteger 
			assertThat(result.get(0).get("createdby_id")).isEqualTo(1l).isEqualTo(fichier.getCreatedBy().getId());
			return null;
		});
	}

	@Test
	void testListInvalidated(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createFichier(entityManagerFactory, unit, 1l, FichierStatus.ALIVE);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.TRANSIENT);
		Fichier fichier4 = createFichier(entityManagerFactory, unit, 4l, FichierStatus.INVALIDATED);
		Fichier fichier3 = createFichier(entityManagerFactory, unit, 3l, FichierStatus.INVALIDATED);
		
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listInvalidated(1)).hasSize(1).contains(fichier3);
			assertThat(databaseOperations.listInvalidated(null)).hasSize(2).containsExactly(fichier3, fichier4);
			return null;
		});
	}

	@Test
	void testListTransient(EntityManagerFactory entityManagerFactory) {
		LocalDateTime oneDayOld = LocalDateTime.now().minusDays(1);
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createFichier(entityManagerFactory, unit, 1l, FichierStatus.ALIVE);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE);
		Fichier fichier4 = createFichier(entityManagerFactory, unit, 4l, FichierStatus.TRANSIENT, oneDayOld.minusSeconds(1));
		Fichier fichier3 = createFichier(entityManagerFactory, unit, 3l, FichierStatus.TRANSIENT, oneDayOld.minusSeconds(1));
		// not old enough for cleaning
		createFichier(entityManagerFactory, unit, 5l, FichierStatus.TRANSIENT, oneDayOld.plusMinutes(1));
		
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listTransient(1, oneDayOld)).hasSize(1).contains(fichier3);
			assertThat(databaseOperations.listTransient(null, oneDayOld)).hasSize(2).containsExactly(fichier3, fichier4);
			return null;
		});
	}

	@Test
	void testRemove(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		Fichier fichier2 = createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE);
		doInWriteTransaction(entityManagerFactory, () -> {
			Fichier fichier = createFichier(entityManagerFactory, unit, 1l, FichierStatus.ALIVE);
			databaseOperations.removeFichier(fichier);
			return fichier;
		});
		doInReadTransactionEntityManager(entityManagerFactory, (em) -> {
			assertThat(em.createQuery("SELECT f FROM Fichier f", Fichier.class).getResultList()).hasSize(1).containsExactly(fichier2);
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplit(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).isEmpty();
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplitNoSizeOverflow(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now(), null, 26000l);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE, 25000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).isEmpty();
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplitSizeOverflow(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now(), null, 24000l);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE, 25000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).hasSize(1);
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplitSizeOverflowOnlyOne(EntityManagerFactory entityManagerFactory) {
		// split only this storage unit
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now(), null, 24000l);
		createFichier(entityManagerFactory, unit1, 2l, FichierStatus.ALIVE, 21000l);
		createFichier(entityManagerFactory, unit1, 4l, FichierStatus.ALIVE, 4000l);
		
		// below split limit
		StorageUnit unit2 = createStorageUnit(entityManagerFactory, 2l, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE, LocalDateTime.now(), null, 24000l);
		createFichier(entityManagerFactory, unit2, 5l, FichierStatus.ALIVE, 21000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).containsExactly(unit1);
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplitDurationNoOverflow(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now().minusDays(3), Duration.ofDays(3).plusMinutes(1), null);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE, 25000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).isEmpty();
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplitDurationOverflow(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now().minusDays(3), Duration.ofDays(3).minusMinutes(1), null);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE, 25000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).hasSize(1);
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplit_durationOverflow_sizeNoOverflow(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now().minusDays(3), Duration.ofDays(3).minusMinutes(1), 26000l);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE, 25000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).hasSize(1);
			return null;
		});
	}

	@Test
	void testListStorageUnitsToSplit_bothDurationSizeOverflow(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE, LocalDateTime.now().minusDays(3), Duration.ofDays(3).minusMinutes(1), 24000l);
		createFichier(entityManagerFactory, unit, 2l, FichierStatus.ALIVE, 25000l);
		doInReadTransaction(entityManagerFactory, () -> {
			assertThat(databaseOperations.listStorageUnitsToSplit()).hasSize(1);
			return null;
		});
	}

	@Test
	void testSplitStorageUnit(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE,
				LocalDateTime.now().minusDays(3), Duration.ofDays(3).minusMinutes(1), 25000l,
				Duration.ofMinutes(10), Duration.ofMinutes(15), StorageUnitCheckType.LISTING_SIZE);
		StorageUnit newUnit = doInWriteTransaction(entityManagerFactory, () -> {
			return databaseOperations.splitStorageUnit(unit, 2l, "path2");
		});
		assertThat(newUnit.getCheckChecksumDelay()).isEqualTo(unit.getCheckChecksumDelay());
		assertThat(newUnit.getCheckDelay()).isEqualTo(unit.getCheckDelay());
		assertThat(newUnit.getCheckType()).isEqualTo(unit.getCheckType());
		assertThat(newUnit.getPath()).isNotEqualTo(unit.getPath()).isNotNull();
		assertThat(newUnit.getId()).isEqualTo(2l);
		assertThat(newUnit.getSplitDuration()).isEqualTo(unit.getSplitDuration());
		assertThat(newUnit.getSplitSize()).isEqualTo(unit.getSplitSize());
		assertThat(newUnit.getStatus()).isEqualTo(StorageUnitStatus.ALIVE);
		assertThat(newUnit.getCreationDate()).isCloseTo(LocalDateTime.now(), byLessThan(1, ChronoUnit.MINUTES));
		assertThat(newUnit.getCheckChecksumDelay()).isEqualTo(unit.getCheckChecksumDelay());
		assertThat(newUnit.getCheckChecksumDelay()).isEqualTo(unit.getCheckChecksumDelay());
		StorageUnit old = doInReadTransactionEntityManager(entityManagerFactory, em -> {
			return em.find(StorageUnit.class, 1l);
		});
		assertThat(old.getStatus()).isEqualTo(StorageUnitStatus.ARCHIVED);
	}

	@Test
	void testSplitStorageUnitWrongStatus(EntityManagerFactory entityManagerFactory) {
		StorageUnit unit = createStorageUnit(entityManagerFactory, 1l, StorageUnitType.TYPE_1, StorageUnitStatus.ARCHIVED);
		assertThatCode(() -> doInWriteTransaction(entityManagerFactory, () -> {
			return databaseOperations.splitStorageUnit(unit, 2l, "path2");
		})).isInstanceOf(IllegalStateException.class).hasMessageContaining("Original status").hasMessageContaining("unexpected");
	}

	private List<StorageCheckStatistic> getStorageCheckStatistics(EntityManagerFactory entityManagerFactory) {
		return doInReadTransaction(entityManagerFactory, () -> {
			return databaseOperations.getStorageCheckStatistics();
		});
	}

	private Consumer<? super StorageFailure> matcher(StorageConsistencyCheck check, String path, StorageFailureType type, StorageFailureStatus status) {
		return f -> {
			try (StorageSoftAssertions softly = new StorageSoftAssertions()) {
				softly.assertThat(f).path(a -> a.isEqualTo(path))
					.status(a -> a.isEqualTo(status))
					.type(a -> a.isEqualTo(type))
					.consistencyCheck(a -> a.isEqualTo(check));
			}
		};
	}

	private List<StorageFailure> listFailures(EntityManagerFactory entityManagerFactory) {
		return doInReadTransactionEntityManager(entityManagerFactory, em -> {
			return em.createQuery(SELECT_STORAGE_FAILURE_ID_ASC, StorageFailure.class).getResultList();
		});
	}

	private void updateSingleFailure(EntityManagerFactory entityManagerFactory, Consumer<StorageFailure> updater) {
		doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			StorageFailure f = em.createQuery(SELECT_STORAGE_FAILURE_ID_ASC, StorageFailure.class).getSingleResult();
			updater.accept(f);
			return f;
		});
	}

	private void updateMatchingFailure(EntityManagerFactory entityManagerFactory, Predicate<StorageFailure> predicate, Consumer<StorageFailure> updater) {
		doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			StorageFailure f = em.createQuery(SELECT_STORAGE_FAILURE_ID_ASC, StorageFailure.class).getResultStream().filter(predicate).findFirst().orElseThrow();
			updater.accept(f);
			return f;
		});
	}

	private void triggerFailure(EntityManagerFactory entityManagerFactory, StorageFailure failure) {
		doInWriteTransaction(entityManagerFactory, () -> {
			databaseOperations.triggerFailure(failure);
			return null;
		});
	}

	private StorageConsistencyCheck createConsistencyCheck(EntityManagerFactory entityManagerFactory, StorageUnit unit1, StorageUnitCheckType type) {
		return createConsistencyCheck(entityManagerFactory, unit1, type, LocalDateTime.now(), StorageConsistencyCheckResult.OK);
	}

	private StorageConsistencyCheck createConsistencyCheck(EntityManagerFactory entityManagerFactory, StorageUnit unit1, StorageUnitCheckType type, LocalDateTime checkDate, StorageConsistencyCheckResult result) {
		return createConsistencyCheck(entityManagerFactory, unit1, type, checkDate, checkDate, result);
	}

	private StorageConsistencyCheck createConsistencyCheck(EntityManagerFactory entityManagerFactory, StorageUnit unit1, StorageUnitCheckType type, LocalDateTime startCheckDate, LocalDateTime endCheckDate, StorageConsistencyCheckResult result) {
		return doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			StorageConsistencyCheck check = new StorageConsistencyCheck();
			check.setCheckStartedOn(startCheckDate);
			check.setCheckFinishedOn(endCheckDate);
			check.setCheckType(type);
			check.setStorageUnit(unit1);
			check.setStatus(result);
			em.persist(check);
			return check;
		});
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status, Long fileSize) {
		return createFichier(entityManagerFactory, storageUnit, id, status, FichierType1.CONTENT1, (LongEntityReference) null, LocalDateTime.now(), fileSize);
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status) {
		return createFichier(entityManagerFactory, storageUnit, id, status, (LongEntityReference) null);
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status, LocalDateTime creationDate) {
		return createFichier(entityManagerFactory, storageUnit, id, status, FichierType1.CONTENT1, null, creationDate, 25l);
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status, LongEntityReference author) {
		return createFichier(entityManagerFactory, storageUnit, id, status, FichierType1.CONTENT1, author, LocalDateTime.now(), 25l);
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status, IFichierType type) {
		return createFichier(entityManagerFactory, storageUnit, id, status, type, null, LocalDateTime.now(), 25l);
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status, IFichierType type, LongEntityReference author, LocalDateTime creationDate, long fileSize) {
		return doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			Fichier fichier = new Fichier();
			fichier.setId(id);
			fichier.setUuid(UUID.randomUUID());
			fichier.setStatus(status);
			fichier.setType(type);
			fichier.setStorageUnit(em.find(StorageUnit.class, storageUnit.getId()));
			fichier.setRelativePath(String.format("/relative-path-%d", id));
			fichier.setFilename("filename");
			fichier.setSize(fileSize);
			fichier.setChecksum("123");
			fichier.setMimetype("application/octet-stream");
			fichier.setChecksumType(ChecksumType.SHA_256);
			fichier.setCreationDate(creationDate);
			fichier.setCreatedBy(author);
			em.persist(fichier);
			return fichier;
		});
	}

	private StorageUnit createStorageUnit(EntityManagerFactory entityManagerFactory, long id, StorageUnitType type, StorageUnitStatus status) {
		return createStorageUnit(entityManagerFactory, id, type, status, LocalDateTime.now(), null, null);
	}

	private StorageUnit createStorageUnit(EntityManagerFactory entityManagerFactory, long id, StorageUnitType type, StorageUnitStatus status, LocalDateTime creationDate, Duration splitDuration, Long splitSize) {
		return createStorageUnit(entityManagerFactory, id, type, status, creationDate, splitDuration, splitSize, null, null, StorageUnitCheckType.NONE);
	}

	private StorageUnit createStorageUnit(EntityManagerFactory entityManagerFactory, long id, StorageUnitType type, StorageUnitStatus status, LocalDateTime creationDate, Duration splitDuration, Long splitSize,
			Duration checkDelay, Duration checkChecksumDelay, StorageUnitCheckType checkType) {
		return doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			StorageUnit storageUnit;
			storageUnit = new StorageUnit();
			storageUnit.setId(id);
			storageUnit.setType(type);
			storageUnit.setPath(String.format("/test%d", id));
			storageUnit.setStatus(status);
			storageUnit.setCreationDate(creationDate);
			storageUnit.setSplitDuration(splitDuration);
			storageUnit.setSplitSize(splitSize);
			storageUnit.setCheckChecksumDelay(checkChecksumDelay);
			storageUnit.setCheckDelay(checkDelay);
			storageUnit.setCheckType(checkType);
			em.persist(storageUnit);
			return storageUnit;
		});
	}

}
