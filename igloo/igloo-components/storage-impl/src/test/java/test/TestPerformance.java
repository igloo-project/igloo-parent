package test;

import static org.assertj.core.api.Assertions.assertThat;

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
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.assertj.core.api.Assumptions;
import org.assertj.core.api.Condition;
import org.igloo.jpa.test.EntityManagerFactoryExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageFailure;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import com.google.common.base.Stopwatch;

import test.model.FichierType1;

/**
 * This test needs environment <code>PERFORMANCE=true</code> to be launched.
 */
class TestPerformance extends AbstractTest {

	@RegisterExtension
	EntityManagerFactoryExtension extension = AbstractTest.initEntityManagerExtension();
	private StorageOperations storageOperations;
	private DatabaseOperations databaseOperations;
	private StorageUnit unit;

	@BeforeEach
	void init(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
		storageOperations = new StorageOperations();
		databaseOperations = new DatabaseOperations(entityManagerFactory, "fichier_id_seq", "storageunit_id_seq");
		super.init(entityManagerFactory, tempDir, storageOperations, databaseOperations);
		unit = super.initStorageUnit(entityManagerFactory);
	}

	@Test
	void testPerformance(EntityManagerFactory entityManagerFactory) {
		Assumptions.assumeThat(Boolean.parseBoolean(System.getenv("PERFORMANCE"))).as("Set PERFORMANCE=true to launch performance test").isTrue();
		// override logging
		Configurator.setLevel("org.igloo.storage", Level.WARN);
		Random random = new Random(123456l);
		RandomStringGenerator filenameGenerator = new RandomStringGenerator.Builder().usingRandom(random::nextInt).filteredBy(CharacterPredicates.ASCII_ALPHA_NUMERALS).build();
		RandomStringGenerator contentGenerator = new RandomStringGenerator.Builder().usingRandom(random::nextInt).build();
		String[] extensions = new String[] {".pdf", ".doc", ".docx", ".xlsx", ".txt", ""};
		Supplier<String> extensionGenerator = () -> {
			return extensions[random.nextInt(extensions.length)];
		};
		int fileCount = 10000;
		Stopwatch creationWatch = Stopwatch.createStarted();
		doInWriteTransaction(entityManagerFactory, () -> {
			for (int i = 0; i < fileCount; i++) {
				String filename = filenameGenerator.generate(12) + extensionGenerator.get();
				String content = contentGenerator.generate(100, 1000);
				storageService.addFichier(filename, FichierType1.CONTENT1, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
			}
			return null;
		});
		creationWatch.stop();
		LOGGER.info("{} files created in {} seconds", fileCount, creationWatch.elapsed().toSeconds());
		Set<Fichier> fichiers = doInReadTransaction(entityManagerFactory, () -> databaseOperations.listUnitAliveFichiers(unit));
		LOGGER.info("{} bytes created in {} seconds",
				fichiers.stream().map(Fichier::getSize).reduce((a, b) -> a + b).get(),
				creationWatch.elapsed().toSeconds());
		assertThat(fichiers).hasSize(fileCount);
		assertThat(fichiers.stream().map(Fichier::getStorageUnit).distinct()).hasSize(1);
		assertThat(storageOperations.listUnitContent(fichiers.iterator().next().getStorageUnit())).hasSize(fileCount);
		
		checkConsistency(entityManagerFactory, fileCount, false, (f) -> assertThat(f).isEmpty());
		checkConsistency(entityManagerFactory, fileCount, true, (f) -> assertThat(f).isEmpty());
		
		List<Fichier> removed = new ArrayList<Fichier>(fichiers);
		Collections.shuffle(removed, random);
		// drop 50 files
		// drop 50 entities
		// alter 50 files
		List<Fichier> removedFiles = removed.stream()
				.limit(50)
				.peek(f -> {
					// remove file
					Path absolutePath = Path.of(unit.getPath()).resolve(f.getRelativePath());
					if (!absolutePath.toFile().delete()) {
						throw new IllegalStateException(String.format("Cannot remove %s", f.getRelativePath()));
					}
				})
				.collect(Collectors.toList());
		List<Path> removedEntities = removed.stream()
				.skip(50)
				.limit(50)
				.peek(f -> doInWriteTransaction(entityManagerFactory, () -> {
					// remove fichier
					EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
					em.remove(em.find(Fichier.class, f.getId()));
					return f;
				}))
				.map(f -> Path.of(unit.getPath()).resolve(f.getRelativePath()))
				.collect(Collectors.toList());
		List<Fichier> contentAltered = removed.stream()
				.skip(100)
				.limit(50)
				.peek(f -> {
					// modify content
					Path absolutePath = Path.of(unit.getPath()).resolve(f.getRelativePath());
					try (FileOutputStream fos = new FileOutputStream(absolutePath.toFile())) {
						IOUtils.copy(new ByteArrayInputStream("alteredContent".getBytes(StandardCharsets.UTF_8)), fos);
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				})
				.collect(Collectors.toList());
		
		checkConsistency(entityManagerFactory, fileCount, true,
				(f) -> assertThat(f)
					.hasSize(150)
					// check we have 50 items for each type
					.areExactly(50, new Condition<StorageFailure>(fi -> StorageFailureType.MISSING_ENTITY.equals(fi.getType()) && removedEntities.contains(Path.of(fi.getPath())), "Entity from removedEntities must be found missing"))
					.areExactly(50, new Condition<StorageFailure>(fi -> StorageFailureType.MISSING_FILE.equals(fi.getType()) && removedFiles.contains(fi.getFichier()), "Path from removedFiles must be found missing"))
					.areExactly(50, new Condition<StorageFailure>(fi -> StorageFailureType.CHECKSUM_MISMATCH.equals(fi.getType()) && contentAltered.contains(fi.getFichier()), "Content from content altered must be found mismatching"))
		);
	}

	private void checkConsistency(EntityManagerFactory entityManagerFactory, int fileCount, boolean checksumValidation, Consumer<List<StorageFailure>> failureCheck) {
		Stopwatch checkWatch = Stopwatch.createStarted();
		List<StorageConsistencyCheck> sc = doInWriteTransaction(entityManagerFactory, () -> {
			return storageService.checkConsistency(unit, checksumValidation);
		});
		checkWatch.stop();
		LOGGER.info("{} files checked {} in {} seconds", fileCount, checksumValidation ? "(with checksum)" : "",checkWatch.elapsed().toSeconds());
		assertThat(sc).hasSize(1);
		Supplier<List<StorageFailure>> listStorageFailures = () -> EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory).createQuery("SELECT s FROM StorageFailure s", StorageFailure.class).getResultList();
		List<StorageFailure> failures = doInWriteTransaction(entityManagerFactory, listStorageFailures);
		failureCheck.accept(failures);
	}
}
