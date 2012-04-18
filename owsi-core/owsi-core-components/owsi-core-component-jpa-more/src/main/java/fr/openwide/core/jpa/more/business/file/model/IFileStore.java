package fr.openwide.core.jpa.more.business.file.model;

import java.io.File;
import java.io.InputStream;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IFileStore {
	
	String getKey();
	
	void check() throws IllegalStateException;

	FileInformation addFile(File file, String fileKey, String extension) throws ServiceException, SecurityServiceException;
	
	FileInformation addFile(byte[] content, String fileKey, String extension) throws ServiceException, SecurityServiceException;

	FileInformation addFile(InputStream inputStream, String fileKey, String extension) throws ServiceException, SecurityServiceException;

	File getFile(String fileKey, String extension);

	void removeFile(String fileKey, String extension);


}
