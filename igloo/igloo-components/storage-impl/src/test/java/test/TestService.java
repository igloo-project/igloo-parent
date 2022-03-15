package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import test.model.FichierType1;

class TestService extends AbstractTest {

	private IStorageService storageService = new StorageService();
	private TransactionTemplate transactionTemplate;
	private Path tempDir;

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
		this.tempDir = tempDir;
		PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute());
		transactionTemplate.execute((t) -> {
			EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
			StorageUnit su = new StorageUnit();
			su.setCreationDate(new Date());
			su.setStatus(StorageUnitStatus.ALIVE);
			su.setPath(tempDir.toString());
			su.setPathStrategy("TODO");
			em.persist(su);
			return su;
		});
	}

	@Test
	void testAdd(EntityManagerFactory entityManagerFactory) {
		Fichier fichier = transactionTemplate.execute((t) -> {
			EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
			try (InputStream bais = new ByteArrayInputStream("blabla".getBytes(StandardCharsets.UTF_8))) {
				return storageService.addFichier(em, FichierType1.CONTENT1, bais);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		});
		assertThat(Path.of(tempDir.toString(), fichier.getRelativePath()))
			.exists().content(StandardCharsets.UTF_8).isEqualTo("blabla");
	}

	@Test
	void testAddRollback(EntityManagerFactory entityManagerFactory) {
		Runnable action = () -> {
			transactionTemplate.execute((t) -> {
				EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
				try (InputStream bais = new ByteArrayInputStream("blabla".getBytes(StandardCharsets.UTF_8))) {
					storageService.addFichier(em, FichierType1.CONTENT1, bais);
					throw new IllegalStateException("Triggered rollback");
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			});
		};
		assertThatCode(action::run).as("Fichier add must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		assertThat(Path.of(tempDir.toString(), "relative-path")).doesNotExist();
	}

	private DefaultTransactionAttribute writeTransactionAttribute() {
		DefaultTransactionAttribute ta = new DefaultTransactionAttribute();
		ta.setReadOnly(false);
		return ta;
	}
}
