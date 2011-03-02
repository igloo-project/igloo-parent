package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.wicket.more.CoreSession;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;


public class LogoutPage extends WebPage {
	
	public LogoutPage() {
		CoreSession session = CoreSession.get();
		if(session != null) {
			session.signOut();
		}
		
		setResponsePage(CoreWicketAuthenticatedApplication.get().getSignInPageClass());
		setRedirect(true);
	}

}
