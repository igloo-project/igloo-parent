package test;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.IFichierType;
import org.igloo.storage.model.StorageUnit;
import org.junit.jupiter.api.Test;

import test.model.FichierType1;
import test.model.FichierType2;

class TestEntities extends AbstractTest {

	/**
	 * Check that hibernate maps correctly {@link IFichierType}
	 */
	@Test
	void testFichierTypePersist(EntityManager entityManager, EntityTransaction transaction) {
		StorageUnit storageUnit = new StorageUnit();
		Fichier fichier1 = new Fichier();
		fichier1.setStorageUnit(storageUnit);
		fichier1.setFichierType(FichierType1.CONTENT1);
		Fichier fichier2 = new Fichier();
		fichier2.setStorageUnit(storageUnit);
		fichier2.setFichierType(FichierType2.CONTENT3);
		entityManager.persist(storageUnit);
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
}
