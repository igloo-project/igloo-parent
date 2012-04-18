package fr.openwide.core.jpa.more.util.image.model;

public class ImageInformation {
	
	private boolean isFormatSupported = false;
	
	private Integer width;
	
	private Integer height;
	
	public ImageInformation() {
	}

	public boolean isFormatSupported() {
		return isFormatSupported;
	}

	public void setFormatSupported(boolean isFormatSupported) {
		this.isFormatSupported = isFormatSupported;
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

}
