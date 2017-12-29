package org.iglooproject.showcase.core.business.fileupload.service;

import java.io.File;
import java.io.InputStream;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.file.model.FileInformation;
import org.iglooproject.showcase.core.business.fileupload.model.ShowcaseFile;

public interface IShowcaseFileFileStoreService {

	File getFile(ShowcaseFile showcaseFile);

	void removeFile(ShowcaseFile showcasefile) throws ServiceException, SecurityServiceException;

	FileInformation addFile(ShowcaseFile showcaseFile, InputStream fileInputStream) throws ServiceException, SecurityServiceException;

}
