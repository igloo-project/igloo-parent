package org.iglooproject.showcase.core.business.fileupload.service;

import java.io.InputStream;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.showcase.core.business.fileupload.model.ShowcaseFile;

public interface IShowcaseFileService extends IGenericEntityService<Long, ShowcaseFile> {

	void addFile(ShowcaseFile showcaseFile, InputStream data) throws ServiceException, SecurityServiceException;

}
