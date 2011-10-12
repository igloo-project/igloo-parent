package fr.openwide.core.wicket.more.markup.html.pages.security;

import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;

public class LoginFailurePage extends WebPage {
	
	public LoginFailurePage() {
		getSession().error(getLocalizer().getString("login.failed", this));
		setResponsePage(CoreWicketAuthenticatedApplication.get().getSignInPageClass());
		setRedirect(true);
	}

}
