package org.iglooproject.jpa.more.business.file.model;

import java.io.File;
import java.io.InputStream;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IFileStore {

  String getKey();

  void check() throws IllegalStateException;

  FileInformation addFile(File file, String fileKey, String extension)
      throws ServiceException, SecurityServiceException;

  FileInformation addFile(byte[] content, String fileKey, String extension)
      throws ServiceException, SecurityServiceException;

  FileInformation addFile(InputStream inputStream, String fileKey, String extension)
      throws ServiceException, SecurityServiceException;

  File getFile(String fileKey, String extension);

  void removeFile(String fileKey, String extension);
}
