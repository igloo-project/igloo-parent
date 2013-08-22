package fr.openwide.core.wicket.more.link.descriptor.parameter.extractor;

public class LinkParameterExtractionRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8266412526724031907L;

	public LinkParameterExtractionRuntimeException(LinkParameterExtractionException cause) {
		super("An error occurred during parameter extraction", cause);
	}

}
