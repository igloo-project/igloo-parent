package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageHousekeepingService;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.StorageConsistencyCheckResult;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.answers.AnswerFunctionalInterfaces;

@Extensions(@ExtendWith(SoftAssertionsExtension.class))
class TestConsistency extends AbstractTest {

	protected StorageOperations storageOperations = mock(StorageOperations.class);
	protected DatabaseOperations databaseOperations = mock(DatabaseOperations.class);

	@BeforeEach
	void initStorageUnit() {
		super.init(null, null, storageOperations, databaseOperations);
	}

	@Test
	void testConsistencyNoFailure(SoftAssertions softly) throws IOException {
		LocalDateTime testStart = LocalDateTime.now();
		StorageUnit unit = new StorageUnit();
		Path path = Path.of("/my-root-path");
		unit.setPath(path.toString());
		when(databaseOperations.getStorageUnit(unit.getId())).thenReturn(unit);
		when(storageOperations.listUnitContent(unit)).thenReturn(Set.of(
			path.resolve("fichier1"),
			path.resolve("fichier2"),
			path.resolve("fichier3"),
			path.resolve("fichier4"),
			path.resolve("fichier5"),
			path.resolve("fichier6")
		));
		when(storageOperations.checksum(any()))
			.then(AnswerFunctionalInterfaces.<String, Path>toAnswer(
					p -> p.getFileName().toString()
		));
		when(storageOperations.length(any()))
			.then(AnswerFunctionalInterfaces.<Long, Path>toAnswer(
					p -> (long) p.getFileName().toString().length()
		));
		Fichier fichier1 = createFichier("fichier1", "fichier1");
		Fichier fichier2 = createFichier("fichier2", "fichier2");
		Fichier fichier3 = createFichier("fichier3", "fichier3");
		Fichier fichier4 = createFichier("fichier4", "fichier4");
		Fichier fichier5 = createFichier("fichier5", "fichier5");
		Fichier fichier6 = createFichier("fichier6", "fichier6");
		when(databaseOperations.listUnitAliveFichiers(unit)).thenReturn(Set.of(
			fichier1,
			fichier2,
			fichier3,
			fichier4,
			fichier5,
			fichier6
		));
		List<StorageConsistencyCheck> beans = storageService.checkConsistency(unit, true);

		verify(databaseOperations).createConsistencyCheck(argThat(c -> assertThat(c.getStorageUnit()).isEqualTo(unit)));
		verify(databaseOperations, never()).triggerFailure(any());
		assertThat(beans)
			.hasSize(1)
			.satisfies(consistency -> {
				softly.assertThat(consistency.getStatus()).as("Check status").isEqualTo(StorageConsistencyCheckResult.OK);
				softly.assertThat(consistency.getCheckType()).as("Check type").isEqualTo(StorageUnitCheckType.LISTING_SIZE_CHECKSUM);
				softly.assertThat(consistency.getFsFileCount()).as("File (filesystem) count").isEqualTo(6L);
				softly.assertThat(consistency.getDbFichierCount()).as("Fichier (entity) count").isEqualTo(6L);
				softly.assertThat(consistency.getSizeMismatchCount()).isEqualTo(0);
				softly.assertThat(consistency.getChecksumMismatchCount()).isEqualTo(0);
				softly.assertThat(consistency.getMissingFileCount()).isEqualTo(0);
				softly.assertThat(consistency.getMissingFichierCount()).isEqualTo(0);
				softly.assertThat(consistency.getCheckStartedOn()).isBefore(consistency.getCheckFinishedOn());
				softly.assertThat(consistency.getCheckFinishedOn()).isAfterOrEqualTo(testStart).isBeforeOrEqualTo(LocalDateTime.now());
				softly.assertThat(consistency.getDbFichierSize()).isEqualTo(6 * "fichiern".length());
				softly.assertThat(consistency.getFsFileSize()).isEqualTo(6 * "fichiern".length());
			}, atIndex(0));
	}

	private Fichier createFichier(String relativePath, String checksum) {
		Fichier fichier = new Fichier();
		fichier.setRelativePath(relativePath);
		fichier.setChecksum(checksum);
		fichier.setSize((long) checksum.length());
		return fichier;
	}

