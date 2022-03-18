package org.igloo.storage.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageOperations.class);

	public void doRemovePhysicalFile(String logPrefix, StorageTask t) {
		try {
			if (Files.exists(t.getPath(), LinkOption.NOFOLLOW_LINKS)) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.debug("{}: deleting {} '{}'", logPrefix, t.getId(), t.getPath());
				}
				Files.delete(t.getPath());
			} else {
				LOGGER.warn("{} {} '{}' cannot be removed - already absent", logPrefix, t.getId(), t.getPath());
			}
		} catch (RuntimeException|IOException e) {
			LOGGER.error("{} {} '{}' cannot be removed", logPrefix, t.getId(), t.getPath(), e);
		}
	}

	public void copy(InputStream inputStream, Path absolutePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(absolutePath.toString())) {
			IOUtils.copy(inputStream, fos);
		} catch (RuntimeException e) {
			// TODO use custom runtime exception
			throw new IllegalStateException(e);
		}
	}

}
