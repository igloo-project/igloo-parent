package fr.openwide.core.wicket.more.link.descriptor.parameter.injector;

public class LinkParameterInjectionRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8266412526724031907L;

	public LinkParameterInjectionRuntimeException(LinkParameterInjectionException cause) {
		super("An error occurred during parameter injection", cause);
	}

}