	@Test
	void testConsistencyFailures(SoftAssertions softly) throws IOException {
		// BEWARE
		// storageOperations use filename (fichier1, fichier2) to generate file content and checksum == filename
		// databaseOperations use checksum to calculate length
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
			path.resolve("fichier6"), // file missing in database
			path.resolve("fichier7") // file missing in database
		));
		when(storageOperations.checksum(any()))
			.then(AnswerFunctionalInterfaces.<String, Path>toAnswer(
					p -> p.getFileName().toString()
		));
		when(storageOperations.length(any()))
			.then(AnswerFunctionalInterfaces.<Long, Path>toAnswer(
					p -> (long) p.getFileName().toString().length()
		));
		Fichier fichier1 = createFichier("fichier1", "fichier1");
		Fichier fichier2 = createFichier("fichier2", "fichier2");
		Fichier fichier4 = createFichier("fichier4", "fichier4"); // missing is FS
		Fichier fichier5 = createFichier("fichier5", "fichier4"); // checksum mismatch
		Fichier fichier7 = createFichier("fichier7", "fichier"); // size mismatch
		when(databaseOperations.listUnitAliveFichiers(unit)).thenReturn(Set.of(
			fichier1,
			fichier2,
			fichier4,
			fichier5,
			fichier7
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
			assertThat(f.getType()).isEqualTo(StorageFailureType.CHECKSUM_MISMATCH);
			assertThat(f.getFichier()).isEqualTo(fichier5);
			assertThat(f.getPath()).isEqualTo(path.resolve(Path.of("fichier5")).toString());
		}));
		verify(databaseOperations).triggerFailure(argThat(f -> {
			assertThat(f.getType()).isEqualTo(StorageFailureType.SIZE_MISMATCH);
			assertThat(f.getFichier()).isEqualTo(fichier7);
			assertThat(f.getPath()).isEqualTo(path.resolve(Path.of("fichier7")).toString());
		}));
		assertThat(beans)
			.hasSize(1)
			.satisfies(consistency -> {
				softly.assertThat(consistency.getStatus()).as("Check status").isEqualTo(StorageConsistencyCheckResult.FAILED);
				softly.assertThat(consistency.getCheckType()).as("Check type").isEqualTo(StorageUnitCheckType.LISTING_SIZE_CHECKSUM);
				softly.assertThat(consistency.getFsFileCount()).as("File (filesystem) count").isEqualTo(6L);
				softly.assertThat(consistency.getDbFichierCount()).as("Fichier (entity) count").isEqualTo(5L);
				softly.assertThat(consistency.getSizeMismatchCount()).isEqualTo(1);
				softly.assertThat(consistency.getChecksumMismatchCount()).isEqualTo(1);
				softly.assertThat(consistency.getMissingFileCount()).isEqualTo(1);
				softly.assertThat(consistency.getMissingFichierCount()).isEqualTo(2);
				softly.assertThat(consistency.getCheckStartedOn()).isBefore(consistency.getCheckFinishedOn());
				softly.assertThat(consistency.getCheckFinishedOn()).isAfterOrEqualTo(testStart).isBeforeOrEqualTo(LocalDateTime.now());
				softly.assertThat(consistency.getDbFichierSize()).isEqualTo(4 * "fichiern".length() + "fichier".length()); // fichier7 has size len("fichier")
				softly.assertThat(consistency.getFsFileSize()).isEqualTo(6 * "fichiern".length());
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

	@Test
	void testConsistencyCheckTypeNoCheck(SoftAssertions softly) {
		StorageUnit unit = new StorageUnit();
		// check is useless ; no database lookup
		// dates and delays are not checked
		assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString, LocalDateTime.now(), () -> StorageUnitCheckType.NONE, null, null, null, null)).isEqualTo(StorageUnitCheckType.NONE);
		verifyNoInteractions(databaseOperations);
	}

	@Test
	void testConsistencyCheckTypeInitialized(SoftAssertions softly) {
		StorageUnit unit = new StorageUnit();

		List<StorageConsistencyCheck> consistencies = storageService.checkConsistency(unit, false);

		assertThat(consistencies)
			.hasSize(1)
			.satisfies(
				consistency -> assertThat(consistency.getCheckType()).isEqualTo(StorageUnitCheckType.LISTING_SIZE),
				atIndex(0)
			);
	}

	@Test
	void testConsistencyCheckTypeChecksumInitialized(SoftAssertions softly) {
		StorageUnit unit = new StorageUnit();

		List<StorageConsistencyCheck> consistencies = storageService.checkConsistency(unit, true);

		assertThat(consistencies)
			.hasSize(1)
			.satisfies(
				consistency -> assertThat(consistency.getCheckType()).isEqualTo(StorageUnitCheckType.LISTING_SIZE_CHECKSUM),
				atIndex(0)
			);
	}

	@Test
	void testConsistencyCheckTypeDelayElapsed(SoftAssertions softly) {
		StorageUnit unit = new StorageUnit();
		LocalDateTime now = LocalDateTime.now();
		// NOTE: checksum date and delay not used as checksum not needed for unit
		// not elapsed (last check not older than delay)
		softly.assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE,
				() -> _24h00m00sAgo(now), // last check last day
				null,
				() -> Duration.ofDays(1),
				null
		)).isEqualTo(StorageUnitCheckType.NONE);
		// not elapsed (last check on delay)
		softly.assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE,
				() -> _23h59m59sAgo(now), // last check 23h59:59 ago
				null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).isEqualTo(StorageUnitCheckType.NONE);
		// no previous check
		softly.assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE,
				() -> null,
				null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).isEqualTo(StorageUnitCheckType.LISTING_SIZE);
		// elasped (last check older than delay)
		softly.assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE,
				() -> _24h00m01sAgo(now), // last check 24h00:01 ago
				null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).isEqualTo(StorageUnitCheckType.LISTING_SIZE);
	}

	@Test
	void testConsistencyCheckTypeDelayElapsedWithChecksum(SoftAssertions softly) {
		StorageUnit unit = new StorageUnit();
		LocalDateTime now = LocalDateTime.now();
		assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE_CHECKSUM,
				() -> _24h00m00sAgo(now), // last check last day
				null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).as("Last check not old enough. Not check expected.").isEqualTo(StorageUnitCheckType.NONE);
		assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE_CHECKSUM,
				() -> _23h59m59sAgo(now), // last check after last day
				() -> null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).as("Last check not old enough. Not  check expected.").isEqualTo(StorageUnitCheckType.NONE);
		softly.assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE_CHECKSUM,
				() -> null,
				() -> null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).as("No last check. Checksum expected.").isEqualTo(StorageUnitCheckType.LISTING_SIZE_CHECKSUM);
		assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE_CHECKSUM,
				() -> _24h00m01sAgo(now), // last check older than lastday
				() -> null,
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).as("Last check old enough and no last checksum. Checksum expected.").isEqualTo(StorageUnitCheckType.LISTING_SIZE_CHECKSUM);
		assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE_CHECKSUM,
				() -> _24h00m01sAgo(now),
				() -> _23h59m59sAgo(now),
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).as("Last check old enough and last checksum not old enough. Basic check expected.").isEqualTo(StorageUnitCheckType.LISTING_SIZE);
		assertThat(StorageHousekeepingService.getCheckConsistencyType(unit::toString,
				now,
				() -> StorageUnitCheckType.LISTING_SIZE_CHECKSUM,
				() -> _24h00m01sAgo(now),
				() -> _24h00m01sAgo(now),
				() -> Duration.ofDays(1),
				() -> Duration.ofDays(1)
		)).as("Last check old enough and last checksum old enough. Checksum expected.").isEqualTo(StorageUnitCheckType.LISTING_SIZE_CHECKSUM);
	}

	@Test
	void testIsExpectedCheckDateElapsed(SoftAssertions softly) {
		LocalDateTime now = LocalDateTime.now();
		// if date is null, delay is not needed to return true
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> null, null, now))
			.as("No previous check. Elapsed.")
			.isTrue();
		// check that delay allow switch result
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> _23h59m59sAgo(now), () -> Duration.ofDays(1), now))
			.as("Last check age less than duration. Not elapsed.")
			.isFalse();
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> _24h00m00sAgo(now), () -> Duration.ofDays(1), now))
			.as("Last check age equals duration. Not elapsed.")
			.isFalse();
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> _24h00m01sAgo(now), () -> Duration.ofDays(1), now))
			.as("Last check age older than duration. Elapsed.")
			.isTrue();
		Duration delay = Duration.ofMinutes(45);
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> now.minus(delay), () -> delay, now))
			.as("Last check age less than duration. Not elapsed.")
			.isFalse();
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> now.minus(delay.minus(Duration.ofSeconds(1))), () -> delay, now))
			.as("Last check age equals duration. Not elapsed.")
			.isFalse();
		softly.assertThat(StorageHousekeepingService.isExpectedCheckDateElapsed(() -> now.minus(delay.plus(Duration.ofSeconds(1))), () -> delay, now))
			.as("Last check age older than duration. Elapsed.")
			.isTrue();
	}

	@Test
	void testConsistencyNoLimit() {
		StorageUnit unit1 = new StorageUnit();
		StorageUnit unit2 = new StorageUnit();
		StorageUnit unit3 = new StorageUnit();
		Supplier<List<StorageUnit>> units = () -> List.of(
					unit1,
					unit2,
					unit3
		);
		Function<StorageUnit, StorageUnitCheckType> policySupplier = (unit) -> StorageUnitCheckType.LISTING_SIZE;
		@SuppressWarnings("unchecked")
		BiConsumer<StorageUnit, Boolean> checker = ((BiConsumer<StorageUnit, Boolean>) mock(BiConsumer.class));
		
		StorageHousekeepingService.checkConsistency(null, units, policySupplier, checker);
		ArgumentCaptor<StorageUnit> capture = forClass(StorageUnit.class);
		verify(checker, times(3)).accept(capture.capture(), eq(Boolean.FALSE));
		assertThat(capture.getAllValues()).containsExactly(unit1, unit2, unit3);
	}

	@Test
	void testConsistencyLimit() {
		StorageUnit unit1 = new StorageUnit();
		unit1.setId(1l);
		StorageUnit unit2 = new StorageUnit();
		unit2.setId(2l);
		StorageUnit unit3 = new StorageUnit();
		unit3.setId(3l);
		Supplier<List<StorageUnit>> units = () -> List.of(
					unit1,
					unit2,
					unit3
		);
		// ensure that skipped unit1 does not affect limit
		@SuppressWarnings("unchecked")
		Function<StorageUnit, StorageUnitCheckType> policySupplier = ((Function<StorageUnit, StorageUnitCheckType>) mock(Function.class));
		when(policySupplier.apply(unit1)).thenReturn(StorageUnitCheckType.NONE);
		when(policySupplier.apply(unit2)).thenReturn(StorageUnitCheckType.LISTING_SIZE);
		when(policySupplier.apply(unit3)).thenReturn(StorageUnitCheckType.LISTING_SIZE);
		@SuppressWarnings("unchecked")
		BiConsumer<StorageUnit, Boolean> checker = ((BiConsumer<StorageUnit, Boolean>) mock(BiConsumer.class));
		
		StorageHousekeepingService.checkConsistency(1, units, policySupplier, checker);
		ArgumentCaptor<StorageUnit> capture = forClass(StorageUnit.class);
		verify(checker, times(1)).accept(capture.capture(), eq(Boolean.FALSE));
		assertThat(capture.getAllValues()).containsExactly(unit2);
	}

	private LocalDateTime _24h00m01sAgo(LocalDateTime reference) {
		return reference.minus(Duration.ofDays(1).plus(Duration.ofSeconds(1)));
	}

	private LocalDateTime _23h59m59sAgo(LocalDateTime reference) {
		return reference.minus(Duration.ofDays(1).minus(Duration.ofSeconds(1)));
	}

	private LocalDateTime _24h00m00sAgo(LocalDateTime now) {
		return now.minus(Duration.ofDays(1));
	}
}
