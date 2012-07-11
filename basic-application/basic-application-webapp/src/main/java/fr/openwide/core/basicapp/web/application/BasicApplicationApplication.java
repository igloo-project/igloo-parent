package fr.openwide.core.basicapp.web.application;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.basicapp.web.application.common.template.MainTemplate;
import fr.openwide.core.basicapp.web.application.navigation.page.HomePage;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.markup.html.pages.monitoring.DatabaseMonitoringPage;

public class BasicApplicationApplication extends CoreWicketAuthenticatedApplication {

	@Override
	protected void mountApplicationPages() {
		
		// Sondes de monitoring
		mountPage("/monitoring/db-access/", DatabaseMonitoringPage.class);
	}

	@Override
	protected void mountApplicationResources() {
		mountStaticResourceDirectory("/application/common", MainTemplate.class);
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return BasicApplicationSession.class;
	}

	@Override
	public Class<? extends WebPage> getSignInPageClass() {
		return HomePage.class;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}
}
