package org.igloo.storage.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageOperations.class);

	public void doRemovePhysicalFile(String logPrefix, StorageTask t) {
		try {
			if (Files.exists(t.getPath(), LinkOption.NOFOLLOW_LINKS)) {
				Files.delete(t.getPath());
			} else {
				LOGGER.warn("{} {} '{}' cannot be removed on rollback event - already absent", logPrefix, t.getId(), t.getPath());
			}
		} catch (RuntimeException|IOException e) {
			LOGGER.error("{} {} '{}' cannot be removed on rollback event", logPrefix, t.getId(), t.getPath(), e);
		}
	}

}
