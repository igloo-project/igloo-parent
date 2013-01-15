package fr.openwide.core.wicket.more.security.page;

import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.application.CoreWicketApplication;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

public class AccessDeniedPage extends CoreWebPage {

	private static final long serialVersionUID = -1269934404552240133L;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		AbstractCoreSession.get().error(getString("access.denied"));
		redirect(CoreWicketApplication.get().getHomePage());
	}
}

