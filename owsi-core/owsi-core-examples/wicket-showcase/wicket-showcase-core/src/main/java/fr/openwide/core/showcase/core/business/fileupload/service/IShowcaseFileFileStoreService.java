package fr.openwide.core.showcase.core.business.fileupload.service;

import java.io.File;
import java.io.InputStream;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.file.model.FileInformation;
import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;

public interface IShowcaseFileFileStoreService {

	File getFile(ShowcaseFile showcaseFile);

	void removeFile(ShowcaseFile showcasefile) throws ServiceException, SecurityServiceException;

	FileInformation addFile(ShowcaseFile showcaseFile, InputStream fileInputStream) throws ServiceException, SecurityServiceException;

}
