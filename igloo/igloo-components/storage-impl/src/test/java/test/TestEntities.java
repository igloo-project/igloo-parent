package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import org.junit.jupiter.api.io.TempDir;

import test.model.FichierType1;
import test.model.FichierType2;
import test.model.StorageUnitType;

class TestEntities extends AbstractTest {

	@RegisterExtension
	EntityManagerFactoryExtension extension = AbstractTest.initEntityManagerExtension();

	private DatabaseOperations databaseOperations;

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
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
		LocalDateTime now = LocalDateTime.now();
		StorageUnit unit1 = createStorageUnit(entityManagerFactory, 1, StorageUnitType.TYPE_1, StorageUnitStatus.ALIVE);
		createStorageUnit(entityManagerFactory, 2, StorageUnitType.TYPE_2, StorageUnitStatus.ALIVE);
		StorageConsistencyCheck check = createConsistencyCheck(entityManagerFactory, unit1, StorageUnitCheckType.LISTING_SIZE);
		Fichier fichier1 = createFichier(entityManagerFactory, unit1, 1, FichierStatus.ALIVE);
		StorageFailure failure = StorageFailure.ofMissingFile(Path.of("test"), fichier1, check);
		doInWriteTransaction(entityManagerFactory, () -> {
			databaseOperations.triggerFailure(failure);
			return null;
		});
		doInReadTransactionEntityManager(entityManagerFactory, (em) -> {
			List<StorageFailure> resultList = em.createQuery("SELECT f FROM StorageFailure f", StorageFailure.class).getResultList();
			assertThat(resultList)
				.hasSize(1)
				.satisfies(i -> {
					assertThat(i.getAcknowledgedOn()).isNull();
					assertThat(i.getFixedOn()).isNull();
					assertThat(i.getType()).isEqualTo(StorageFailureType.MISSING_FILE);
					assertThat(i.getStatus()).isEqualTo(StorageFailureStatus.ALIVE);
					assertThat(i.getPath()).isEqualTo("test");
					assertThat(i.getLastFailureOn()).isAfter(now);
					assertThat(i.getCreationTime()).isNotNull();
					assertThat(i.getConsistencyCheck()).isEqualTo(check);
					assertThat(i.getFichier()).isEqualTo(fichier1);
				}, Assertions.atIndex(0));
			return resultList.get(0);
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
