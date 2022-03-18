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

	public void removePhysicalFile(String logPrefix, StorageTask t) {
		Long fichierId = t.getId();
		Path fileAbsolutePath = t.getPath();
		removePhysicalFile(logPrefix, fichierId, fileAbsolutePath);
	}

	public void removePhysicalFile(String logPrefix, Long fichierId, Path fileAbsolutePath) {
		try {
			if (Files.exists(fileAbsolutePath, LinkOption.NOFOLLOW_LINKS)) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.debug("{}: deleting {} '{}'", logPrefix, fichierId, fileAbsolutePath);
				}
				Files.delete(fileAbsolutePath);
			} else {
				LOGGER.warn("{} {} '{}' cannot be removed - already absent", logPrefix, fichierId, fileAbsolutePath);
			}
		} catch (RuntimeException|IOException e) {
			LOGGER.error("{} {} '{}' cannot be removed", logPrefix, fichierId, fileAbsolutePath, e);
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
