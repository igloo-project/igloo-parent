package org.igloo.storage.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

public class StorageOperations {

	public void doRemovePhysicalFichier(String logPrefix, StorageTask t) {
		try {
			if (Files.exists(t.getPath(), LinkOption.NOFOLLOW_LINKS)) {
				Files.delete(t.getPath());
			} else {
				StorageTransactionAdapter.LOGGER.warn("{} {} '{}' cannot be removed on rollback event - already absent", logPrefix, t.getId(), t.getPath());
			}
		} catch (RuntimeException|IOException e) {
			StorageTransactionAdapter.LOGGER.error("{} {} '{}' cannot be removed on rollback event", logPrefix, t.getId(), t.getPath(), e);
		}
	}

}
