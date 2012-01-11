package fr.openwide.core.showcase.web.application;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

import fr.openwide.core.showcase.web.application.navigation.page.HomePage;
import fr.openwide.core.showcase.web.application.navigation.page.SignInPage;
import fr.openwide.core.showcase.web.application.portfolio.page.PortfolioMainPage;
import fr.openwide.core.showcase.web.application.util.template.MainTemplate;
import fr.openwide.core.showcase.web.application.widgets.page.AutocompletePage;
import fr.openwide.core.showcase.web.application.widgets.page.CalendarPage;
import fr.openwide.core.showcase.web.application.widgets.page.FancyboxPage;
import fr.openwide.core.showcase.web.application.widgets.page.WidgetsMainPage;
import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.security.page.LoginFailurePage;
import fr.openwide.core.wicket.more.security.page.LoginSuccessPage;

public class ShowcaseApplication extends CoreWicketAuthenticatedApplication {

	public static ShowcaseApplication get() {
		final Application application = Application.get();
		if (application instanceof ShowcaseApplication) {
			return (ShowcaseApplication) application;
		}
		throw new WicketRuntimeException("There is no ShowcaseApplication attached to current thread " +
				Thread.currentThread().getName());
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return ShowcaseSession.class;
	}

	@Override
	protected void mountApplicationPages() {
		// Sign in
		mountPage("/login/", getSignInPageClass());
		mountPage("/login/failure/", LoginFailurePage.class);
		mountPage("/login/success/", LoginSuccessPage.class);
		
		// Application
		mountPage("/home/", getHomePage());
		mountPage("/portfolio/", PortfolioMainPage.class);
		mountPage("/widgets/", WidgetsMainPage.class);
		mountPage("/widgets/calendar", CalendarPage.class);
		mountPage("/widgets/autocomplete", AutocompletePage.class);
		mountPage("/widgets/fancybox", FancyboxPage.class);
	}

	@Override
	protected void mountApplicationResources() {
		mountStaticResourceDirectory("/application", MainTemplate.class);
	}

	@Override
	public Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

}
