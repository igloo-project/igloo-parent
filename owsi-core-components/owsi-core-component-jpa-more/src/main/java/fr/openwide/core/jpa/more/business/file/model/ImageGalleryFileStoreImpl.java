package fr.openwide.core.jpa.more.business.file.model;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.image.exception.ImageThumbnailGenerationException;
import fr.openwide.core.jpa.more.util.image.model.ImageInformation;
import fr.openwide.core.jpa.more.util.image.model.ImageThumbnailFormat;
import fr.openwide.core.jpa.more.util.image.service.IImageService;

public class ImageGalleryFileStoreImpl extends SimpleFileStoreImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageGalleryFileStoreImpl.class);
	
	private List<ImageThumbnailFormat> thumbnailFormats;
	
	@Autowired
	private IImageService imageService;
	
	public ImageGalleryFileStoreImpl(String key, String rootDirectoryPath, List<ImageThumbnailFormat> thumbnailFormats) {
		super(key, rootDirectoryPath, true);
		
		this.thumbnailFormats = thumbnailFormats;
	}
	
	@Override
	public FileInformation addFile(byte[] fileContent, String fileKey, String extension) throws ServiceException,
			SecurityServiceException {
		FileInformation information = super.addFile(fileContent, fileKey, extension.toLowerCase(Locale.ROOT));
		
		addFileToGallery(information, fileKey, extension.toLowerCase(Locale.ROOT));
		
		return information;
	}
	
	@Override
	public FileInformation addFile(File file, String fileKey, String extension) throws ServiceException, SecurityServiceException {
		FileInformation information = super.addFile(file, fileKey, extension);
		
		addFileToGallery(information, fileKey, extension.toLowerCase(Locale.ROOT));
		
		return information;
	}
	
	@Override
	public FileInformation addFile(InputStream inputStream, String fileKey, String extension) throws ServiceException,
			SecurityServiceException {
		FileInformation information = super.addFile(inputStream, fileKey, extension);
		
		addFileToGallery(information, fileKey, extension.toLowerCase(Locale.ROOT));
		
		return information;
	}
	
	protected FileInformation addFileToGallery(FileInformation fileInformation, String fileKey, String extension)
			throws ImageThumbnailGenerationException {
		ImageInformation imageInformation = imageService.getImageInformation(getFile(fileKey, extension));
		fileInformation.addImageInformation(imageInformation);
		
		generateThumbnails(fileKey, extension);
		fileInformation.setImageThumbnailAvailable(true);
		
		return fileInformation;
	}
	
	protected void generateThumbnails(String fileKey, String extension) throws ImageThumbnailGenerationException {
		for (ImageThumbnailFormat thumbnailFormat : thumbnailFormats) {
			imageService.generateThumbnail(getFile(fileKey, extension),
					getThumbnailFile(fileKey, extension, thumbnailFormat), thumbnailFormat);
		}
	}
	
	@Override
	public void removeFile(String fileKey, String extension) {
		for (ImageThumbnailFormat thumbnailFormat : thumbnailFormats) {
			File thumbnailFile = getThumbnailFile(fileKey, extension, thumbnailFormat);
			if (!thumbnailFile.delete()) {
				LOGGER.error("Error removing thumbnail file " + thumbnailFile.getAbsolutePath());
			}
		}
		super.removeFile(fileKey, extension);
	}
	
	public File getThumbnailFile(String fileKey, String extension, ImageThumbnailFormat thumbnailFormat) {
		return new File(getThumbnailFilePath(fileKey, extension, thumbnailFormat));
	}
	
	protected String getThumbnailFilePath(String fileKey, String extension, ImageThumbnailFormat thumbnailFormat) {
		StringBuilder fileName = new StringBuilder();
		fileName.append(fileKey);
		fileName.append("-");
		fileName.append(thumbnailFormat.getName());
		fileName.append(".");
		fileName.append(thumbnailFormat.getExtension(extension));
		
		return FilenameUtils.concat(getRootDirectoryPath(), fileName.toString());
	}

}
