package org.iglooproject.showcase.core.business.fileupload.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.showcase.core.business.fileupload.dao.IShowcaseFileDao;
import org.iglooproject.showcase.core.business.fileupload.model.ShowcaseFile;

@Service("showcaseService")
public class ShowcaseFileServiceImpl extends GenericEntityServiceImpl<Long, ShowcaseFile> implements IShowcaseFileService {

	@Autowired
	private IShowcaseFileFileStoreService showcaseFileFileStoreService;

	@Autowired
	public ShowcaseFileServiceImpl(IShowcaseFileDao showcaseDao) {
		super(showcaseDao);
	}

	@Override
	public void delete(ShowcaseFile showcaseFile) throws ServiceException, SecurityServiceException {
		showcaseFileFileStoreService.removeFile(showcaseFile);
		super.delete(showcaseFile);
	}

	@Override
	public void addFile(ShowcaseFile showcaseFile, InputStream dataInputStream) throws ServiceException, SecurityServiceException {
		showcaseFileFileStoreService.addFile(showcaseFile, dataInputStream);
	}

}
