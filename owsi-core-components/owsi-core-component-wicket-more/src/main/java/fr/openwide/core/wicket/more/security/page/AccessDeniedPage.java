package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.request.cycle.RequestCycleUtils;

/**
 * This page is used only when a logged in user tries to access an unauthorized page.
 */
public class AccessDeniedPage extends CoreWebPage {

	private static final long serialVersionUID = 4583415457223655426L;

	public AccessDeniedPage() {
		super(new PageParameters());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		AbstractCoreSession.get().signOut();
		AbstractCoreSession.get().getFeedbackMessages().clear();
		AbstractCoreSession.get().error(getString("access.denied"));
		AbstractCoreSession.get().registerRedirectUrl(RequestCycleUtils.getSpringSecuritySavedRequest());
		
		throw CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor().newRestartResponseException();
	}
}
