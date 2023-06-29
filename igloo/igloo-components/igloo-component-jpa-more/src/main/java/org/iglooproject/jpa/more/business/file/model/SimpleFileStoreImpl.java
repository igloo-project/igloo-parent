package org.iglooproject.jpa.more.business.file.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.file.model.path.IFileStorePathGenerator;
import org.iglooproject.jpa.more.business.file.model.path.SimpleFileStorePathGeneratorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


public class SimpleFileStoreImpl implements IFileStore {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleFileStoreImpl.class);
	
	private String key;
	
	private String rootDirectoryPath;
	
	private boolean writable;
	
	private final IFileStorePathGenerator pathGenerator;
	
	public SimpleFileStoreImpl(String key, String rootDirectoryPath, boolean writable) {
		this(key, rootDirectoryPath, new SimpleFileStorePathGeneratorImpl(), writable);
	}
	
	public SimpleFileStoreImpl(String key, String rootDirectoryPath, IFileStorePathGenerator pathGenerator, boolean writable) {
		this.key = key;
		this.rootDirectoryPath = rootDirectoryPath;
		this.writable = writable;
		this.pathGenerator = pathGenerator;
	}
	
	@Override
	public String getKey() {
		return key;
	}
	
	@Override
	public FileInformation addFile(byte[] content, String fileKey, String extension) throws ServiceException, SecurityServiceException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		return addFile(inputStream, fileKey, extension);
	}
	
	@Override
	public FileInformation addFile(File file, String fileKey, String extension) throws ServiceException, SecurityServiceException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			return addFile(fileInputStream, fileKey, extension);
		} catch (RuntimeException | IOException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public FileInformation addFile(InputStream inputStream, String fileKey, String extension)
			throws ServiceException, SecurityServiceException {
		File outputFile = null;
		
		try {
			String filePath = getFilePath(fileKey, extension);
			String dirPath = FilenameUtils.getFullPathNoEndSeparator(filePath);
			if (StringUtils.hasLength(dirPath)) {
				File dir = new File(dirPath);
				if (!dir.isDirectory()) {
					FileUtils.forceMkdir(dir);
				}
			}
			
			outputFile = new File(filePath);
			
			try (InputStream myInputStream = inputStream; OutputStream outputStream = createOutputStream(outputFile)) {
				IOUtils.copy(myInputStream, outputStream);
			}
		} catch (RuntimeException | IOException e) {
			throw new ServiceException(e);
		}
		
		String cleanExtension = extension == null ? null : extension.toLowerCase(Locale.ROOT);
		FileInformation fileInformation = new FileInformation(outputFile, cleanExtension);
		
		return fileInformation;
	}
	
	protected OutputStream createOutputStream(File outputFile) throws IOException {
		return new FileOutputStream(outputFile);
	}

	@Override
	public void removeFile(String fileKey, String extension) {
		File file = getFile(fileKey, extension);
		if (!file.delete()) {
			LOGGER.error(String.format("Error removing file %1$s (key: %2$s; extension: %3$s)",
					getFile(fileKey, extension).getAbsolutePath(),
					fileKey,
					extension));
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
			} catch (RuntimeException | IOException e) {
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
		String cleanExtension = extension == null ? null : extension.toLowerCase(Locale.ROOT);
		return FilenameUtils.concat(rootDirectoryPath, pathGenerator.getFilePath(fileKey, cleanExtension));
	}
	
	protected String getRootDirectoryPath() {
		return rootDirectoryPath;
	}

}
