package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.igloo.storage.impl.DatabaseOperations;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistency;
import org.igloo.storage.model.StorageUnit;
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
		StorageUnit unit = new StorageUnit();
		Path path = Path.of("/my-root-path");
		unit.setPath(path.toString());
		when(storageOperations.listUnitContent(unit)).thenReturn(Set.of(
			Path.of("fichier1"),
			Path.of("fichier2")
		));
		when(storageOperations.checksum(any())).then(AnswerFunctionalInterfaces.<String, Path>toAnswer(p -> p.getFileName().toString()));
		Fichier fichier1 = new Fichier();
		fichier1.setRelativePath("fichier1");
		fichier1.setChecksum("fichier1");
		Fichier fichier2 = new Fichier();
		fichier2.setRelativePath("fichier2");
		fichier2.setChecksum("fichier1");
		when(databaseOperations.listUnitAliveFichiers()).thenReturn(Set.of(
			fichier1,
			fichier2
		));
		List<StorageConsistency> beans = storageService.checkConsistency(unit, true);

		assertThat(beans)
			.hasSize(1)
			.satisfies(consistency -> {
				softly.assertThat(consistency.getFsFileCount()).as("File (filesystem) count").isEqualTo(2L);
				softly.assertThat(consistency.getDbFichierCount()).as("Fichier (entity) count").isEqualTo(2L);
			}, atIndex(0));
	}

}
