package fr.openwide.core.wicket.more.request.resource;

import java.io.File;

import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.resource.FileResourceStream;

public class FileStoreResourceStream extends FileResourceStream {

	private static final long serialVersionUID = -8027981055479218267L;
	
	private String fileName;
	
	public FileStoreResourceStream(File file) {
		this(file, null);
	}
	
	public FileStoreResourceStream(File file, String fileName) {
		super(file);
		
		Args.notNull(file, "file");
		
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

}