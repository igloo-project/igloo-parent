package org.igloo.storage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.igloo.storage.model.StorageUnit;
import org.iglooproject.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;

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

	@Nonnull
	public Set<Path> listUnitContent(StorageUnit unit) {
		Path absolutePath = Path.of(unit.getPath());
		return Optional.ofNullable(listRecursively(absolutePath))
				.orElseGet(Collections::emptyList)
				.stream()
				.map(File::toPath)
				.collect(Collectors.toSet());
	}

	@Nullable
	private Collection<File> listRecursively(@Nonnull Path directoryPath) {
		File directory = getFile(directoryPath);
		if (!directory.exists()) {
			return null;
		}
		return FileUtils.listRecursively(getFile(directoryPath), FileFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}

	@Nonnull
	public String checksum(Path root) {
		String checksum = null;
		try (InputStream fis = new FileInputStream(root.toFile()); HashingInputStream his = new HashingInputStream(Hashing.sha256(), fis)) {
			his.readAllBytes();
			checksum = his.hash().toString();
		} catch (IOException e) {
			throw new IllegalStateException(String.format("Checksum calculation error on %s", root), e);
		}
		return checksum;
	}
}
