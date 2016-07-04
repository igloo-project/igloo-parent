package fr.openwide.core.commons.util.functional.converter;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Converter;

public class StringDirectoryFileCreatingConverter extends Converter<String, File> {

	private static final StringDirectoryFileCreatingConverter INSTANCE = new StringDirectoryFileCreatingConverter();

	public static StringDirectoryFileCreatingConverter get() {
		return INSTANCE;
	}

	protected StringDirectoryFileCreatingConverter() {
	}

	@Override
	protected File doForward(String path) {
		if (!StringUtils.isEmpty(path) && !"/".equals(path)) {
			File directory = new File(path);
			
			if (directory.isDirectory() && directory.canWrite()) {
				return directory;
			}
			if (!directory.exists()) {
				try {
					FileUtils.forceMkdir(directory);
					return directory;
				} catch (Exception e) {
					throw new IllegalStateException("The directory " + path + " does not exist and it is impossible to create it.");
				}
			}
		}
		throw new IllegalStateException("The tmp directory " + path + " is not writable.");
	}

	@Override
	protected String doBackward(File b) {
		return b.getPath();
	}

	/**
	 * Workaround sonar/findbugs - https://github.com/google/guava/issues/1858
	 * Guava Converter overrides only equals to add javadoc, but findbugs warns about non coherent equals/hashcode
	 * possible issue.
	 */
	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	/**
	 * Workaround sonar/findbugs - see #equals(Object)
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
