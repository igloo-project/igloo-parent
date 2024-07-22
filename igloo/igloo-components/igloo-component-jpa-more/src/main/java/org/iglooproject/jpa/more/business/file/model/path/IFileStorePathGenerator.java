package org.iglooproject.jpa.more.business.file.model.path;

public interface IFileStorePathGenerator {

  String getFilePath(String fileKey, String extension);
}
