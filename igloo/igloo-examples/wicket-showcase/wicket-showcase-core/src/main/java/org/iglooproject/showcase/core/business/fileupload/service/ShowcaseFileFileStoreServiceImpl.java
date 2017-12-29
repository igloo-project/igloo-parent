package org.iglooproject.showcase.core.business.fileupload.service;

import static org.iglooproject.showcase.core.util.property.ShowcaseCorePropertyIds.SHOWCASE_FILE_ROOT_DIRECTORY;

import java.io.File;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.file.model.FileInformation;
import org.iglooproject.jpa.more.business.file.model.SimpleFileStoreImpl;
import org.iglooproject.jpa.more.business.file.service.AbstractFileStoreServiceImpl;
import org.iglooproject.showcase.core.business.fileupload.model.ShowcaseFile;
import org.iglooproject.spring.property.service.IPropertyService;

@Service("showcaseFileFileStoreService")
public class ShowcaseFileFileStoreServiceImpl extends AbstractFileStoreServiceImpl implements IShowcaseFileFileStoreService {

	private static final String SHOWCASE_FILE_FILESTORE_KEY = "showcaseFile";

	@Autowired
	private IPropertyService propertyService;

	@PostConstruct
	private void init() { // NOSONAR (@PostConstruct)
		registerFileStore(new SimpleFileStoreImpl(SHOWCASE_FILE_FILESTORE_KEY, propertyService.get(SHOWCASE_FILE_ROOT_DIRECTORY), true));
	}

	@Override
	public File getFile(ShowcaseFile showcaseFile) {
		return getFileStore(SHOWCASE_FILE_FILESTORE_KEY).getFile(getFileKey(showcaseFile.getId()), showcaseFile.getExtension());
	}

	@Override
	public FileInformation addFile(ShowcaseFile showcaseFile, InputStream fileInputStream) throws ServiceException, SecurityServiceException {
		return getFileStore(SHOWCASE_FILE_FILESTORE_KEY).addFile(fileInputStream, getFileKey(showcaseFile.getId()), showcaseFile.getExtension());
	}

	@Override
	public void removeFile(ShowcaseFile showcasefile) throws ServiceException, SecurityServiceException {
		getFileStore(SHOWCASE_FILE_FILESTORE_KEY).removeFile(getFileKey(showcasefile.getId()), showcasefile.getExtension());
	}

	private String getFileKey(Long id) {
		return id.toString();
	}

}
