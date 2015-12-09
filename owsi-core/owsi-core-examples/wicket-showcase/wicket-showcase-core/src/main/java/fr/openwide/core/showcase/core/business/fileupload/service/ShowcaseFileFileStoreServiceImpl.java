package fr.openwide.core.showcase.core.business.fileupload.service;

import java.io.File;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.file.model.FileInformation;
import fr.openwide.core.jpa.more.business.file.model.SimpleFileStoreImpl;
import fr.openwide.core.jpa.more.business.file.service.AbstractFileStoreServiceImpl;
import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;
import fr.openwide.core.showcase.core.util.spring.ShowcaseConfigurer;

@Service("showcaseFileFileStoreService")
public class ShowcaseFileFileStoreServiceImpl extends AbstractFileStoreServiceImpl implements IShowcaseFileFileStoreService {

	private static final String SHOWCASE_FILE_FILESTORE_KEY = "showcaseFile";

	@Autowired
	private ShowcaseConfigurer configurer;

	@PostConstruct
	private void init() { // NOSONAR (@PostConstruct)
		registerFileStore(new SimpleFileStoreImpl(SHOWCASE_FILE_FILESTORE_KEY, configurer.getShowcaseFileFileStoreRootDirectory(), true));
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
