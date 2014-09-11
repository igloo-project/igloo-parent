package fr.openwide.core.wicket.more.console.navigation.page;

import fr.openwide.core.wicket.more.security.page.AccessDeniedPage;

public class ConsoleAccessDeniedPage extends AccessDeniedPage {

	private static final long serialVersionUID = 981749717310498235L;

	public ConsoleAccessDeniedPage() {
		super(ConsoleSignInPage.linkDescriptor());
	}
}
