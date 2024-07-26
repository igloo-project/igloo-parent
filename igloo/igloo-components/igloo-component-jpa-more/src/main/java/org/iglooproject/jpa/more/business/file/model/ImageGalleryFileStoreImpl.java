package org.iglooproject.jpa.more.business.file.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.file.model.path.IImageGalleryFileStorePathGenerator;
import org.iglooproject.jpa.more.business.file.model.path.SimpleFileStorePathGeneratorImpl;
import org.iglooproject.jpa.more.util.image.exception.ImageThumbnailGenerationException;
import org.iglooproject.jpa.more.util.image.model.ImageInformation;
import org.iglooproject.jpa.more.util.image.model.ImageThumbnailFormat;
import org.iglooproject.jpa.more.util.image.service.IImageService;
import org.iglooproject.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ImageGalleryFileStoreImpl extends SimpleFileStoreImpl {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageGalleryFileStoreImpl.class);

  private final IImageGalleryFileStorePathGenerator pathGenerator;

  private List<ImageThumbnailFormat> thumbnailFormats;

  @Autowired private IImageService imageService;

  public ImageGalleryFileStoreImpl(
      String key, String rootDirectoryPath, List<ImageThumbnailFormat> thumbnailFormats) {
    this(key, rootDirectoryPath, new SimpleFileStorePathGeneratorImpl(), thumbnailFormats);
  }

  public ImageGalleryFileStoreImpl(
      String key,
      String rootDirectoryPath,
      IImageGalleryFileStorePathGenerator pathGenerator,
      List<ImageThumbnailFormat> thumbnailFormats) {
    super(key, rootDirectoryPath, pathGenerator, true);

    this.pathGenerator = pathGenerator;

    this.thumbnailFormats = thumbnailFormats;
  }

  @Override
  public FileInformation addFile(byte[] fileContent, String fileKey, String extension)
      throws ServiceException, SecurityServiceException {
    FileInformation information =
        super.addFile(fileContent, fileKey, extension.toLowerCase(Locale.ROOT));

    addFileToGallery(information, fileKey, extension.toLowerCase(Locale.ROOT));

    return information;
  }

  @Override
  public FileInformation addFile(File file, String fileKey, String extension)
      throws ServiceException, SecurityServiceException {
    FileInformation information = super.addFile(file, fileKey, extension);

    addFileToGallery(information, fileKey, extension.toLowerCase(Locale.ROOT));

    return information;
  }

  @Override
  public FileInformation addFile(InputStream inputStream, String fileKey, String extension)
      throws ServiceException, SecurityServiceException {
    FileInformation information = super.addFile(inputStream, fileKey, extension);

    addFileToGallery(information, fileKey, extension.toLowerCase(Locale.ROOT));

    return information;
  }

  protected FileInformation addFileToGallery(
      FileInformation fileInformation, String fileKey, String extension)
      throws ImageThumbnailGenerationException {
    ImageInformation imageInformation =
        imageService.getImageInformation(getFile(fileKey, extension));
    fileInformation.addImageInformation(imageInformation);

    generateThumbnails(fileKey, extension);
    fileInformation.setImageThumbnailAvailable(true);

    return fileInformation;
  }

  protected void generateThumbnails(String fileKey, String extension)
      throws ImageThumbnailGenerationException {
    for (ImageThumbnailFormat thumbnailFormat : thumbnailFormats) {
      String cleanExtension = extension.toLowerCase(Locale.ROOT);
      String thumbnailFilePath = getThumbnailFilePath(fileKey, cleanExtension, thumbnailFormat);
      String dirPath = FilenameUtils.getFullPathNoEndSeparator(thumbnailFilePath);
      if (StringUtils.hasLength(dirPath)) {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
          try {
            FileUtils.forceMkdir(dir);
          } catch (IOException e) {
            throw new ImageThumbnailGenerationException(e);
          }
        }
      }

      imageService.generateThumbnail(
          getFile(fileKey, cleanExtension),
          getThumbnailFile(fileKey, cleanExtension, thumbnailFormat),
          thumbnailFormat);
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

  public File getThumbnailFile(
      String fileKey, String extension, ImageThumbnailFormat thumbnailFormat) {
    return new File(getThumbnailFilePath(fileKey, extension, thumbnailFormat));
  }

  protected String getThumbnailFilePath(
      String fileKey, String extension, ImageThumbnailFormat thumbnailFormat) {
    return FilenameUtils.concat(
        getRootDirectoryPath(),
        pathGenerator.getThumbnailFilePath(fileKey, extension, thumbnailFormat));
  }
}
