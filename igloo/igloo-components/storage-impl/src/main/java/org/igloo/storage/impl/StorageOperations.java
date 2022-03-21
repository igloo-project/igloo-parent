package org.igloo.storage.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageOperations.class);

	public void removePhysicalFile(String logPrefix, StorageEvent t) {
		Long fichierId = t.getId();
		Path fileAbsolutePath = t.getPath();
		removePhysicalFile(logPrefix, fichierId, fileAbsolutePath);
	}

	public void removePhysicalFile(String logPrefix, Long fichierId, Path fileAbsolutePath) {
		try {
			if (Files.exists(fileAbsolutePath, LinkOption.NOFOLLOW_LINKS)) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.debug("{}: deleting Fichier {} '{}'", logPrefix, fichierId, fileAbsolutePath);
				}
				Files.delete(fileAbsolutePath);
			} else {
				LOGGER.warn("{} Fichier {} '{}' cannot be removed - already absent", logPrefix, fichierId, fileAbsolutePath);
			}
		} catch (RuntimeException|IOException e) {
			LOGGER.error("{} Fichier {} '{}' cannot be removed", logPrefix, fichierId, fileAbsolutePath, e);
		}
	}

	public void copy(InputStream inputStream, Path absolutePath) throws IOException {
		Path dirPath = absolutePath.getParent();
		File directory = dirPath.toFile();
		if (directory.exists() && !directory.isDirectory()) {
			throw new IllegalStateException(String.format("Parent path %s already exists and is not a directory", dirPath.toAbsolutePath()));
		}
		if (!directory.exists()) {
			FileUtils.forceMkdir(directory);
		}

		try (FileOutputStream fos = new FileOutputStream(absolutePath.toString())) {
			IOUtils.copy(inputStream, fos);
		} catch (RuntimeException e) {
			// TODO use custom runtime exception
			throw new IllegalStateException(e);
		}
	}

	public File getFile(Path absolutePath) {
		return absolutePath.toFile();
	}
}
