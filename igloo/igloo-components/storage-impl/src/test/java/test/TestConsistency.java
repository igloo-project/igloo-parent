package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.assertj.core.api.SoftAssertions;
import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import test.model.FichierType1;

public class TestConsistency extends AbstractTest {

	@Override
	@BeforeEach
	void initStorageUnit(EntityManagerFactory entityManagerFactory, @TempDir Path tempDir) {
		super.initStorageUnit(entityManagerFactory, tempDir);
	}

	@Test
	void testConsistency() throws IOException {
		doCallRealMethod().when(operations).copy(any(), any());
		doCallRealMethod().when(operations).getFile(any());
		doCallRealMethod().when(operations).listRecursively(any());
		Fichier fichier = transactionTemplate.execute((t) -> createFichier("filename.txt", FichierType1.CONTENT1, FILE_CONTENT, () -> {}));

		List<StorageConsistency> beans = transactionTemplate.execute((t) -> storageService.checkConsistency(fichier.getStorageUnit(), true));

		SoftAssertions soft = new SoftAssertions();
		assertThat(beans)
			.hasSize(1)
			.satisfies(consistency -> {
				soft.assertThat(consistency.getFsFileCount()).isEqualTo(1L);
				soft.assertThat(consistency.getDbFichierCount()).isEqualTo(1L);
			}, atIndex(0));
		soft.assertAll();
	}

}
