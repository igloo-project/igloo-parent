package fr.openwide.core.wicket.more.link.descriptor.parameter.extractor;

import org.springframework.core.NestedRuntimeException;

public class LinkParameterExtractionRuntimeException extends NestedRuntimeException {

	private static final long serialVersionUID = -456914851659661424L;

	public LinkParameterExtractionRuntimeException(LinkParameterExtractionException cause) {
		super("An error occurred during parameter extraction", cause);
	}

}
