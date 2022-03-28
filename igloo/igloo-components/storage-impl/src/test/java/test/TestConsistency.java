package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces;

@Extensions(@ExtendWith(SoftAssertionsExtension.class))
public class TestConsistency extends AbstractTest {

	protected StorageOperations storageOperations = mock(StorageOperations.class);
	protected DatabaseOperations databaseOperations = mock(DatabaseOperations.class);

	@BeforeEach
	void initStorageUnit() {
		super.init(null, null, storageOperations, databaseOperations);
	}

	@Test
	void testConsistency(SoftAssertions softly) throws IOException {
		LocalDateTime testStart = LocalDateTime.now();
		StorageUnit unit = new StorageUnit();
		Path path = Path.of("/my-root-path");
		unit.setPath(path.toString());
		when(databaseOperations.getStorageUnit(unit.getId())).thenReturn(unit);
		when(storageOperations.listUnitContent(unit)).thenReturn(Set.of(
			path.resolve("fichier1"),
			path.resolve("fichier2"),
			path.resolve("fichier3"), // file missing in database
			path.resolve("fichier5"),
			path.resolve("fichier6") // file missing in database
		));
		when(storageOperations.checksum(any()))
			.then(AnswerFunctionalInterfaces.<String, Path>toAnswer(
					p -> p.getFileName().toString()
		));
		when(storageOperations.length(any()))
			.then(AnswerFunctionalInterfaces.<Long, Path>toAnswer(
					p -> (long) p.getFileName().toString().length()
		));
		Fichier fichier1 = new Fichier();
		fichier1.setRelativePath("fichier1");
		fichier1.setChecksum("fichier1");
		fichier1.setSize((long) "fichiern".length());
		Fichier fichier2 = new Fichier();
		fichier2.setRelativePath("fichier2");
		fichier2.setChecksum("fichier2");
		fichier2.setSize((long) "fichiern".length());
		Fichier fichier4 = new Fichier(); // file missing in listing
		fichier4.setRelativePath("fichier4");
		fichier4.setChecksum("fichier4");
		fichier4.setSize((long) "fichiern".length());
		Fichier fichier5 = new Fichier();
		fichier5.setRelativePath("fichier5");
		fichier5.setChecksum("fichier4"); // trigger checksum error
		fichier5.setSize((long) "fichiern".length());
		when(databaseOperations.listUnitAliveFichiers(unit)).thenReturn(Set.of(
			fichier1,
			fichier2,
			fichier4,
			fichier5
		));
		List<StorageConsistencyCheck> beans = storageService.checkConsistency(unit, true);

		// 2 missing entity
		// 1 missing file
		// 1 checksum mismatch
		verify(databaseOperations).createConsistencyCheck(argThat(c -> assertThat(c.getStorageUnit()).isEqualTo(unit)));
		verify(databaseOperations).triggerFailure(argThat(f -> {
			assertThat(f.getType()).isEqualTo(StorageFailureType.MISSING_ENTITY);
			assertThat(f.getFichier()).isNull();
			assertThat(f.getPath()).isEqualTo(path.resolve(Path.of("fichier3")).toString());
		}));
		verify(databaseOperations).triggerFailure(argThat(f -> {
			assertThat(f.getType()).isEqualTo(StorageFailureType.MISSING_ENTITY);
			assertThat(f.getFichier()).isNull();
			assertThat(f.getPath()).isEqualTo(path.resolve(Path.of("fichier6")).toString());
		}));
		verify(databaseOperations).triggerFailure(argThat(f -> {
			assertThat(f.getType()).isEqualTo(StorageFailureType.MISSING_FILE);
			assertThat(f.getFichier()).isEqualTo(fichier4);
			assertThat(f.getPath()).isEqualTo(path.resolve(Path.of("fichier4")).toString());
		}));
		verify(databaseOperations).triggerFailure(argThat(f -> {
			assertThat(f.getType()).isEqualTo(StorageFailureType.CONTENT_MISMATCH);
			assertThat(f.getFichier()).isEqualTo(fichier5);
			assertThat(f.getPath()).isEqualTo(path.resolve(Path.of("fichier5")).toString());
		}));
		assertThat(beans)
			.hasSize(1)
			.satisfies(consistency -> {
				softly.assertThat(consistency.getFsFileCount()).as("File (filesystem) count").isEqualTo(5L);
				softly.assertThat(consistency.getDbFichierCount()).as("Fichier (entity) count").isEqualTo(4L);
				softly.assertThat(consistency.getContentMismatchCount()).isEqualTo(1);
				softly.assertThat(consistency.getMissingFileCount()).isEqualTo(1);
				softly.assertThat(consistency.getMissingFichierCount()).isEqualTo(2);
				softly.assertThat(consistency.getCheckStartedOn()).isBefore(consistency.getCheckFinishedOn());
				softly.assertThat(consistency.getCheckFinishedOn()).isAfterOrEqualTo(testStart).isBeforeOrEqualTo(LocalDateTime.now());
				softly.assertThat(consistency.getDbFichierSize()).isEqualTo(4 * "fichiern".length());
				softly.assertThat(consistency.getFsFileSize()).isEqualTo(5 * "fichiern".length());
			}, atIndex(0));
	}

	@Test
	void testConsistencyNoChecksum(SoftAssertions softly) throws IOException {
		StorageUnit unit = new StorageUnit();
		Path path = Path.of("/my-root-path");
		unit.setPath(path.toString());
		Fichier fichier1 = new Fichier();
		fichier1.setRelativePath("fichier1");
		fichier1.setChecksum(null);
		fichier1.setChecksumType(ChecksumType.NONE);
		
		when(storageOperations.listUnitContent(unit)).thenReturn(Set.of(Path.of("fichier1")));
		when(databaseOperations.listUnitAliveFichiers(unit)).thenReturn(Set.of(fichier1));
		
		storageService.checkConsistency(unit, true);
		
		verify(databaseOperations, never()).triggerFailure(any());
		verify(storageOperations, never()).checksum(any());
	}

}
