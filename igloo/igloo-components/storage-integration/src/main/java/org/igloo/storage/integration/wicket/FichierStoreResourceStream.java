package org.igloo.storage.integration.wicket;

import java.io.File;
import java.io.InputStream;

import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

public class FichierStoreResourceStream extends FileResourceStream {

	private static final long serialVersionUID = -8027981055479218267L;
	
	public static FichierStoreResourceStream notFound() {
		return new FichierStoreResourceStream(new File("")) {
			private static final long serialVersionUID = 1L;
			@Override 
			public InputStream getInputStream() throws ResourceStreamNotFoundException {
				throw new ResourceStreamNotFoundException();
			}
		};
	}
	
	private String fileName;
	
	public FichierStoreResourceStream(File file) {
		this(file, null);
	}
	
	public FichierStoreResourceStream(File file, String fileName) {
		super(file);
		
		Args.notNull(file, "file");
		
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

}