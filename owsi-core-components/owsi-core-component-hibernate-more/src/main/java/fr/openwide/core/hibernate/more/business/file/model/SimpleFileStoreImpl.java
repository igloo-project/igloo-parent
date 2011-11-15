package fr.openwide.core.hibernate.more.business.file.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import de.schlichtherle.io.FileOutputStream;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;


public class SimpleFileStoreImpl implements FileStore {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFileStoreImpl.class);
	
	private String key;
	
	private String rootDirectoryPath;
	
	private boolean writable;
	
	public SimpleFileStoreImpl(String key, String rootDirectoryPath, boolean writable) {
		this.key = key;
		this.rootDirectoryPath = rootDirectoryPath;
		this.writable = writable;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public void addFile(byte[] content, String fileKey, String extension) throws ServiceException, SecurityServiceException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		addFile(inputStream, fileKey, extension);
	}
	
	@Override
	public void addFile(File file, String fileKey, String extension) throws ServiceException, SecurityServiceException {
		de.schlichtherle.io.FileInputStream fileInputStream = null;
		try {
			// Attention le fichier peut être contenu dans un zip d'où cette
			// manipulation spécifique.
			fileInputStream = new de.schlichtherle.io.FileInputStream(file);
			addFile(fileInputStream, fileKey, extension);
		} catch (Exception e) {
			if (!(e instanceof ServiceException)) {
				throw new ServiceException(e);
			} else {
				throw (ServiceException) e;
			}
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
		}
	}
	
	protected void addFile(InputStream inputStream, String fileKey, String extension) throws ServiceException {
		FileOutputStream outputStream = null;
		try {
			String filePath = getFilePath(fileKey, extension);
			String dirPath = FilenameUtils.getFullPathNoEndSeparator(filePath);
			if (StringUtils.hasLength(dirPath)) {
				File dir = new File(dirPath);
				if (!dir.isDirectory()) {
					FileUtils.forceMkdir(dir);
				}
			}
			
			outputStream = new FileOutputStream(new File(filePath));
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
			
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					throw new ServiceException(e);
				}
			}
		}
	}
	
	@Override
	public void removeFile(String fileKey, String extension) {
		if(!getFile(fileKey, extension).delete()) {
			LOGGER.error("Error removing file " + fileKey + " " + extension);
		}
	}
	
	@Override
	public File getFile(String fileKey, String extension) {
		return new File(getFilePath(fileKey, extension));
	}
	
	@Override
	public void check() throws IllegalStateException {
		if (!StringUtils.hasText(rootDirectoryPath)) {
			throw new IllegalStateException("The root directory path is null or empty.");
		}
		
		File directory = new File(rootDirectoryPath);
		if (!directory.isDirectory()) {
			try {
				FileUtils.forceMkdir(directory);
			} catch (Exception e) {
				throw new IllegalStateException("The directory " + rootDirectoryPath + " does not exist and we are unable to create it.");
			}
		}
		if (!directory.canRead()) {
			throw new IllegalStateException("The directory " + rootDirectoryPath + " exists but is not readable.");
		}
		if (writable && !directory.canWrite()) {
			throw new IllegalStateException("The directory " + rootDirectoryPath + " exists but should be writable and is not.");
		}
	}
	
	protected String getFilePath(String fileKey, String extension) {
		return FilenameUtils.concat(rootDirectoryPath, fileKey + "." + extension);
	}
	
	protected String getRootDirectoryPath() {
		return rootDirectoryPath;
	}

}
