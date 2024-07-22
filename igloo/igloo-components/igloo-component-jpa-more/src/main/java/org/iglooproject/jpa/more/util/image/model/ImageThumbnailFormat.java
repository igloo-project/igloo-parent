package org.iglooproject.jpa.more.util.image.model;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import org.iglooproject.commons.util.mime.MediaType;
import org.iglooproject.spring.util.StringUtils;

public class ImageThumbnailFormat implements Serializable {

  private static final long serialVersionUID = -2325299250310910619L;

  private static final Pattern NAME_PATTERN =
      Pattern.compile("[a-z0-9_-]+", Pattern.CASE_INSENSITIVE);

  private static final List<String> AUTHORIZED_THUMBNAIL_EXTENSIONS =
      Lists.newArrayList(
          MediaType.IMAGE_JPEG.extension(),
          MediaType.IMAGE_GIF.extension(),
          MediaType.IMAGE_PNG.extension());

  private static final String DEFAULT_THUMBNAIL_EXTENSION = MediaType.IMAGE_JPEG.extension();

  private String name;

  /**
   * A n'utiliser que si on veut forcer l'extension, sinon on conserve l'extension du fichier
   * original
   */
  private String extension;

  private int width;

  private int height;

  private int quality;

  private boolean allowEnlarge;

  public ImageThumbnailFormat(String name, int width, int height) {
    this(name, width, height, false, 80, null);
  }

  public ImageThumbnailFormat(String name, int width, int height, boolean allowEnlarge) {
    this(name, width, height, allowEnlarge, 80, null);
  }

  public ImageThumbnailFormat(String name, int width, int height, int quality, String extension) {
    this(name, width, height, false, quality, extension);
  }

  public ImageThumbnailFormat(
      String name, int width, int height, boolean allowEnlarge, int quality, String extension) {
    if (!NAME_PATTERN.matcher(name).matches()) {
      throw new IllegalArgumentException(
          "Thumbnail format name must respect the following pattern: '[a-z0-9_-]+'.");
    }

    this.name = name;
    this.width = width;
    this.height = height;
    this.allowEnlarge = allowEnlarge;
    this.quality = quality;
    this.extension = extension;
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isAllowEnlarge() {
    return allowEnlarge;
  }

  public int getQuality() {
    return quality;
  }

  public String getExtension(String originalFileExtension) {
    String thumbnailExtension = extension;

    if (thumbnailExtension == null) {
      if (AUTHORIZED_THUMBNAIL_EXTENSIONS.contains(StringUtils.lowerCase(originalFileExtension))) {
        thumbnailExtension = originalFileExtension;
      } else {
        thumbnailExtension = DEFAULT_THUMBNAIL_EXTENSION;
      }
    }
    return StringUtils.lowerCase(thumbnailExtension);
  }

  public String getJavaFormatName(String originalFileExtension) {
    return getExtension(originalFileExtension);
  }
}
