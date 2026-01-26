package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import com.google.common.base.Stopwatch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.assertj.core.api.Assumptions;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.Condition;
import org.igloo.jpa.test.SpringEntityManagerExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.TestExecutionListeners;
import test.model.FichierType1;

/** This test needs environment <code>PERFORMANCE=true</code> to be launched. */
@SpringBootTest(classes = TestConfiguration.class)
@ExtendWith(SpringEntityManagerExtension.class)
@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class TestPerformance extends AbstractTest {

  private StorageOperations storageOperations;
  private DatabaseOperations databaseOperations;
  private StorageUnit unit;

  @Autowired private EntityManagerFactory entityManagerFactory;

  @BeforeEach
  void init(@TempDir Path tempDir) {
    storageOperations = new StorageOperations();
    databaseOperations =
        new DatabaseOperations(entityManagerFactory, "fichier_id_seq", "storageunit_id_seq");
    super.init(entityManagerFactory, tempDir, storageOperations, databaseOperations);
    unit = super.initStorageUnit(entityManagerFactory);
  }

  @Test
  void testPerformance() {
    Assumptions.assumeThat(Boolean.parseBoolean(System.getenv("PERFORMANCE")))
        .as("Set PERFORMANCE=true to launch performance test")
        .isTrue();
    // override logging
    Configurator.setLevel("org.igloo.storage", Level.WARN);
    Random random = new Random(123456l);
    RandomStringGenerator filenameGenerator =
        new RandomStringGenerator.Builder()
            .usingRandom(random::nextInt)
            .filteredBy(CharacterPredicates.ASCII_ALPHA_NUMERALS)
            .get();
    RandomStringGenerator contentGenerator =
        new RandomStringGenerator.Builder().usingRandom(random::nextInt).get();
    String[] extensions = new String[] {".pdf", ".doc", ".docx", ".xlsx", ".txt", ""};
    Supplier<String> extensionGenerator =
        () -> {
          return extensions[random.nextInt(extensions.length)];
        };
    int fileCount = 10000;
    Stopwatch creationWatch = Stopwatch.createStarted();
    doInWriteTransaction(
        entityManagerFactory,
        () -> {
          for (int i = 0; i < fileCount; i++) {
            String filename = filenameGenerator.generate(12) + extensionGenerator.get();
            String content = contentGenerator.generate(100, 1000);
            storageService.addFichier(
                filename,
                FichierType1.CONTENT1,
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
          }
          return null;
        });
    creationWatch.stop();
    LOGGER.info("{} files created in {} seconds", fileCount, creationWatch.elapsed().toSeconds());
    Set<Fichier> fichiers =
        doInReadTransaction(
            entityManagerFactory, () -> databaseOperations.listUnitAliveFichiers(unit));
    Long fichierSize = fichiers.stream().map(Fichier::getSize).reduce((a, b) -> a + b).get();
    LOGGER.info("{} bytes created in {} seconds", fichierSize, creationWatch.elapsed().toSeconds());
    assertThat(fichiers).hasSize(fileCount);
    assertThat(fichiers.stream().map(Fichier::getStorageUnit).distinct()).hasSize(1);
    assertThat(storageOperations.listUnitContent(fichiers.iterator().next().getStorageUnit()))
        .hasSize(fileCount);

    Consumer<List<StorageConsistencyCheck>> noErrorAssertion =
        (cs) ->
            assertThat(cs)
                .hasSize(1)
                .satisfies(
                    (c) -> {
                      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
                        softly
                            .assertThat(c.getFsFileCount())
                            .as("File (filesystem) count")
                            .isEqualTo(fileCount);
                        softly
                            .assertThat(c.getDbFichierCount())
                            .as("Fichier (entity) count")
                            .isEqualTo(fileCount);
                        softly.assertThat(c.getSizeMismatchCount()).isEqualTo(0);
                        softly.assertThat(c.getChecksumMismatchCount()).isEqualTo(0);
                        softly.assertThat(c.getMissingFileCount()).isEqualTo(0);
                        softly.assertThat(c.getMissingFichierCount()).isEqualTo(0);
                        softly.assertThat(c.getCheckStartedOn()).isNotNull();
                        softly.assertThat(c.getCheckFinishedOn()).isNotNull();
                        softly.assertThat(c.getDbFichierSize()).isEqualTo(fichierSize);
                        softly.assertThat(c.getFsFileSize()).isEqualTo(fichierSize);
                      }
                    },
                    atIndex(0));
    checkConsistency(
        entityManagerFactory, fileCount, false, noErrorAssertion, (f) -> assertThat(f).isEmpty());
    checkConsistency(
        entityManagerFactory, fileCount, true, noErrorAssertion, (f) -> assertThat(f).isEmpty());

    List<Fichier> removed = new ArrayList<Fichier>(fichiers);
    AtomicLong removedEntitiesSize = new AtomicLong();
    AtomicLong removedFilesSize = new AtomicLong();
    AtomicLong alteredSize = new AtomicLong();
    Collections.shuffle(removed, random);
    // drop 50 files
    // drop 50 entities
    // alter 50 files
    List<Fichier> removedFiles =
        removed.stream()
            .limit(50)
            .peek(
                f -> {
                  // remove file
                  Path absolutePath = Path.of(unit.getPath()).resolve(f.getRelativePath());
                  if (!absolutePath.toFile().delete()) {
                    throw new IllegalStateException(
                        String.format("Cannot remove %s", f.getRelativePath()));
                  }
                  removedFilesSize.addAndGet(-f.getSize());
                })
            .collect(Collectors.toList());
    List<Path> removedEntities =
        removed.stream()
            .skip(50)
            .limit(50)
            .peek(
                f ->
                    doInWriteTransaction(
                        entityManagerFactory,
                        () -> {
                          // remove fichier
                          EntityManager em =
                              EntityManagerFactoryUtils.getTransactionalEntityManager(
                                  entityManagerFactory);
                          em.remove(em.find(Fichier.class, f.getId()));
                          removedEntitiesSize.addAndGet(-f.getSize());
                          return f;
                        }))
            .map(f -> Path.of(unit.getPath()).resolve(f.getRelativePath()))
            .collect(Collectors.toList());
    List<Fichier> contentAltered =
        removed.stream()
            .skip(100)
            .limit(50)
            .peek(
                f -> {
                  // modify content
                  Path absolutePath = Path.of(unit.getPath()).resolve(f.getRelativePath());
                  byte[] newContent = "alteredContent".getBytes(StandardCharsets.UTF_8);
                  try (FileOutputStream fos = new FileOutputStream(absolutePath.toFile())) {
                    IOUtils.copy(new ByteArrayInputStream(newContent), fos);
                  } catch (IOException e) {
                    throw new IllegalStateException(e);
                  }
                  alteredSize.addAndGet(-f.getSize() + newContent.length);
                })
            .collect(Collectors.toList());

    checkConsistency(
        entityManagerFactory,
        fileCount,
        true,
        (cs) ->
            assertThat(cs)
                .hasSize(1)
                .satisfies(
                    (c) -> {
                      try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
                        softly
                            .assertThat(c.getFsFileCount())
                            .as("File (filesystem) count")
                            .isEqualTo(fileCount - 50);
                        softly
                            .assertThat(c.getDbFichierCount())
                            .as("Fichier (entity) count")
                            .isEqualTo(fileCount - 50);
                        softly.assertThat(c.getSizeMismatchCount()).isEqualTo(50);
                        softly.assertThat(c.getChecksumMismatchCount()).isEqualTo(0);
                        softly.assertThat(c.getMissingFileCount()).isEqualTo(50);
                        softly.assertThat(c.getMissingFichierCount()).isEqualTo(50);
                        softly.assertThat(c.getCheckStartedOn()).isNotNull();
                        softly.assertThat(c.getCheckFinishedOn()).isNotNull();
                        // database size: original size minus removed entities
                        softly
                            .assertThat(c.getDbFichierSize())
                            .isEqualTo(fichierSize + removedEntitiesSize.get());
                        // fs size: original size
                        softly
                            .assertThat(c.getFsFileSize())
                            .isEqualTo(fichierSize + removedFilesSize.get() + alteredSize.get());
                      }
                    },
                    atIndex(0)),
        (f) ->
            assertThat(f)
                .hasSize(150)
                // check we have 50 items for each type
                .areExactly(
                    50,
                    new Condition<StorageFailure>(
                        fi ->
                            StorageFailureType.MISSING_ENTITY.equals(fi.getType())
                                && removedEntities.contains(Path.of(fi.getPath())),
                        "Entity from removedEntities must be found missing"))
                .areExactly(
                    50,
                    new Condition<StorageFailure>(
                        fi ->
                            StorageFailureType.MISSING_FILE.equals(fi.getType())
                                && removedFiles.contains(fi.getFichier()),
                        "Path from removedFiles must be found missing"))
                .areExactly(
                    50,
                    new Condition<StorageFailure>(
                        fi ->
                            StorageFailureType.SIZE_MISMATCH.equals(fi.getType())
                                && contentAltered.contains(fi.getFichier()),
                        "Content from content altered must be found mismatching")));
  }

  private void checkConsistency(
      EntityManagerFactory entityManagerFactory,
      int fileCount,
      boolean checksumValidation,
      Consumer<List<StorageConsistencyCheck>> consistencyCheckCheck,
      Consumer<List<StorageFailure>> failureCheck) {
    Stopwatch checkWatch = Stopwatch.createStarted();
    List<StorageConsistencyCheck> sc =
        doInWriteTransaction(
            entityManagerFactory,
            () -> {
              return storageService.checkConsistency(unit, checksumValidation);
            });
    checkWatch.stop();
    consistencyCheckCheck.accept(sc);
    LOGGER.info(
        "{} files checked {} in {} seconds",
        fileCount,
        checksumValidation ? "(with checksum)" : "",
        checkWatch.elapsed().toSeconds());
    assertThat(sc).hasSize(1);
    Supplier<List<StorageFailure>> listStorageFailures =
        () ->
            EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory)
                .createQuery("SELECT s FROM StorageFailure s", StorageFailure.class)
                .getResultList();
    List<StorageFailure> failures = doInWriteTransaction(entityManagerFactory, listStorageFailures);
    failureCheck.accept(failures);
  }
}
