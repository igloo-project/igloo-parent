package org.iglooproject.jpa.more.business.file.model.path;

import org.iglooproject.jpa.more.util.image.model.ImageThumbnailFormat;

public interface IImageGalleryFileStorePathGenerator extends IFileStorePathGenerator {

  String getThumbnailFilePath(
      String fileKey, String extension, ImageThumbnailFormat thumbnailFormat);
}
