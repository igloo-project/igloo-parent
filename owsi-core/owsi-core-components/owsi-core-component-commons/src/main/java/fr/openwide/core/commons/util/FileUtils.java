package fr.openwide.core.commons.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

import com.google.common.collect.Lists;

import de.schlichtherle.truezip.file.TFile;

public final class FileUtils {
	
	public static File getFile(File directory, String name) {
		List<File> files = list(directory, new NameFileFilter(name));
		
		if (files.size() == 1) {
			return files.get(0);
		} else {
			throw new IllegalArgumentException("Unable to find file " + name + " in " + directory.getAbsolutePath());
		}
	}
	
	public static List<File> list(File directory, FilenameFilter filter) {
		List<File> files = new ArrayList<File>();
		
		if (directory != null) {
			String[] filesPaths = directory.list(filter);
			if (filesPaths != null) {
				Arrays.sort(filesPaths);
				for (int i = 0; i < filesPaths.length; i++) {
					TFile file = new TFile(FilenameUtils.concat(directory.getAbsolutePath(), filesPaths[i]));
					if (file.canRead()) {
						files.add(file);
					}
				}
			} else {
				// le résultat filesPaths est null si et seulement si il y a un problème avec la lecture du répertoire
				throw new IllegalStateException("Error reading directory: " + directory.getPath());
			}
		}
		
		return files;
	}
	
	public static List<File> listFiles(File directory, FileFilter filter) {
		List<File> files = new ArrayList<File>();
		
		if (directory != null) {
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
		}
		
		return files;
	}
	
	/**
	 * On contrary to
	 * {@link org.apache.commons.io.FileUtils#listFiles(File, IOFileFilter, IOFileFilter)} and
	 * {@link org.apache.commons.io.FileUtils#listFilesAndDirs(File, IOFileFilter, IOFileFilter)}
	 * , this method returns sub-directories if and only if {@code resultsFilter} allows it.
	 * 
	 * @param resultsFilter
	 *            Filter for returned files.
	 * @param recurseFilter
	 *            Filter for recursion into sub-directories. Will be made
	 *            {@link FileFilterUtils#makeDirectoryOnly(IOFileFilter)
	 *            directory only} before use.
	 */
	public static Collection<File> listRecursively(File directory, IOFileFilter resultsFilter, IOFileFilter recurseFilter) {
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
	
	private FileUtils() {
	}

}
