package fr.openwide.core.showcase.core.business.fileupload.service;

import java.io.InputStream;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;

public interface IShowcaseFileService extends IGenericEntityService<Long, ShowcaseFile> {

	void addFile(ShowcaseFile showcaseFile, InputStream data) throws ServiceException, SecurityServiceException;

}
