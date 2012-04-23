package fr.openwide.core.jpa.more.util.image.exception;

import fr.openwide.core.jpa.exception.ServiceException;

public class ImageThumbnailGenerationException extends ServiceException {

	private static final long serialVersionUID = -4607736032731104009L;

	public ImageThumbnailGenerationException() {
		super();
	}

	public ImageThumbnailGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageThumbnailGenerationException(String message) {
		super(message);
	}

	public ImageThumbnailGenerationException(Throwable cause) {
		super(cause);
	}
	
}
