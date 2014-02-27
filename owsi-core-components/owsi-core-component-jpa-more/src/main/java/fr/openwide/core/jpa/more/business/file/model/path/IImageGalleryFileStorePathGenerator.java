package fr.openwide.core.jpa.more.business.file.model.path;

import fr.openwide.core.jpa.more.util.image.model.ImageThumbnailFormat;

public interface IImageGalleryFileStorePathGenerator extends IFileStorePathGenerator {
	
	String getThumbnailFilePath(String fileKey, String extension, ImageThumbnailFormat thumbnailFormat);

}
