package fr.openwide.core.hibernate.more.business.file.model;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.more.business.file.model.util.ImageThumbnailFormat;

public class ImageGalleryFileStoreImpl extends SimpleFileStoreImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageGalleryFileStoreImpl.class);
	
	private static final int IMAGE_MAGICK_CONVERT_TIMEOUT = 5000;
	
	private File imageMagickConvertBinary;
	
	private List<ImageThumbnailFormat> thumbnailFormats;
	
	public ImageGalleryFileStoreImpl(String key, String rootDirectoryPath, List<ImageThumbnailFormat> thumbnailFormats,
			File imageMagickConvertBinaryCandidate) {
		super(key, rootDirectoryPath, true);
		
		this.thumbnailFormats = thumbnailFormats;
		this.imageMagickConvertBinary = getImageMagickConvertBinary(imageMagickConvertBinaryCandidate);
	}
	
	@Override
	public void addFile(File file, String fileKey, String extension) throws ServiceException, SecurityServiceException {
		super.addFile(file, fileKey, extension);
		
		for (ImageThumbnailFormat thumbnailFormat : thumbnailFormats) {
			if (isImageMagickConvertAvailable()) {
				generateThumbnailWithImageMagickConvert(getFile(fileKey, extension), fileKey, extension, thumbnailFormat);
			} else {
				generateThumbnailWithJava(getFile(fileKey, extension), fileKey, extension, thumbnailFormat);
			}
		}
	}
	
	@Override
	public void removeFile(String fileKey, String extension) {
		for (ImageThumbnailFormat thumbnailFormat : thumbnailFormats) {
			if (!getThumbnailFile(fileKey, extension, thumbnailFormat).delete()) {
				LOGGER.error("Error removing thumbnail file " + fileKey + " " + extension + " " + thumbnailFormat);
			}
		}
		super.removeFile(fileKey, extension);
	}

	private File getImageMagickConvertBinary(File imageMagickConvertBinaryCandidate) {
		if (imageMagickConvertBinaryCandidate == null) {
			LOGGER.warn("ImageMagick's convert is not configured. Using Java to scale images.");
			return null;
		}
		
		if (!imageMagickConvertBinaryCandidate.exists()) {
			LOGGER.warn("ImageMagick's convert binary {} does not exist. Using Java to scale images.",
					imageMagickConvertBinaryCandidate.getAbsolutePath());
			return null;
		}
		
		if (!imageMagickConvertBinaryCandidate.canExecute()) {
			LOGGER.warn("ImageMagick's convert binary {} is not executable. Using Java to scale images.",
					imageMagickConvertBinaryCandidate.getAbsolutePath());
			return null;
		}
		
		return imageMagickConvertBinaryCandidate;
	}
	
	private boolean isImageMagickConvertAvailable() {
		if (imageMagickConvertBinary != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private void generateThumbnailWithImageMagickConvert(File file, String fileKey, String extension, ImageThumbnailFormat thumbnailFormat)
			throws ServiceException, SecurityServiceException {
		try {
			CommandLine commandLine = new CommandLine(imageMagickConvertBinary);
			commandLine.addArgument("-resize");
				commandLine.addArgument("${width}x${height}>");
			commandLine.addArgument("-quality");
				commandLine.addArgument("${quality}");
			commandLine.addArgument("${originalFilePath}");
			commandLine.addArgument("${targetFilePath}");
			
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("width", String.valueOf(thumbnailFormat.getWidth()));
			parameters.put("height", String.valueOf(thumbnailFormat.getHeight()));
			parameters.put("quality", String.valueOf(thumbnailFormat.getQuality()));
			parameters.put("originalFilePath", file.getAbsolutePath());
			parameters.put("targetFilePath", getThumbnailFilePath(fileKey, extension, thumbnailFormat));
			
			commandLine.setSubstitutionMap(parameters);
			
			DefaultExecutor executor = new DefaultExecutor();
			ExecuteWatchdog watchdog = new ExecuteWatchdog(IMAGE_MAGICK_CONVERT_TIMEOUT);
			executor.setWatchdog(watchdog);
			executor.execute(commandLine);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private void generateThumbnailWithJava(File file, String fileKey, String extension, ImageThumbnailFormat thumbnailFormat)
			throws ServiceException, SecurityServiceException {
		try {
			BufferedImage originalImage = ImageIO.read(getFile(fileKey, extension));
			int type = (originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType());
			
			int originalImageWidth = originalImage.getWidth();
			int originalImageHeight = originalImage.getHeight();
			
			double widthRatio = (double) thumbnailFormat.getWidth() / (double) originalImageWidth;
			double heightRatio = (double)  thumbnailFormat.getHeight() /(double) originalImageHeight;
			
			if (widthRatio < 1.0d || heightRatio < 1.0d) {
				double ratio = Math.min(widthRatio, heightRatio);
				
				int resizedImageWidth = (int) (ratio * originalImageWidth);
				int resizedImageHeight = (int) (ratio * originalImageHeight);
				
				BufferedImage resizedImage = new BufferedImage(resizedImageWidth, resizedImageHeight, type);
				Graphics2D g = resizedImage.createGraphics();
				g.setComposite(AlphaComposite.Src);
				
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				
				AffineTransform tx = new AffineTransform();
				tx.scale(ratio, ratio);
				
				g.drawImage(originalImage, 0, 0, resizedImageWidth, resizedImageHeight, null);
				g.dispose();
				
				FileImageOutputStream outputStream = null;
				
				try {
					Iterator<ImageWriter> imageWritersIterator = ImageIO.getImageWritersByFormatName(thumbnailFormat.getJavaFormatName());
					ImageWriter writer = imageWritersIterator.next();
					
					ImageWriteParam iwp = writer.getDefaultWriteParam();
	
					iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					iwp.setCompressionQuality(thumbnailFormat.getQuality() / 100f);
	
					outputStream = new FileImageOutputStream(getThumbnailFile(fileKey, extension, thumbnailFormat));
					writer.setOutput(outputStream);
					IIOImage image = new IIOImage(resizedImage, null, null);
					writer.write(null, image, iwp);
					writer.dispose();
				} catch (Exception e) {
					throw new ServiceException(e);
				} finally {
					if (outputStream != null) {
						outputStream.close();
					}
				}
			} else {
				FileUtils.copyFile(getFile(fileKey, extension), getThumbnailFile(fileKey, extension, thumbnailFormat));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
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
		fileName.append(thumbnailFormat.getExtension());
		
		return FilenameUtils.concat(getRootDirectoryPath(), fileName.toString());
	}

}
