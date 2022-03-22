package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.StorageUnitStatistics;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.junit.jupiter.api.Test;

import test.model.FichierType1;
import test.model.FichierType2;
import test.model.StorageUnitType;

class TestEntities extends AbstractTest {


	/**
	 * Check that hibernate maps correctly {@link IFichierType}
	 */
	@Test
	void testFichierTypePersist(EntityManager entityManager, EntityTransaction transaction) {
		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		StorageUnitStatistics statistics = new StorageUnitStatistics();
		statistics.setStorageUnit(storageUnit);

		storageUnit.setStatistics(statistics);

		Fichier fichier1 = new Fichier();
		fichier1.setId(1l);
		fichier1.setUuid(UUID.randomUUID());
		fichier1.setStatus(FichierStatus.ALIVE);
		fichier1.setFichierType(FichierType1.CONTENT1);
		fichier1.setStorageUnit(storageUnit);
		fichier1.setRelativePath("/relative-path");
		fichier1.setName("filename");
		fichier1.setSize(25);
		fichier1.setChecksum("123");
		fichier1.setMimetype("application/octet-stream");
		fichier1.setChecksumType(ChecksumType.SHA_256);
		fichier1.setCreationDate(LocalDateTime.now());

		Fichier fichier2 = new Fichier();
		fichier2.setId(2l);
		fichier2.setUuid(UUID.randomUUID());
		fichier2.setStatus(FichierStatus.ALIVE);
		fichier2.setFichierType(FichierType2.CONTENT3);
		fichier2.setStorageUnit(storageUnit);
		fichier2.setRelativePath("/relative-path");
		fichier2.setName("filename");
		fichier2.setSize(25);
		fichier2.setChecksum("123");
		fichier2.setMimetype("application/octet-stream");
		fichier2.setChecksumType(ChecksumType.SHA_256);
		fichier2.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.persist(statistics);
		entityManager.persist(fichier1);
		entityManager.persist(fichier2);
		entityManager.flush();
		entityManager.clear();

		assertThat(entityManager.find(Fichier.class, fichier1.getId()).getFichierType()).isEqualTo(FichierType1.CONTENT1);
		assertThat(entityManager.find(Fichier.class, fichier2.getId()).getFichierType()).isEqualTo(FichierType2.CONTENT3);

		Session session = entityManager.unwrap(Session.class);
		session.doWork(c -> {
			try (PreparedStatement s = c.prepareStatement("select id, fichiertype from fichier order by id")) {
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
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		StorageUnitStatistics statistics = new StorageUnitStatistics();
		statistics.setStorageUnit(storageUnit);

		storageUnit.setStatistics(statistics);

		Fichier fichier = new Fichier();
		fichier.setId(1l);
		fichier.setUuid(UUID.randomUUID());
		fichier.setStatus(FichierStatus.ALIVE);
		fichier.setFichierType(FichierType1.CONTENT1);
		fichier.setStorageUnit(storageUnit);
		fichier.setRelativePath("/relative-path");
		fichier.setName("filename");
		fichier.setSize(25);
		fichier.setChecksum("123");
		fichier.setMimetype("application/octet-stream");
		fichier.setChecksumType(ChecksumType.SHA_256);
		fichier.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.persist(statistics);
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
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		StorageUnitStatistics statistics = new StorageUnitStatistics();
		statistics.setStorageUnit(storageUnit);

		storageUnit.setStatistics(statistics);

		UUID uuid = UUID.randomUUID();
		Fichier fichier1 = new Fichier();
		fichier1.setId(1l);
		fichier1.setUuid(uuid);
		fichier1.setStatus(FichierStatus.ALIVE);
		fichier1.setFichierType(FichierType1.CONTENT1);
		fichier1.setStorageUnit(storageUnit);
		fichier1.setRelativePath("/relative-path");
		fichier1.setName("filename");
		fichier1.setSize(25);
		fichier1.setChecksum("123");
		fichier1.setMimetype("application/octet-stream");
		fichier1.setChecksumType(ChecksumType.SHA_256);
		fichier1.setCreationDate(LocalDateTime.now());

		Fichier fichier2 = new Fichier();
		fichier2.setId(2l);
		fichier2.setUuid(uuid);
		fichier2.setStatus(FichierStatus.ALIVE);
		fichier2.setFichierType(FichierType1.CONTENT1);
		fichier2.setStorageUnit(storageUnit);
		fichier2.setRelativePath("/relative-path");
		fichier2.setName("filename");
		fichier2.setSize(25);
		fichier2.setChecksum("123");
		fichier2.setMimetype("application/octet-stream");
		fichier2.setChecksumType(ChecksumType.SHA_256);
		fichier2.setCreationDate(LocalDateTime.now());

		entityManager.persist(storageUnit);
		entityManager.persist(statistics);
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
		storageUnit.setType(StorageUnitType.TYPE_1);
		storageUnit.setPath("/test");
		storageUnit.setStatus(StorageUnitStatus.ALIVE);
		storageUnit.setCreationDate(LocalDateTime.now());

		StorageUnitStatistics statistics = new StorageUnitStatistics();
		statistics.setStorageUnit(storageUnit);

		storageUnit.setStatistics(statistics);

		entityManager.persist(storageUnit);
		entityManager.persist(statistics);
		entityManager.flush();
		entityManager.clear();

		assertThat(entityManager.find(StorageUnit.class, storageUnit.getId())).isNotNull();
		assertThat(entityManager.find(StorageUnit.class, storageUnit.getId()).getStatistics()).isNotNull();
		assertThat(entityManager.find(StorageUnit.class, storageUnit.getId()).getStatistics().getId()).isEqualTo(statistics.getId());
	}

}
