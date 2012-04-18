package fr.openwide.core.jpa.more.business.file.model;

import fr.openwide.core.jpa.more.util.image.model.ImageInformation;

public class FileInformation {
	
	private long size;
	
	private boolean isImage = false;
	
	private boolean isImageFormatSupported = false;
	
	private Integer imageWidth;
	
	private Integer imageHeight;
	
	public FileInformation() {
	}
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isImage() {
		return isImage;
	}

	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}

	public boolean isImageFormatSupported() {
		return isImageFormatSupported;
	}

	public void setImageFormatSupported(boolean isImageFormatSupported) {
		this.isImageFormatSupported = isImageFormatSupported;
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
	
	public void addImageInformation(ImageInformation imageInformation) {
		this.isImage = true;
		this.isImageFormatSupported = imageInformation.isFormatSupported();
		this.imageWidth = imageInformation.getWidth();
		this.imageHeight = imageInformation.getHeight();
	}

}
