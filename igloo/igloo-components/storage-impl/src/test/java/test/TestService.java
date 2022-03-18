package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
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
import test.model.StorageUnitType;

class TestService extends AbstractTest {

	private static final String FILE_CONTENT = "blabla";
	// checksum from 'echo -n "blabla" | sha256sum'
	private static final String FILE_CHECKSUM_SHA_256 = "ccadd99b16cd3d200c22d6db45d8b6630ef3d936767127347ec8a76ab992c2ea";
	private static final long FILE_SIZE = 6l;
	private IStorageService storageService;
	private TransactionTemplate transactionTemplate;
	private Path tempDir;

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
		this.storageService = new StorageService(entityManagerFactory, Set.<IStorageUnitType>copyOf(EnumSet.allOf(StorageUnitType.class)));
		this.tempDir = tempDir;
		PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute());
		transactionTemplate.execute((t) -> {
			EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
			StorageUnit su = new StorageUnit();
			su.setCreationDate(new Date());
			su.setType(StorageUnitType.TYPE_1);
			su.setStatus(StorageUnitStatus.ALIVE);
			su.setPath(tempDir.toString());
			em.persist(su);
			return su;
		});
	}

	@Test
	void testAdd(EntityManagerFactory entityManagerFactory) {
		Fichier fichier = createFichier(entityManagerFactory, "filename.txt", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
		assertThat(Path.of(tempDir.toString(), fichier.getRelativePath()))
			.as("File must exist").exists()
			.content(StandardCharsets.UTF_8)
			.as("File content must be 'blabla'").isEqualTo(FILE_CONTENT);
		assertThat(fichier.getChecksum()).isEqualTo(FILE_CHECKSUM_SHA_256);
		assertThat(fichier.getSize()).isEqualTo(FILE_SIZE);
		assertThat(fichier.getMimetype()).isEqualTo("text/plain");
	}

	@Test
	void testAddRollback(EntityManagerFactory entityManagerFactory) {
		Runnable action = () -> {
			String fileContent = FILE_CONTENT;
			FichierType1 type = FichierType1.CONTENT1;
			createFichier(entityManagerFactory, "filename", type, fileContent, () -> { throw new IllegalStateException("Triggered rollback"); });
		};

		assertThatCode(action::run).as("Fichier add must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		assertThat(Path.of(tempDir.toString(), "relative-path")).doesNotExist();
	}

	@Test
	void testGet(EntityManagerFactory entityManagerFactory) {
		String fileContent = FILE_CONTENT;
		FichierType1 type = FichierType1.CONTENT1;
		Fichier fichier = createFichier(entityManagerFactory, "filename", type, fileContent, () -> {});

		File file = storageService.getFile(fichier);
		assertThat(file)
			.as("File must exist").exists()
			.content(StandardCharsets.UTF_8)
			.as("File content must be 'blabla'").isEqualTo(FILE_CONTENT);
	}

	@Test
	void testRemove(EntityManagerFactory entityManagerFactory) {
		String fileContent = FILE_CONTENT;
		FichierType1 type = FichierType1.CONTENT1;

		transactionTemplate.executeWithoutResult((t) -> {
			Fichier fichier = createFichier(entityManagerFactory, "filename", type, fileContent, () -> {});
			storageService.removeFichier(fichier);
		});

		assertThat(Path.of(tempDir.toString(), "relative-path")).doesNotExist();
	}

	@Test
	void testRemoveRollback(EntityManagerFactory entityManagerFactory) {
		// TODO
	}

	@Test
	void testInvalidate(EntityManagerFactory entityManagerFactory) {
		String fileContent = FILE_CONTENT;
		FichierType1 type = FichierType1.CONTENT1;

		Fichier fichier = transactionTemplate.execute((t) -> {
			Fichier fichierToInvalidate = createFichier(entityManagerFactory, "filename", type, fileContent, () -> {});
			storageService.invalidateFichier(fichierToInvalidate);
			return fichierToInvalidate;
		});

		assertThat(fichier.getStatus()).isEqualTo(FichierStatus.INVALIDATED);
	}

	private Fichier createFichier(EntityManagerFactory entityManagerFactory, String filename, IFichierType fichierType, String fileContent, Runnable postCreationAction) {
		return transactionTemplate.execute((t) -> {
			try (InputStream bais = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8))) {
				return storageService.addFichier(filename, fichierType, bais);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} finally {
				postCreationAction.run();
			}
		});
	}

	private DefaultTransactionAttribute writeTransactionAttribute() {
		DefaultTransactionAttribute ta = new DefaultTransactionAttribute();
		ta.setReadOnly(false);
		return ta;
	}
}
