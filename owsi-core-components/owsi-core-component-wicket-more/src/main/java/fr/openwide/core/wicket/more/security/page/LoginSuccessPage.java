package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.wicket.more.AbstractCoreSession;

public class LoginSuccessPage extends WebPage {
	
	public LoginSuccessPage() {
		AbstractCoreSession.get().signIn("", "");
		
		String redirect = AbstractCoreSession.get().getRedirectUrl();
		
		if (redirect != null) {
			throw new RedirectToUrlException(redirect);
		} else {
			setResponsePage(this.getApplication().getHomePage());
			setRedirect(true);
		}
	}

}
