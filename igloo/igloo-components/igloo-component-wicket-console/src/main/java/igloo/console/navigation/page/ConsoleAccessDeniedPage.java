package igloo.console.navigation.page;

import org.iglooproject.wicket.more.security.page.AccessDeniedPage;

public class ConsoleAccessDeniedPage extends AccessDeniedPage {

	private static final long serialVersionUID = 981749717310498235L;

	public ConsoleAccessDeniedPage() {
		super(ConsoleSignInPage.linkDescriptor());
	}
}
