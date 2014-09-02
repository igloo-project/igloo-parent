package fr.openwide.core.showcase.core.business.fileupload.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.showcase.core.business.fileupload.dao.IShowcaseFileDao;
import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;

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
