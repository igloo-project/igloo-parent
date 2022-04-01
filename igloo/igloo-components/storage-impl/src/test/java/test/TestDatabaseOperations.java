package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.atIndex;
import static test.StorageAssertions.assertThat;

import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

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
		fichier1.setName("filename");
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
		fichier2.setName("filename");
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
		fichier.setName("filename");
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
	void testFichierUuidUnicityPersist(EntityManager entityManager, EntityTransaction transaction) {
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
		fichier1.setName("filename");
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
		fichier2.setName("filename");
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
			.hasMessageContaining("ConstraintViolationException");
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
		return doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			StorageConsistencyCheck check = new StorageConsistencyCheck();
			check.setCheckFinishedOn(LocalDateTime.now());
			check.setCheckStartedOn(LocalDateTime.now());
			check.setCheckType(type);
			check.setStorageUnit(unit1);
			em.persist(check);
			return check;
		});
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, StorageUnit storageUnit, long id, FichierStatus status) {
		return doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			Fichier fichier = new Fichier();
			fichier.setId(id);
			fichier.setUuid(UUID.randomUUID());
			fichier.setStatus(status);
			fichier.setType(FichierType1.CONTENT1);
			fichier.setStorageUnit(em.find(StorageUnit.class, storageUnit.getId()));
			fichier.setRelativePath(String.format("/relative-path-%d", id));
			fichier.setName("filename");
			fichier.setSize(25l);
			fichier.setChecksum("123");
			fichier.setMimetype("application/octet-stream");
			fichier.setChecksumType(ChecksumType.SHA_256);
			fichier.setCreationDate(LocalDateTime.now());
			em.persist(fichier);
			return fichier;
		});
	}

	private StorageUnit createStorageUnit(EntityManagerFactory entityManagerFactory, long id, StorageUnitType type, StorageUnitStatus status) {
		return doInWriteTransactionEntityManager(entityManagerFactory, (em) -> {
			StorageUnit storageUnit;
			storageUnit = new StorageUnit();
			storageUnit.setId(id);
			storageUnit.setType(type);
			storageUnit.setPath(String.format("/test%d", id));
			storageUnit.setStatus(status);
			storageUnit.setCreationDate(LocalDateTime.now());
			em.persist(storageUnit);
			return storageUnit;
		});
	}

}
