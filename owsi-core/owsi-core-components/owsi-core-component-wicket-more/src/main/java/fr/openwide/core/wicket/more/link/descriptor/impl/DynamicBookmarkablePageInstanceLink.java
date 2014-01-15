package fr.openwide.core.wicket.more.link.descriptor.impl;

import org.apache.wicket.Page;
import org.apache.wicket.Session;

import fr.openwide.core.wicket.more.link.descriptor.AbstractDynamicBookmarkableLink;
import fr.openwide.core.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import fr.openwide.core.wicket.more.link.descriptor.parameter.validator.LinkParameterValidationRuntimeException;

public class DynamicBookmarkablePageInstanceLink extends AbstractDynamicBookmarkableLink {
	
	private static final long serialVersionUID = 3375563675399910552L;
	
	private final CorePageInstanceLinkGenerator pageInstanceLinkGenerator;

	public DynamicBookmarkablePageInstanceLink(String wicketId, CorePageInstanceLinkGenerator pageInstanceLinkGenerator) {
		super(wicketId);
		this.pageInstanceLinkGenerator = pageInstanceLinkGenerator;
	}

	@Override
	protected boolean isValid() {
		return pageInstanceLinkGenerator.isAccessible();
	}

	@Override
	protected CharSequence getRelativeURL() throws LinkInvalidTargetRuntimeException,
			LinkParameterValidationRuntimeException {
		Page pageInstance = pageInstanceLinkGenerator.getValidPageInstance();
		if (!Session.get().getAuthorizationStrategy().isActionAuthorized(pageInstance, Page.RENDER)) {
			throw new LinkInvalidTargetRuntimeException("The rendering of the target page '" + pageInstance + "' was not authorized when trying to render the URL.");
		}
		return pageInstanceLinkGenerator.relativeUrl();
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		pageInstanceLinkGenerator.detach();
	}

}
