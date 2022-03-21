package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.io.IOUtils;
import org.igloo.storage.api.IStorageService;
import org.igloo.storage.impl.StorageEvent;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageService;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistency;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.AdditionalAnswers;
import org.mockito.Answers;
import org.mockito.Mockito;
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
	private static final long FILE_SIZE = 6L;
	private IStorageService storageService;
	private TransactionTemplate transactionTemplate;
	private final StorageOperations operations = mock(StorageOperations.class);
	private Path tempDir;

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
		this.storageService = new StorageService(entityManagerFactory, Set.<IStorageUnitType>copyOf(EnumSet.allOf(StorageUnitType.class)), operations, () -> tempDir);
		this.tempDir = tempDir;
		PlatformTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute());
		transactionTemplate.executeWithoutResult((t) -> storageService.createStorageUnit(StorageUnitType.TYPE_1));
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

	// TODO MPI : à vérifier par LAL
	@Test
	void testRemoveRollback(EntityManagerFactory entityManagerFactory) throws IOException {
		Runnable action = () -> {
			Fichier fichier = transactionTemplate.execute((t) -> createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT, () -> {}));

			transactionTemplate.executeWithoutResult((t) -> {
				EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
				storageService.removeFichier(em.find(Fichier.class, fichier.getId()));
				throw new IllegalStateException("Triggered rollback");
			});
		};

		assertThatCode(action::run).as("Fichier remove must throw an exception").isInstanceOf(IllegalStateException.class).hasMessageContaining("Triggered rollback");
		verify(operations, times(1)).copy(any(), any());
		verifyNoMoreInteractions(operations); // method removePhysicalFile should not be called
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

	@Test
	void testConsistency() throws IOException {
		Mockito.doCallRealMethod().when(operations).copy(any(), any());
		Mockito.doCallRealMethod().when(operations).getFile(any());
		Fichier fichier = transactionTemplate.execute((t) -> createFichier("filename.txt", FichierType1.CONTENT1, FILE_CONTENT, () -> {}));

		StorageConsistency bean = transactionTemplate.execute((t) -> storageService.checkConsistency(fichier.getStorageUnit()));

		assertThat(bean.getFsFileCount()).isEqualTo(1);
	}

	private Fichier createFichier(String filename, IFichierType fichierType, String fileContent, Runnable postCreationAction) {
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
