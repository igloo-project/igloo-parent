package org.iglooproject.wicket.bootstrap5.console.navigation.page;

import org.apache.wicket.Page;
import org.iglooproject.wicket.bootstrap5.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;

public class ConsoleLoginSuccessPage extends LoginSuccessPage {

	private static final long serialVersionUID = 1395214320773430444L;

	public ConsoleLoginSuccessPage() {
	}
	
	@Override
	protected Class<? extends Page> getDefaultRedirectPage() {
		return ConsoleMaintenanceSearchPage.class;
	}

}
