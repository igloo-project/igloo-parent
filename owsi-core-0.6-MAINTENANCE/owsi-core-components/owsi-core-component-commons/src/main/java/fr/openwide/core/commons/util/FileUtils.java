package fr.openwide.core.commons.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.NameFileFilter;

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
	
	private FileUtils() {
	}

}
