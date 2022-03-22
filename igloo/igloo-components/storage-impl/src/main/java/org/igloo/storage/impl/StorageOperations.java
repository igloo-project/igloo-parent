package org.igloo.storage.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IFichierType;
import org.iglooproject.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StorageOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageOperations.class);

	public void removePhysicalFile(@Nonnull String logPrefix, @Nonnull StorageEvent t) {
		Long fichierId = t.getId();
		Path fileAbsolutePath = t.getPath();
		removePhysicalFile(logPrefix, fichierId, fileAbsolutePath);
	}

	public void removePhysicalFile(@Nonnull String logPrefix, @Nonnull Long fichierId, @Nonnull Path fileAbsolutePath) {
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

	public void copy(@Nonnull InputStream inputStream, @Nonnull Path absolutePath) throws IOException {
		Path dirPath = absolutePath.getParent();
		File directory = dirPath.toFile();
		if (directory.exists() && !directory.isDirectory()) {
			throw new IllegalStateException(String.format("Parent path %s already exists and is not a directory", dirPath.toAbsolutePath()));
		}
		if (!directory.exists()) {
			org.apache.commons.io.FileUtils.forceMkdir(directory);
		}

		try (FileOutputStream fos = new FileOutputStream(absolutePath.toString())) {
			IOUtils.copy(inputStream, fos);
		} catch (RuntimeException e) {
			// TODO use custom runtime exception
			throw new IllegalStateException(e);
		}
	}

	@Nonnull
	public File getFile(@Nonnull Path absolutePath) {
		return absolutePath.toFile();
	}

	@Nullable
	public Collection<File> listRecursively(@Nonnull Path directoryPath) {
		File directory = getFile(directoryPath);
		if (!directory.exists()) {
			return null;
		}
		return FileUtils.listRecursively(getFile(directoryPath), TrueFileFilter.TRUE, FileFileFilter.INSTANCE);
	}
}
