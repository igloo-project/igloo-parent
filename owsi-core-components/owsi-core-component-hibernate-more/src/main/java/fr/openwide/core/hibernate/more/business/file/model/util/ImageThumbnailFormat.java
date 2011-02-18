package fr.openwide.core.hibernate.more.business.file.model.util;

import java.util.regex.Pattern;

public class ImageThumbnailFormat {
	
	private static final Pattern NAME_PATTERN = Pattern.compile("[a-z0-9_-]+", Pattern.CASE_INSENSITIVE);
	
	public static final String EXTENSION_JPG = "jpg";
	
	public static final String EXTENSION_PNG = "png";
	
	private String name;
	
	private String extension;
	
	private int width;
	
	private int height;
	
	private int quality;
	
	public ImageThumbnailFormat(String name, int width, int height) {
		this(name, width, height, 80, EXTENSION_JPG);
	}
	
	public ImageThumbnailFormat(String name, int width, int height, int quality, String extension) {
		if (!NAME_PATTERN.matcher(name).matches()) {
			throw new IllegalArgumentException("Thumbnail format name must respect the following pattern: '[a-z0-9_-]+'.");
		}
		
		this.name = name;
		this.width = width;
		this.height = height;
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
	
	public int getQuality() {
		return quality;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String getJavaFormatName() {
		return extension;
	}

}