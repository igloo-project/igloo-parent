package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.apache.commons.io.IOUtils;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.StorageUnitStatus;
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

  //	protected DatabseOperations databaseOperations = mock(DatabaseOperations.class);

  @BeforeEach
  void init(EntityManagerFactory entityManagerFactory) {
    Path fakeRootPath = Path.of("/fakepath");
    super.init(
        entityManagerFactory,
        fakeRootPath,
        storageOperations,
        new DatabaseOperations(entityManagerFactory, "fichier_id_seq", "storageunit_id_seq"));
    super.initStorageUnit(entityManagerFactory);
  }

  @Test
  void testAdd_FilenameNull() {
    String filename = null;
    FichierType1 type = FichierType1.CONTENT1;
    InputStream inputStream =
        new ByteArrayInputStream(FILE_CONTENT.getBytes(StandardCharsets.UTF_8));

    assertThatNullPointerException()
        .isThrownBy(() -> storageService.addFichier(filename, type, inputStream, null));
  }

  @Test
  void testAdd_FichierTypeNull() {
    String filename = FILENAME;
    FichierType1 type = null;
    InputStream inputStream =
        new ByteArrayInputStream(FILE_CONTENT.getBytes(StandardCharsets.UTF_8));

    assertThatNullPointerException()
        .isThrownBy(() -> storageService.addFichier(filename, type, inputStream, null));
  }

  @Test
  void testAdd_InputStreamNull() {
    String filename = FILENAME;
    FichierType1 type = FichierType1.CONTENT1;
    InputStream inputStream = null;

    assertThatNullPointerException()
        .isThrownBy(() -> storageService.addFichier(filename, type, inputStream, null));
  }

  @Test
  void testAdd_ok() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    doAnswer(
            AdditionalAnswers.<InputStream, Path>answerVoid(
                (a, b) -> {
                  IOUtils.copy(a, baos);
                }))
        .when(storageOperations)
        .copy(any(), any());

    Fichier fichier = createFichier(FILENAME, FichierType1.CONTENT1, FILE_CONTENT);

    verify(storageOperations, times(1)).copy(any(), any());
    verify(storageOperations, times(1))
        .copy(
            any(),
            Mockito.eq(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath())));
    verifyNoMoreInteractions(storageOperations);

    assertThat(fichier).isNotNull();
    assertThat(baos.toByteArray())
        .isEqualTo(FILE_CONTENT.getBytes(StandardCharsets.UTF_8))
        .hasSize((int) FILE_SIZE);
    assertThat(fichier.getChecksum()).isEqualTo(FILE_CHECKSUM_SHA_256);
    assertThat(fichier.getSize()).isEqualTo(FILE_SIZE);
    assertThat(fichier.getMimetype()).isEqualTo("text/plain");
    assertThat(fichier.getCreationDate()).isNotNull();
    assertThat(fichier.getValidationDate()).isNull();
    assertThat(fichier.getInvalidationDate()).isNull();
  }

  @Test
  void testAdd_Rollback() throws IOException {
    Runnable action =
        () -> {
          createFichier(
              "filename",
              FichierType1.CONTENT1,
              FILE_CONTENT,
              (f) -> {
                throw new IllegalStateException("Triggered rollback");
              });
        };

    assertThatCode(action::run)
        .as("Fichier add must throw an exception")
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Triggered rollback");
    verify(storageOperations, times(1)).copy(any(), any());
    verify(storageOperations, times(1)).removePhysicalFile(notNull(), notNull(), notNull());
    verifyNoMoreInteractions(storageOperations);
  }

  @Test
  void testGetFile() throws IOException {
    Fichier fichier = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT);
    Mockito.reset(storageOperations); // Get rid of creation operation

    storageService.getFile(fichier);
    verify(storageOperations, times(1)).getFile(any(), eq(true));
    verifyNoMoreInteractions(storageOperations);

    verify(storageOperations)
        .getFile(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()), true);
  }

  @Test
  void testGetFile_Invalidated() throws IOException {
    Fichier fichier =
        createFichier(
            "filename",
            FichierType1.CONTENT1,
            FILE_CONTENT,
            (f) -> f.setStatus(FichierStatus.INVALIDATED));
    Mockito.reset(storageOperations); // Get rid of creation operation

    // check enabled - exception thrown
    assertThatCode(() -> storageService.getFile(fichier)).isInstanceOf(FileNotFoundException.class);
    verify(storageOperations, never()).getFile(any(), anyBoolean());

    // check disabled - file can be retrieved
    storageService.getFile(fichier, false, true);
    verify(storageOperations, times(1)).getFile(any(), eq(true));
    verifyNoMoreInteractions(storageOperations);

    verify(storageOperations)
        .getFile(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()), true);
  }

  @Test
  void testGetFile_Transient() throws IOException {
    Fichier fichier =
        createFichier(
            "filename",
            FichierType1.CONTENT1,
            FILE_CONTENT,
            (f) -> f.setStatus(FichierStatus.TRANSIENT));
    Mockito.reset(storageOperations); // Get rid of creation operation

    storageService.getFile(fichier);
    verify(storageOperations, times(1)).getFile(any(), eq(true));
    verifyNoMoreInteractions(storageOperations);

    verify(storageOperations)
        .getFile(Path.of(fichier.getStorageUnit().getPath(), fichier.getRelativePath()), true);
  }

  @Test
  void testRemoveFichier() {
    Fichier fichierToRemove =
        transactionTemplate.execute(
            (t) -> {
              Fichier fichier = createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT);
              Mockito.reset(storageOperations); // Get rid of creation operation
              return fichier;
            });
    // storageService reload an attached instance for remove, so we can pass detached instance
    transactionTemplate.execute(
        t -> {
          storageService.removeFichier(fichierToRemove);
          return null;
        });
    // check physical removal is called
    verify(storageOperations, times(1))
        .removePhysicalFile(
            Mockito.any(), // log message
            Mockito.eq(fichierToRemove.getId()), // id
            argThat(
                (a) -> {
                  assertThat(a).toString().contains(fichierToRemove.getRelativePath());
                }) // path
            );

    verifyNoMoreInteractions(storageOperations);
  }

  @Test
  void testRemoveFichier_Rollback(EntityManagerFactory entityManagerFactory) throws IOException {
    Runnable action =
        () -> {
          Fichier fichier =
              transactionTemplate.execute(
                  (t) -> createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT));
          Mockito.reset(storageOperations);

          transactionTemplate.executeWithoutResult(
              (t) -> {
                EntityManager em =
                    EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
                storageService.removeFichier(em.find(Fichier.class, fichier.getId()));
                throw new IllegalStateException("Triggered rollback");
              });
        };

    assertThatCode(action::run)
        .as("Fichier remove must throw an exception")
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Triggered rollback");
    verifyNoInteractions(storageOperations); // method removePhysicalFile should not be called
  }

  @Test
  void testGetFichierById_IdNull() {
    Long id = null;

    assertThatNullPointerException().isThrownBy(() -> storageService.getFichierById(id));
  }

  @Test
  void testValidateFichier() {
    Fichier fichier =
        transactionTemplate.execute(
            (t) -> {
              Fichier fichierToInvalidate =
                  createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT);
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
  void testValidateFichier_AlreadyAlive() {
    Fichier fichier =
        transactionTemplate.execute(
            (t) -> {
              Fichier fichierToInvalidate =
                  createFichier(
                      "filename",
                      FichierType1.CONTENT1,
                      FILE_CONTENT,
                      (f) -> f.setStatus(FichierStatus.ALIVE));
              Mockito.reset(storageOperations); // Get rid of creation operation
              storageService.validateFichier(fichierToInvalidate);
              return fichierToInvalidate;
            });

    assertThat(fichier).isNotNull();
    assertThat(fichier.getStatus()).isEqualTo(FichierStatus.ALIVE);
    assertThat(fichier.getValidationDate()).isNull(); // We did not initialized it
    Mockito.verifyNoInteractions(storageOperations);
  }

  @Test
  void testInvalidateFichier() {
    Fichier fichier =
        transactionTemplate.execute(
            (t) -> {
              Fichier fichierToInvalidate =
                  createFichier("filename", FichierType1.CONTENT1, FILE_CONTENT);
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

  @Test
  void testInvalidateFichier_AlreadyInvalided() {
    Fichier fichier =
        transactionTemplate.execute(
            (t) -> {
              Fichier fichierToInvalidate =
                  createFichier(
                      "filename",
                      FichierType1.CONTENT1,
                      FILE_CONTENT,
                      (f) -> f.setStatus(FichierStatus.INVALIDATED));
              storageService.validateFichier(fichierToInvalidate);
              Mockito.reset(storageOperations); // Get rid of creation/validation operation
              storageService.invalidateFichier(fichierToInvalidate);
              return fichierToInvalidate;
            });

    assertThat(fichier).isNotNull();
    assertThat(fichier.getStatus()).isEqualTo(FichierStatus.INVALIDATED);
    assertThat(fichier.getInvalidationDate()).isNull(); // We did not initialized it
    Mockito.verifyNoInteractions(storageOperations);
  }

  @Test
  void testSplitStorageUnit(EntityManagerFactory entityManagerFactory) {
    StorageUnit original =
        transactionTemplate.execute(
            (t) -> {
              EntityManager em =
                  EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
              // created by init
              return em.find(StorageUnit.class, 1l);
            });
    StorageUnit newUnit =
        transactionTemplate.execute((t) -> storageService.splitStorageUnit(original));
    StorageUnit updated =
        transactionTemplate.execute(
            (t) -> {
              EntityManager em =
                  EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
              // created by init
              return em.find(StorageUnit.class, 1l);
            });
    assertThat(updated.getStatus()).isEqualTo(StorageUnitStatus.ARCHIVED);
    assertThat(newUnit.getStatus()).isEqualTo(StorageUnitStatus.ALIVE);
    assertThat(newUnit.getCreationDate()).isAfter(original.getCreationDate());
    assertThat(newUnit.getId()).isGreaterThan(original.getId());
  }
}
