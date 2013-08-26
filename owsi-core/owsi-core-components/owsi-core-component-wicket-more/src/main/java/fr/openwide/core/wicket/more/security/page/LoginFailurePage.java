package fr.openwide.core.wicket.more.security.page;

import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

public class LoginFailurePage extends CoreWebPage {
	
	private static final long serialVersionUID = 7256074713578695000L;

	public LoginFailurePage() {
		getSession().error(getLocalizer().getString("login.failed", this));

		throw CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor()
				.newRestartResponseException();
	}

}
