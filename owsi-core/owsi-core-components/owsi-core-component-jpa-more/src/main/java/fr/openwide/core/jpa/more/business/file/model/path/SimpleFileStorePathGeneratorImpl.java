package fr.openwide.core.jpa.more.business.file.model.path;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.more.util.image.model.ImageThumbnailFormat;


public class SimpleFileStorePathGeneratorImpl implements IFileStorePathGenerator, IImageGalleryFileStorePathGenerator {

	@Override
	public String getFilePath(String fileKey, String extension) {
		StringBuilder filePath = new StringBuilder(getFileKeyPath(fileKey));
		if (StringUtils.hasText(extension)) {
			filePath.append(FilenameUtils.EXTENSION_SEPARATOR).append(extension);
		}
		
		return filePath.toString();
	}

	@Override
	public String getThumbnailFilePath(String fileKey, String extension, ImageThumbnailFormat thumbnailFormat) {
		StringBuilder thumbnailFilePath = new StringBuilder(getFileKeyPath(fileKey));
		thumbnailFilePath.append("-");
		thumbnailFilePath.append(thumbnailFormat.getName());
		if (StringUtils.hasText(extension)) {
			thumbnailFilePath.append(FilenameUtils.EXTENSION_SEPARATOR).append(extension);
		}
		
		return thumbnailFilePath.toString();
	}
	
	protected String getFileKeyPath(String fileKey) {
		return fileKey;
	}

}
