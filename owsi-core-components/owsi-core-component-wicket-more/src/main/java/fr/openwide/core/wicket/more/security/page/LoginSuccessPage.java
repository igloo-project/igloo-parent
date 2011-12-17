package fr.openwide.core.wicket.more.security.page;

import org.apache.wicket.request.flow.RedirectToUrlException;

import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

public class LoginSuccessPage extends CoreWebPage {
	
	private static final long serialVersionUID = -875304387617628398L;

	public LoginSuccessPage() {
		AbstractCoreSession.get().signIn("", "");
		
		String redirect = AbstractCoreSession.get().getRedirectUrl();
		
		if (redirect != null) {
			throw new RedirectToUrlException(redirect);
		} else {
			redirect(this.getApplication().getHomePage());
		}
	}

}
