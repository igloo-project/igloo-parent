package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

import org.assertj.core.matcher.AssertionMatcher;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageEventType;
import org.igloo.storage.impl.StorageTransactionAdapter;
import org.igloo.storage.impl.StorageTransactionHandler;
import org.junit.jupiter.api.Test;
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
	 * On commit, deleted files are physically removed. Other events are ignored.
	 */
	@Test
	void testTransactionCommit() throws IOException {
		StorageOperations operations = Mockito.mock(StorageOperations.class);
		StorageTransactionAdapter adapter = new StorageTransactionAdapter(new StorageTransactionHandler(operations));
		Path fichier1 = Path.of("fichier1");
		Path fichier2 = Path.of("fichier2");
		Path fichier3 = Path.of("fichier3");
		adapter.addEvent(1l, StorageEventType.ADD, fichier1);
		adapter.addEvent(2l, StorageEventType.DELETE, fichier2);
		adapter.addEvent(4l, StorageEventType.ADD, fichier3);
		adapter.afterCompletion(TransactionSynchronization.STATUS_COMMITTED);
		verify(operations).removePhysicalFile(anyString(), argThat(t -> assertThat(t.getPath()).isEqualTo(fichier2)));
		verifyNoMoreInteractions(operations);
	}

	/**
	 * On rollback, added files are physically removed. Other events are ignored.
	 */
	@Test
	void testTransactionRollback() throws IOException {
		StorageOperations operations = Mockito.mock(StorageOperations.class);
		StorageTransactionAdapter adapter = new StorageTransactionAdapter(new StorageTransactionHandler(operations));
		Path fichier1 = Path.of("fichier1");
		Path fichier2 = Path.of("fichier2");
		Path fichier3 = Path.of("fichier3");
		adapter.addEvent(1l, StorageEventType.ADD, fichier1);
		adapter.addEvent(2l, StorageEventType.DELETE, fichier2);
		adapter.addEvent(4l, StorageEventType.ADD, fichier3);
		adapter.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);
		verify(operations).removePhysicalFile(anyString(), argThat(t -> assertThat(t.getPath()).isEqualTo(fichier1)));
		verify(operations).removePhysicalFile(anyString(), argThat(t -> assertThat(t.getPath()).isEqualTo(fichier3)));
		verifyNoMoreInteractions(operations);
	}

}
