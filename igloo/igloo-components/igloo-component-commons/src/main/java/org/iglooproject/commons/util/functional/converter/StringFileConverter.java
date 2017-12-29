package org.iglooproject.commons.util.functional.converter;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;

public class StringFileConverter extends Converter<String, File> {
	
	private final FileFilter filter;

	public StringFileConverter(FileFilter filter) {
		this.filter = Preconditions.checkNotNull(filter);
	}

	@Override
	protected File doForward(String path) {
		if (StringUtils.isEmpty(path)) {
			throw new IllegalStateException("An empty string is not a file.");
		}
		
		File file = new File(path);
		if (!filter.accept(file)) {
			throw new IllegalStateException("File " + path + " does not meet the requirements of " + filter);
		}
		
		return file;
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
