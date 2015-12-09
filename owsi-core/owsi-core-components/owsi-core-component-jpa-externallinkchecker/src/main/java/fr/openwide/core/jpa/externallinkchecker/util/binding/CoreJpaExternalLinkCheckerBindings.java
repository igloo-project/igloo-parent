package fr.openwide.core.jpa.externallinkchecker.util.binding;

import fr.openwide.core.jpa.externallinkchecker.business.model.ExternalLinkWrapperBinding;

public final class CoreJpaExternalLinkCheckerBindings {

	private static final ExternalLinkWrapperBinding EXTERNAL_LINK_WRAPPER = new ExternalLinkWrapperBinding();
	
	public static ExternalLinkWrapperBinding externalLinkWrapper() {
		return EXTERNAL_LINK_WRAPPER;
	}
	
	private CoreJpaExternalLinkCheckerBindings() {
	}

}
