package fr.openwide.core.basicapp.web.application;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserDescriptionPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupDescriptionPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupPortfolioPage;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.common.template.MainTemplate;
import fr.openwide.core.basicapp.web.application.console.notification.demo.page.ConsoleNotificationDemoIndexPage;
import fr.openwide.core.basicapp.web.application.navigation.page.HomePage;
import fr.openwide.core.basicapp.web.application.navigation.page.SignInPage;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.console.common.model.ConsoleMenuSection;
import fr.openwide.core.wicket.more.console.template.ConsoleConfiguration;
import fr.openwide.core.wicket.more.link.descriptor.parameter.CommonParameters;
import fr.openwide.core.wicket.more.markup.html.pages.monitoring.DatabaseMonitoringPage;
import fr.openwide.core.wicket.more.security.page.LoginFailurePage;
import fr.openwide.core.wicket.more.security.page.LoginSuccessPage;

public class BasicApplicationApplication extends CoreWicketAuthenticatedApplication {
	
	public static final String NAME = "BasicApplication";
	
	public static BasicApplicationApplication get() {
		final Application application = Application.get();
		if (application instanceof BasicApplicationApplication) {
			return (BasicApplicationApplication) application;
		}
		throw new WicketRuntimeException("There is no BasicApplicationApplication attached to current thread " +
				Thread.currentThread().getName());
	}

	@Override
	protected void mountApplicationPages() {
		
		// Sign in
		mountPage("/login/", getSignInPageClass());
		mountPage("/login/failure/", LoginFailurePage.class);
		mountPage("/login/success/", LoginSuccessPage.class);
		
		// Administration
		mountPage("/administration/user/", AdministrationUserPortfolioPage.class);
		mountPage("/administration/user/${" + CommonParameters.ID + "}/",
				AdministrationUserDescriptionPage.class);
		mountPage("/administration/user-group/", AdministrationUserGroupPortfolioPage.class);
		mountPage("/administration/user-group/${" + CommonParameters.ID + "}/",
				AdministrationUserGroupDescriptionPage.class);
		
		// Console
		ConsoleConfiguration consoleConfiguration = ConsoleConfiguration.build("console");
		consoleConfiguration.mountPages(this);
		
		ConsoleMenuSection notificationMenuSection = new ConsoleMenuSection("notificationsMenuSection", "console.notifications",
				"notifications", ConsoleNotificationDemoIndexPage.class);
		consoleConfiguration.addMenuSection(notificationMenuSection);
		
		mountPage("/console/notifications/", ConsoleNotificationDemoIndexPage.class);
		
		// Monitoring
		mountPage("/monitoring/db-access/", DatabaseMonitoringPage.class);
	}

	@Override
	protected void mountApplicationResources() {
		mountStaticResourceDirectory("/application", MainTemplate.class);
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return BasicApplicationSession.class;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	@Override
	public Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}
	
}
