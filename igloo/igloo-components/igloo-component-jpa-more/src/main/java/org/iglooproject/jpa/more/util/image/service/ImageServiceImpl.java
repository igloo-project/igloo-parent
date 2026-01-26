package org.iglooproject.jpa.more.util.image.service;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.IMAGE_MAGICK_CONVERT_BINARY_PATH;

import jakarta.annotation.PostConstruct;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.devlib.schmidt.imageinfo.ImageInfo;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.util.image.exception.ImageThumbnailGenerationException;
import org.iglooproject.jpa.more.util.image.model.ImageInformation;
import org.iglooproject.jpa.more.util.image.model.ImageThumbnailFormat;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imageService")
public class ImageServiceImpl implements IImageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

  private static final int IMAGE_MAGICK_CONVERT_TIMEOUT = 15000;

  private File imageMagickConvertBinary;

  @Autowired private IPropertyService propertyService;

  @PostConstruct
  private void init() {
    imageMagickConvertBinary =
        getImageMagickConvertBinary(propertyService.get(IMAGE_MAGICK_CONVERT_BINARY_PATH));
  }

  @Override
  public ImageInformation getImageInformation(File source) {
    InputStream is = null;
    try {
      is = new FileInputStream(source);

      return getImageInformation(is);
    } catch (FileNotFoundException e) {
      LOGGER.error(
          String.format(
              "File %1$s not found while trying to get image information",
              source.getAbsolutePath()),
          e);

      return new ImageInformation();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
        }
      }
    }
  }

  @Override
  public ImageInformation getImageInformation(InputStream source) {
    ImageInformation imageInformation = new ImageInformation();

    ImageInfo imageInfo = new ImageInfo();
    imageInfo.setInput(source);
    if (imageInfo.check()) {
      imageInformation.setSizeDetected(true);
      imageInformation.setWidth(imageInfo.getWidth());
      imageInformation.setHeight(imageInfo.getHeight());
    }
    return imageInformation;
  }

  @Override
  public void generateThumbnail(File source, File destination, ImageThumbnailFormat thumbnailFormat)
      throws ImageThumbnailGenerationException {
    if (isImageMagickConvertAvailable()) {
      generateThumbnailWithImageMagickConvert(source, destination, thumbnailFormat);
    } else {
      generateThumbnailWithJava(source, destination, thumbnailFormat);
    }
  }

  private File getImageMagickConvertBinary(File imageMagickConvertBinaryCandidate) {
    if (imageMagickConvertBinaryCandidate == null) {
      LOGGER.warn("ImageMagick's convert binary is not configured. Using Java to scale images.");
      return null;
    }

    if (!imageMagickConvertBinaryCandidate.exists()) {
      LOGGER.warn(
          "ImageMagick's convert binary {} does not exist. Using Java to scale images.",
          imageMagickConvertBinaryCandidate.getAbsolutePath());
      return null;
    }

    if (!imageMagickConvertBinaryCandidate.isFile()) {
      LOGGER.warn(
          "ImageMagick's convert binary {} is not a file. Using Java to scale images.",
          imageMagickConvertBinaryCandidate.getAbsolutePath());
      return null;
    }

    if (!imageMagickConvertBinaryCandidate.canExecute()) {
      LOGGER.warn(
          "ImageMagick's convert binary {} is not executable. Using Java to scale images.",
          imageMagickConvertBinaryCandidate.getAbsolutePath());
      return null;
    }

    return imageMagickConvertBinaryCandidate;
  }

  private boolean isImageMagickConvertAvailable() {
    return imageMagickConvertBinary != null;
  }

  private void generateThumbnailWithImageMagickConvert(
      File source, File destination, ImageThumbnailFormat thumbnailFormat)
      throws ImageThumbnailGenerationException {
    try {
      CommandLine commandLine = new CommandLine(imageMagickConvertBinary);
      commandLine.addArgument("-auto-orient");
      commandLine.addArgument("-thumbnail");
      if (thumbnailFormat.isAllowEnlarge()) {
        commandLine.addArgument("${width}x${height}");
      } else {
        commandLine.addArgument("${width}x${height}>");
      }
      commandLine.addArgument("-quality");
      commandLine.addArgument("${quality}");
      commandLine.addArgument("${originalFilePath}");
      commandLine.addArgument("${targetFilePath}");

      Map<String, String> parameters = new HashMap<>();
      parameters.put("width", String.valueOf(thumbnailFormat.getWidth()));
      parameters.put("height", String.valueOf(thumbnailFormat.getHeight()));
      parameters.put("quality", String.valueOf(thumbnailFormat.getQuality()));
      parameters.put("originalFilePath", source.getAbsolutePath());
      parameters.put("targetFilePath", destination.getAbsolutePath());

      commandLine.setSubstitutionMap(parameters);

      DefaultExecutor executor = DefaultExecutor.builder().get();
      ExecuteWatchdog watchdog =
          ExecuteWatchdog.builder()
              .setTimeout(Duration.ofMillis(IMAGE_MAGICK_CONVERT_TIMEOUT))
              .get();
      executor.setWatchdog(watchdog);
      executor.execute(commandLine);
    } catch (RuntimeException | IOException e) {
      throw new ImageThumbnailGenerationException(
          String.format("Unable to generate a thumbnail for file %1$s", source.getAbsolutePath()),
          e);
    }
  }

  private void generateThumbnailWithJava(
      File source, File destination, ImageThumbnailFormat thumbnailFormat)
      throws ImageThumbnailGenerationException {
    try {
      BufferedImage originalImage = ImageIO.read(source);
      if (originalImage == null) {
        throw new ServiceException("Image cannot be read.");
      }
      int type =
          (originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType());

      int originalImageWidth = originalImage.getWidth();
      int originalImageHeight = originalImage.getHeight();

      double widthRatio = (double) thumbnailFormat.getWidth() / (double) originalImageWidth;
      double heightRatio = (double) thumbnailFormat.getHeight() / (double) originalImageHeight;

      if (widthRatio < 1.0d || heightRatio < 1.0d) {
        double ratio = Math.min(widthRatio, heightRatio);

        int resizedImageWidth = (int) (ratio * originalImageWidth);
        int resizedImageHeight = (int) (ratio * originalImageHeight);

        BufferedImage resizedImage = new BufferedImage(resizedImageWidth, resizedImageHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(
            RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform tx = new AffineTransform();
        tx.scale(ratio, ratio);

        g.drawImage(originalImage, 0, 0, resizedImageWidth, resizedImageHeight, null);
        g.dispose();

        FileImageOutputStream outputStream = null;

        try {
          Iterator<ImageWriter> imageWritersIterator =
              ImageIO.getImageWritersByFormatName(
                  thumbnailFormat.getJavaFormatName(
                      FilenameUtils.getExtension(destination.getName()).toLowerCase(Locale.ROOT)));
          ImageWriter writer = imageWritersIterator.next();

          ImageWriteParam iwp = writer.getDefaultWriteParam();

          if (iwp.canWriteCompressed()) {
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(thumbnailFormat.getQuality() / 100f);
          }

          outputStream = new FileImageOutputStream(destination);
          writer.setOutput(outputStream);
          IIOImage image = new IIOImage(resizedImage, null, null);
          writer.write(null, image, iwp);
          writer.dispose();
        } catch (RuntimeException | IOException e) {
          throw new ServiceException(e);
        } finally {
          if (outputStream != null) {
            outputStream.close();
          }
        }
      } else {
        FileUtils.copyFile(source, destination);
      }
    } catch (RuntimeException | IOException | ServiceException e) {
      throw new ImageThumbnailGenerationException(
          String.format("Unable to generate a thumbnail for file %1$s", source.getAbsolutePath()),
          e);
    }
  }
}
