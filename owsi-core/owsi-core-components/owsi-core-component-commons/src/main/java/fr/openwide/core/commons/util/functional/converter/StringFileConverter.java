package fr.openwide.core.commons.util.functional.converter;

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

}
