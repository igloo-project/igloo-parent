package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.io.IOUtils;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.FichierStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import test.model.FichierType1;

class TestService extends AbstractTest {

	@RegisterExtension
	EntityManagerFactoryExtension extension = AbstractTest.initEntityManagerExtension();

	protected StorageOperations storageOperations = mock(StorageOperations.class);

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory) {
		Path fakeRootPath = Path.of("/fakepath");
		super.init(entityManagerFactory, fakeRootPath, storageOperations, new DatabaseOperations(entityManagerFactory, "fichier_id_seq", "storageunit_id_seq"));
		super.initStorageUnit(entityManagerFactory);
	}

	@Test
	void testAdd() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		doAnswer(AdditionalAnswers.<InputStream, Path>answerVoid((a, b) -> { IOUtils.copy(a, baos); }))
			.when(storageOperations).copy(any(), any());
		Fichier fichier = createFichier("filename.txt", FichierType1.CONTENT1, FILE_CONTENT, () -> {});

		verify(storageOperations).copy(any(), Mockito.eq(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath())));
		verify(storageOperations, times(1)).copy(any(), any());
		verifyNoMoreInteractions(storageOperations);

		assertThat(baos.toByteArray()).isEqualTo(FILE_CONTENT.getBytes(StandardCharsets.UTF_8)).hasSize((int) FILE_SIZE);
		assertThat(fichier.getChecksum()).isEqualTo(FILE_CHECKSUM_SHA_256);
		assertThat(fichier.getSize()).isEqualTo(FILE_SIZE);
		assertThat(fichier.getMimetype()).isEqualTo("text/plain");
		assertThat(fichier.getCreationDate()).isNotNull();
		assertThat(fichier.getValidationDate()).isNull();
		assertThat(fichier.getInvalidationDate()).isNull();
	}

	@Test
	void testAddRollback() throws IOException {
		Runnable action = () -> {
			createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> { throw new IllegalStateException("Triggered rollback"); });
		};

		assertThatCode(action::run).as("Fichier add must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		verify(storageOperations, times(1)).copy(any(), any());
		verify(storageOperations, times(1)).removePhysicalFile(notNull(), notNull(), notNull());
		verifyNoMoreInteractions(storageOperations);
	}

	@Test
	void testGet() throws IOException {
		Fichier fichier = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
		Mockito.reset(storageOperations); // Get rid of creation operation

		storageService.getFile(fichier);
		verify(storageOperations, times(1)).getFile(any());
		verifyNoMoreInteractions(storageOperations);

		verify(storageOperations).getFile(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()));
	}

	@Test
	void testRemove() {
		transactionTemplate.executeWithoutResult((t) -> {
			Fichier fichier = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
			Mockito.reset(storageOperations); // Get rid of creation operation
			storageService.removeFichier(fichier);
		});

		verify(storageOperations, times(1)).removePhysicalFile(notNull(), notNull(), notNull());
		verifyNoMoreInteractions(storageOperations);
	}

	@Test
	void testRemoveRollback(EntityManagerFactory entityManagerFactory) throws IOException {
		Runnable action = () -> {
			Fichier fichier = transactionTemplate.execute((t) -> createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {}));
			Mockito.reset(storageOperations);

			transactionTemplate.executeWithoutResult((t) -> {
				EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
				storageService.removeFichier(em.find(Fichier.class, fichier.getId()));
				throw new IllegalStateException("Triggered rollback");
			});
		};

		assertThatCode(action::run).as("Fichier remove must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		verifyNoInteractions(storageOperations); // method removePhysicalFile should not be called
	}

	@Test
	void testValidate() {
		Fichier fichier = transactionTemplate.execute((t) -> {
			Fichier fichierToInvalidate = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
			Mockito.reset(storageOperations); // Get rid of creation operation
			storageService.validateFichier(fichierToInvalidate);
			return fichierToInvalidate;
		});

		assertThat(fichier).isNotNull();
		assertThat(fichier.getStatus()).isEqualTo(FichierStatus.ALIVE);
		assertThat(fichier.getValidationDate()).isNotNull();
		Mockito.verifyNoInteractions(storageOperations);
	}

	@Test
	void testInvalidate() {
		Fichier fichier = transactionTemplate.execute((t) -> {
			Fichier fichierToInvalidate = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
			storageService.validateFichier(fichierToInvalidate);
			Mockito.reset(storageOperations); // Get rid of creation/validation operation
			storageService.invalidateFichier(fichierToInvalidate);
			return fichierToInvalidate;
		});

		assertThat(fichier).isNotNull();
		assertThat(fichier.getStatus()).isEqualTo(FichierStatus.INVALIDATED);
		assertThat(fichier.getInvalidationDate()).isNotNull();
		Mockito.verifyNoInteractions(storageOperations);
	}

}
