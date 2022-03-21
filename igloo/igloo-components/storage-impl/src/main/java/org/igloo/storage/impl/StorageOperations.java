package org.igloo.storage.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
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

	public static Collection<File> listRecursively(File directory, IOFileFilter resultsFilter, IOFileFilter recurseFilter) {
		if (directory == null) {
			throw new IllegalArgumentException(String.format("Null directory is not allowed (filter: %s, recurseFilter: %s)",
					resultsFilter, recurseFilter));
		}
		Collection<File> results = Lists.newLinkedList();

		IOFileFilter effectiveRecurseFilter = FileFilterUtils.makeDirectoryOnly(recurseFilter);
		innerListRecursively(results, directory, resultsFilter, effectiveRecurseFilter);

		return results;
	}

	private static void innerListRecursively(Collection<File> results, File directory, IOFileFilter resultsFilter, IOFileFilter recurseFilter) {
		List<File> found = listFiles(directory, resultsFilter);
		results.addAll(found);

		List<File> recursedSubdirectories = listFiles(directory, recurseFilter);
		for (File recursedSubdirectory : recursedSubdirectories) {
			innerListRecursively(results, recursedSubdirectory, resultsFilter, recurseFilter);
		}
	}

	private static List<File> listFiles(File directory, FileFilter filter) {
		if (directory == null) {
			throw new IllegalArgumentException(String.format("Null directory is not allowed (filter: %s)", filter));
		}

		List<File> files = Lists.newArrayList();

		File[] filesArray = directory.listFiles(filter);
		if (filesArray != null) {
			Arrays.sort(filesArray);
			for (int i = 0; i < filesArray.length; i++) {
				File file = filesArray[i];
				if (file.canRead()) {
					files.add(file);
				}
			}
		} else {
			// le résultat filesPaths est null si et seulement si il y a un problème avec la lecture du répertoire
			throw new IllegalStateException("Error reading directory: " + directory.getPath());
		}
		return files;
	}

}
