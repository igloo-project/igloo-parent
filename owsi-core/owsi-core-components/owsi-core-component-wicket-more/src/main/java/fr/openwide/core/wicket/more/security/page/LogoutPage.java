package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;


public class LogoutPage extends WebPage {
	
	public LogoutPage() {
		AuthenticatedWebSession session = AuthenticatedWebSession.get();
		if(session != null) {
			session.signOut();
		}
		
		setResponsePage(CoreWicketAuthenticatedApplication.get().getSignInPageClass());
		setRedirect(true);
	}

}
