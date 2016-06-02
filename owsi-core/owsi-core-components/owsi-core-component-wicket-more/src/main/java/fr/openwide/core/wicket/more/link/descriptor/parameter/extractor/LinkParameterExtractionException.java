package fr.openwide.core.wicket.more.link.descriptor.parameter.extractor;

import fr.openwide.core.wicket.more.link.descriptor.LinkException;

public class LinkParameterExtractionException extends LinkException {

	private static final long serialVersionUID = 1755131619089627896L;

	public LinkParameterExtractionException(String message) {
		super(message);
	}

	/**
	 * @deprecated Use {@link #LinkParameterExtractionException(String, Throwable)} and provide a meaningful message.
	 */
	@Deprecated
	public LinkParameterExtractionException(Throwable cause) {
		super(null, cause);
	}

	public LinkParameterExtractionException(String message, Throwable cause) {
		super(message, cause);
	}

}
