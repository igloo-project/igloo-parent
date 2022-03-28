package org.igloo.storage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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

	public void removePhysicalFile(@Nonnull String logPrefix, @Nonnull Long fichierId, @Nonnull Path fileAbsolutePath) {
		checkAbsolutePath(fileAbsolutePath);
		try {
			if (Files.exists(fileAbsolutePath, LinkOption.NOFOLLOW_LINKS)) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("{}: deleting Fichier {} '{}'", logPrefix, fichierId, fileAbsolutePath);
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
		Objects.requireNonNull(inputStream, "InputStream must not be null.");
		checkAbsolutePath(absolutePath);
		if (absolutePath.toFile().exists() && absolutePath.toFile().isFile()) {
			throw new IllegalStateException(String.format("File %s already exists and is a file.", absolutePath));
		}
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
	public File getFile(@Nonnull Path absolutePath) throws FileNotFoundException {
		checkAbsolutePath(absolutePath);
		File file = absolutePath.toFile();
		if (file.exists() && file.canRead()) {
			return file;
		} else {
			throw new FileNotFoundException(String.format("File %s cannot be found or is not readable", file));
		}
	}

	/**
	 * List content from a {@link StorageUnit}. If {@link StorageUnit} folder does not exist, returns an empty set.
	 * 
	 * @param unit an existing {@link StorageUnit}
	 * @return a set of absolutePath designing file in unit directory. Folders are ignored.
	 */
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
		File directory = directoryPath.toFile();
		if (!directory.exists()) {
			return null;
		}
		return FileUtils.listRecursively(directory, FileFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}

	@Nonnull
	public String checksum(Path path) throws FileNotFoundException {
		checkAbsolutePath(path);
		String checksum = null;
		File file = path.toFile();
		if (file.exists() && file.canRead()) {
			try (InputStream fis = new FileInputStream(path.toFile()); HashingInputStream his = new HashingInputStream(Hashing.sha256(), fis)) {
				his.readAllBytes();
				checksum = his.hash().toString();
				return checksum;
			} catch (IOException e) {
				throw new IllegalStateException(String.format("Checksum calculation error on %s", path), e);
			}
		} else {
			throw new FileNotFoundException(String.format("File %s cannot be found or is not readable", file));
		}
	}

	private void checkAbsolutePath(Path fileAbsolutePath) {
		Objects.requireNonNull(fileAbsolutePath, "Path must not be null.");
		if (!fileAbsolutePath.isAbsolute()) {
			throw new IllegalStateException(String.format("Path %s is not absolute.", fileAbsolutePath));
		}
		if (fileAbsolutePath.toFile().exists() && fileAbsolutePath.toFile().isDirectory()) {
			throw new IllegalStateException(String.format("Path %s is a directory, not a file.", fileAbsolutePath));
		}
	}
}
