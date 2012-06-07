package fr.openwide.core.jpa.more.util.image.model;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.mime.MediaType;

public class ImageThumbnailFormat {
	
	private static final Pattern NAME_PATTERN = Pattern.compile("[a-z0-9_-]+", Pattern.CASE_INSENSITIVE);
	
	private static final List<String> AUTHORIZED_THUMBNAIL_EXTENSIONS = Lists.newArrayList(
			MediaType.IMAGE_JPEG.extension(),
			MediaType.IMAGE_GIF.extension(),
			MediaType.IMAGE_PNG.extension()
	);
	
	private static final String DEFAULT_THUMBNAIL_EXTENSION = MediaType.IMAGE_JPEG.extension();
	
	private String name;
	
	/**
	 * A n'utiliser que si on veut forcer l'extension, sinon on conserve l'extension du fichier original
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
	
	public ImageThumbnailFormat(String name, int width, int height, boolean allowEnlarge, int quality, String extension) {
		if (!NAME_PATTERN.matcher(name).matches()) {
			throw new IllegalArgumentException("Thumbnail format name must respect the following pattern: '[a-z0-9_-]+'.");
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
			if (AUTHORIZED_THUMBNAIL_EXTENSIONS.contains(originalFileExtension.toLowerCase(Locale.ROOT))) {
				thumbnailExtension = originalFileExtension;
			} else {
				thumbnailExtension = DEFAULT_THUMBNAIL_EXTENSION;
			}
		}
		return thumbnailExtension.toLowerCase(Locale.ROOT);
	}
	
	public String getJavaFormatName(String originalFileExtension) {
		return getExtension(originalFileExtension);
	}

}