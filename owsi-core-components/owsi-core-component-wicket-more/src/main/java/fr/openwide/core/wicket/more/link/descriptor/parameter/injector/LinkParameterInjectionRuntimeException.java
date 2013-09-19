package fr.openwide.core.wicket.more.link.descriptor.parameter.injector;

import org.springframework.core.NestedRuntimeException;

public class LinkParameterInjectionRuntimeException extends NestedRuntimeException {

	private static final long serialVersionUID = 7659297942305885540L;

	public LinkParameterInjectionRuntimeException(LinkParameterInjectionException cause) {
		super("An error occurred during parameter injection", cause);
	}

}
