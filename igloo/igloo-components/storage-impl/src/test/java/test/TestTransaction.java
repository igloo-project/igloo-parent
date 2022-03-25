package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.igloo.storage.impl.IStorageTransactionResourceManager;
import org.igloo.storage.impl.StorageEvent;
import org.igloo.storage.impl.StorageEventType;
import org.igloo.storage.impl.StorageOperations;
import org.igloo.storage.impl.StorageTransactionAdapter;
import org.igloo.storage.impl.StorageTransactionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.Ordered;
import org.springframework.transaction.support.TransactionSynchronization;

class TestTransaction extends AbstractTest {

	private StorageTransactionResourceManager resourceManager = new StorageTransactionResourceManager();

	/**
	 * On commit, deleted files are physically removed. Other events are ignored.
	 */
	@Test
	void testTransactionCommit() throws IOException {
		StorageOperations operations = Mockito.mock(StorageOperations.class);
		StorageTransactionAdapter adapter = new StorageTransactionAdapter(Ordered.LOWEST_PRECEDENCE, new StorageTransactionHandler(operations), resourceManager);
		Path fichier1 = Path.of("fichier1");
		Path fichier2 = Path.of("fichier2");
		Path fichier3 = Path.of("fichier3");
		resourceManager.addEvent(1l, StorageEventType.ADD, fichier1);
		resourceManager.addEvent(2l, StorageEventType.DELETE, fichier2);
		resourceManager.addEvent(4l, StorageEventType.ADD, fichier3);
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
		StorageTransactionAdapter adapter = new StorageTransactionAdapter(Ordered.LOWEST_PRECEDENCE, new StorageTransactionHandler(operations), resourceManager);
		Path fichier1 = Path.of("fichier1");
		Path fichier2 = Path.of("fichier2");
		Path fichier3 = Path.of("fichier3");
		resourceManager.addEvent(1l, StorageEventType.ADD, fichier1);
		resourceManager.addEvent(2l, StorageEventType.DELETE, fichier2);
		resourceManager.addEvent(4l, StorageEventType.ADD, fichier3);
		adapter.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK);
		verify(operations).removePhysicalFile(anyString(), argThat(t -> assertThat(t.getPath()).isEqualTo(fichier1)));
		verify(operations).removePhysicalFile(anyString(), argThat(t -> assertThat(t.getPath()).isEqualTo(fichier3)));
		verifyNoMoreInteractions(operations);
	}

	public static class StorageTransactionResourceManager implements IStorageTransactionResourceManager {
		public List<StorageEvent> events = new ArrayList<>();
		
		@Override
		public void addEvent(Long id, StorageEventType type, Path path) {
			events.add(new StorageEvent(id, type, path));
		}
		
		@Override
		public List<StorageEvent> getEvents() {
			return events;
		}
		
		@Override
		public void unbind() {
			// NOSONAR
		}
	}

}
