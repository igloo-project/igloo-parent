package fr.openwide.core.wicket.more.security.page;

import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

public class LoginFailurePage extends CoreWebPage {
	
	private static final long serialVersionUID = 7256074713578695000L;
	
	public LoginFailurePage() {
		this(CoreWicketAuthenticatedApplication.get().getSignInPageLinkDescriptor());
	}

	public LoginFailurePage(IPageLinkDescriptor signInPageLinkDescriptor) {
		getSession().error(getLocalizer().getString("login.failed", this));
		
		throw signInPageLinkDescriptor.newRestartResponseException();
	}

}
