package org.iglooproject.wicket.more.request.resource;

import java.io.File;
import java.io.InputStream;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

public class FileStoreResourceStream extends FileResourceStream {

  private static final long serialVersionUID = -8027981055479218267L;

  public static FileStoreResourceStream notFound() {
    return new FileStoreResourceStream(new File("")) {
      private static final long serialVersionUID = 1L;

      @Override
      public InputStream getInputStream() throws ResourceStreamNotFoundException {
        throw new ResourceStreamNotFoundException();
      }
    };
  }

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
