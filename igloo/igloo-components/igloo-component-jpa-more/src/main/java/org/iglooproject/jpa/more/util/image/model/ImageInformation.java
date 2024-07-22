package org.iglooproject.jpa.more.util.image.model;

public class ImageInformation {

  private boolean isSizeDetected = false;

  private Integer width;

  private Integer height;

  private boolean isThumbnailAvailable = false;

  public ImageInformation() {}

  public boolean isSizeDetected() {
    return isSizeDetected;
  }

  public void setSizeDetected(boolean isSizeDetected) {
    this.isSizeDetected = isSizeDetected;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public boolean isThumbnailAvailable() {
    return isThumbnailAvailable;
  }

  public void setThumbnailAvailable(boolean isThumbnailAvailable) {
    this.isThumbnailAvailable = isThumbnailAvailable;
  }
}
