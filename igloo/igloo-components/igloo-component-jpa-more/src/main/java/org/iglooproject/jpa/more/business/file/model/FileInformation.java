package org.iglooproject.jpa.more.business.file.model;

import java.io.File;
import java.util.Date;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.more.util.image.model.ImageInformation;

public class FileInformation {

  private String name;

  private String extension;

  private long size;

  private Date lastModifiedDate;

  private boolean isImage = false;

  private boolean isImageSizeDetected = false;

  private Integer imageWidth;

  private Integer imageHeight;

  private boolean isImageThumbnailAvailable = false;

  public FileInformation() {}

  public FileInformation(File file, String extension) {
    if (file != null && file.canRead()) {
      setName(file.getName());
      setExtension(extension);
      setSize(file.length());
      setLastModifiedDate(new Date(file.lastModified()));
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public Date getLastModifiedDate() {
    return CloneUtils.clone(lastModifiedDate);
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = CloneUtils.clone(lastModifiedDate);
  }

  public boolean isImage() {
    return isImage;
  }

  public void setImage(boolean isImage) {
    this.isImage = isImage;
  }

  public boolean isImageSizeDetected() {
    return isImageSizeDetected;
  }

  public void setImageSizeDetected(boolean isSizeDetected) {
    this.isImageSizeDetected = isSizeDetected;
  }

  public Integer getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(Integer width) {
    this.imageWidth = width;
  }

  public Integer getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(Integer height) {
    this.imageHeight = height;
  }

  public boolean isImageThumbnailAvailable() {
    return isImageThumbnailAvailable;
  }

  public void setImageThumbnailAvailable(boolean isImageThumbnailAvailable) {
    this.isImageThumbnailAvailable = isImageThumbnailAvailable;
  }

  public void addImageInformation(ImageInformation imageInformation) {
    this.isImage = true;
    this.isImageSizeDetected = imageInformation.isSizeDetected();
    this.imageWidth = imageInformation.getWidth();
    this.imageHeight = imageInformation.getHeight();
    this.isImageThumbnailAvailable = imageInformation.isThumbnailAvailable();
  }
}
