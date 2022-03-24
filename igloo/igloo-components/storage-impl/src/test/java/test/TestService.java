package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.FichierStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import test.model.FichierType1;

class TestService extends AbstractTest {

	@Override
	@BeforeEach
	void initStorageUnit(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
		super.initStorageUnit(entityManagerFactory, tempDir);
	}

	@Test
	void testAdd() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		doAnswer(AdditionalAnswers.<InputStream, Path>answerVoid((a, b) -> { IOUtils.copy(a, baos); }))
			.when(operations).copy(any(), any());
		Fichier fichier = createFichier("filename.txt", FichierType1.CONTENT1, FILE_CONTENT, () -> {});

		verify(operations).copy(any(), Mockito.eq(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath())));
		verify(operations, times(1)).copy(any(), any());
		verifyNoMoreInteractions(operations);

		assertThat(baos.toByteArray()).isEqualTo(FILE_CONTENT.getBytes(StandardCharsets.UTF_8)).hasSize((int) FILE_SIZE);
		assertThat(fichier.getChecksum()).isEqualTo(FILE_CHECKSUM_SHA_256);
		assertThat(fichier.getSize()).isEqualTo(FILE_SIZE);
		assertThat(fichier.getMimetype()).isEqualTo("text/plain");
	}

	@Test
	void testAddRollback() throws IOException {
		Runnable action = () -> {
			createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> { throw new IllegalStateException("Triggered rollback"); });
		};

		assertThatCode(action::run).as("Fichier add must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		verify(operations, times(1)).copy(any(), any());
		verify(operations, times(1)).removePhysicalFile(any(), any());
		verifyNoMoreInteractions(operations);
	}

	@Test
	void testGet() throws IOException {
		Fichier fichier = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
		Mockito.reset(operations); // Get rid of creation operation

		storageService.getFile(fichier);
		verify(operations, times(1)).getFile(any());
		verifyNoMoreInteractions(operations);

		verify(operations).getFile(Mockito.eq(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath())));
	}

	@Test
	void testRemove() {
		transactionTemplate.executeWithoutResult((t) -> {
			Fichier fichier = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
			Mockito.reset(operations); // Get rid of creation operation
			storageService.removeFichier(fichier);
		});

		verify(operations, times(1)).removePhysicalFile(any(), any());
		verifyNoMoreInteractions(operations);
	}

	@Test
	void testRemoveRollback(EntityManagerFactory entityManagerFactory) throws IOException {
		Runnable action = () -> {
			Fichier fichier = transactionTemplate.execute((t) -> createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {}));
			Mockito.reset(operations);

			transactionTemplate.executeWithoutResult((t) -> {
				EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
				storageService.removeFichier(em.find(Fichier.class, fichier.getId()));
				throw new IllegalStateException("Triggered rollback");
			});
		};

		assertThatCode(action::run).as("Fichier remove must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		verifyNoInteractions(operations); // method removePhysicalFile should not be called
	}

	@Test
	void testInvalidate() {
		Fichier fichier = transactionTemplate.execute((t) -> {
			Fichier fichierToInvalidate = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {});
			Mockito.reset(operations); // Get rid of creation operation
			storageService.invalidateFichier(fichierToInvalidate);
			return fichierToInvalidate;
		});

		assertThat(fichier).isNotNull();
		assertThat(fichier.getStatus()).isEqualTo(FichierStatus.INVALIDATED);
		Mockito.verifyNoInteractions(operations);
	}

}
