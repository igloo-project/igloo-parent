package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.assertj.core.matcher.AssertionMatcher;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageTaskType;
import org.igloo.storage.impl.StorageTransactionAdapter;
import org.igloo.storage.impl.StorageTransactionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.transaction.support.TransactionSynchronization;

class TestTransaction {
	public static <T> T argThat(Consumer<T> assertions) {
		return MockitoHamcrest.argThat(new AssertionMatcher<T>() {
			@Override
			public void assertion(T actual) throws AssertionError {
				assertions.accept(actual);
			}
		});
	}

	/**
	 * On commit, deleted files are physically removed. Other tasks are ignored.
	 */
	@Test
	void testTransactionCommit(@TempDir Path tempDir) throws IOException {
		StorageOperations operations = Mockito.spy(StorageOperations.class);
		StorageTransactionAdapter adapter = new StorageTransactionAdapter(new StorageTransactionHandler(operations));
		Path fichier1 = Files.createFile(tempDir.resolve("fichier1"));
		Path fichier2 = Files.createFile(tempDir.resolve("fichier2"));
		Path fichier3 = Files.createFile(tempDir.resolve("fichier3"));
		Path fichier4 = Files.createFile(tempDir.resolve("fichier4"));
		adapter.addTask(1l, StorageTaskType.ADD, fichier1);
		adapter.addTask(1l, StorageTaskType.DELETE, fichier2);
		adapter.addTask(1l, StorageTaskType.ASK_DELETION, fichier3);
		adapter.addTask(1l, StorageTaskType.ADD, fichier4);
		adapter.afterCompletion(TransactionSynchronization.STATUS_COMMITTED);
		Mockito.verify(operations).doRemovePhysicalFichier(Mockito.anyString(), argThat(t -> Assertions.assertThat(t.getPath()).isEqualTo(fichier2)));
		Mockito.verifyNoMoreInteractions(operations);
	}

	/**
	 * On rollback, added files are physically removed. Other tasks are ignored.
	 */
	@Test
	void testTransactionRollback(@TempDir Path tempDir) throws IOException {
		StorageOperations operations = Mockito.spy(StorageOperations.class);
		StorageTransactionAdapter adapter = new StorageTransactionAdapter(new StorageTransactionHandler(operations));
		Path fichier1 = Files.createFile(tempDir.resolve("fichier1"));
		Path fichier2 = Files.createFile(tempDir.resolve("fichier2"));
		Path fichier3 = Files.createFile(tempDir.resolve("fichier3"));
		Path fichier4 = Files.createFile(tempDir.resolve("fichier4"));
		adapter.addTask(1l, StorageTaskType.ADD, fichier1);
		adapter.addTask(1l, StorageTaskType.DELETE, fichier2);
		adapter.addTask(1l, StorageTaskType.ASK_DELETION, fichier3);
		adapter.addTask(1l, StorageTaskType.ADD, fichier4);
		adapter.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);
		Mockito.verify(operations).doRemovePhysicalFichier(Mockito.anyString(), argThat(t -> Assertions.assertThat(t.getPath()).isEqualTo(fichier1)));
		Mockito.verify(operations).doRemovePhysicalFichier(Mockito.anyString(), argThat(t -> Assertions.assertThat(t.getPath()).isEqualTo(fichier4)));
		Mockito.verifyNoMoreInteractions(operations);
	}

}
