package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

public class AccessDeniedPage extends CoreWebPage {

	private static final long serialVersionUID = 4583415457223655426L;

	public AccessDeniedPage() {
		super(new PageParameters());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		AuthenticatedWebSession.get().getFeedbackMessages().clear();
		AuthenticatedWebSession.get().error(getString("access.denied"));
		
		throw CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor()
				.newRestartResponseException();
	}
}
