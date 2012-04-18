package fr.openwide.core.jpa.more.util.image.model;

public class ImageInformation {
	
	private boolean isFormatSupported;
	
	private int width;
	
	private int height;
	
	public ImageInformation() {
	}

	public boolean isFormatSupported() {
		return isFormatSupported;
	}

	public void setFormatSupported(boolean isFormatSupported) {
		this.isFormatSupported = isFormatSupported;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
