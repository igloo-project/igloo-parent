package org.iglooproject.showcase.web.application;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.showcase.web.application.links.page.LinksPage1;
import org.iglooproject.showcase.web.application.links.page.LinksPage2;
import org.iglooproject.showcase.web.application.links.page.LinksPage3;
import org.iglooproject.showcase.web.application.navigation.page.HomePage;
import org.iglooproject.showcase.web.application.navigation.page.SignInPage;
import org.iglooproject.showcase.web.application.others.page.ButtonsPage;
import org.iglooproject.showcase.web.application.others.page.HideableComponentsPage;
import org.iglooproject.showcase.web.application.others.page.TitlesPage;
import org.iglooproject.showcase.web.application.portfolio.page.PortfolioMainPage;
import org.iglooproject.showcase.web.application.portfolio.page.UserDescriptionPage;
import org.iglooproject.showcase.web.application.task.page.TaskMainPage;
import org.iglooproject.showcase.web.application.util.template.MainTemplate;
import org.iglooproject.showcase.web.application.widgets.page.AutocompletePage;
import org.iglooproject.showcase.web.application.widgets.page.AutosizePage;
import org.iglooproject.showcase.web.application.widgets.page.BootstrapJsPage;
import org.iglooproject.showcase.web.application.widgets.page.CalendarPage;
import org.iglooproject.showcase.web.application.widgets.page.CarouselPage;
import org.iglooproject.showcase.web.application.widgets.page.FileDownloadPage;
import org.iglooproject.showcase.web.application.widgets.page.FileUploadPage;
import org.iglooproject.showcase.web.application.widgets.page.ListFilterPage;
import org.iglooproject.showcase.web.application.widgets.page.ModalPage;
import org.iglooproject.showcase.web.application.widgets.page.SelectBoxPage;
import org.iglooproject.showcase.web.application.widgets.page.SortableListPage;
import org.iglooproject.showcase.web.application.widgets.page.StatisticsPage;
import org.iglooproject.showcase.web.application.widgets.page.WidgetsMainPage;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.console.template.ConsoleConfiguration;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.security.page.LoginFailurePage;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;

public class ShowcaseApplication extends CoreWicketAuthenticatedApplication {
	
	public static final String NAME = "ShowcaseApplication";

	@Autowired
	IPropertyService propertyService;

	public static ShowcaseApplication get() {
		final Application application = Application.get();
		if (application instanceof ShowcaseApplication) {
			return (ShowcaseApplication) application;
		}
		throw new WicketRuntimeException("There is no ShowcaseApplication attached to current thread " +
				Thread.currentThread().getName());
	}
	
	@Override
	public void init() {
		super.init();
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return ShowcaseSession.class;
	}

	@Override
	protected void mountApplicationPages() {
		// Sign in
		mountPage("/login/", SignInPage.class);
		mountPage("/login/failure/", LoginFailurePage.class);
		mountPage("/login/success/", LoginSuccessPage.class);
		
		// Portfolio exemple
		mountPage("/portfolio/", PortfolioMainPage.class);
		mountPage("/portfolio/user/${" + CommonParameters.ID + "}", UserDescriptionPage.class);
		
		// Widgets exemple
		mountPage("/widgets/", WidgetsMainPage.class);
		mountPage("/widgets/calendar/", CalendarPage.class);
		mountPage("/widgets/autocomplete/", AutocompletePage.class);
		mountPage("/widgets/modal/", ModalPage.class);
		mountPage("/widgets/list-filter/", ListFilterPage.class);
		mountPage("/widgets/bootstrap-js/", BootstrapJsPage.class);
		mountPage("/widgets/statistics/", StatisticsPage.class);
		mountPage("/widgets/carousel/", CarouselPage.class);
		mountPage("/widgets/autosize/", AutosizePage.class);
		mountPage("/widgets/sortable-list/", SortableListPage.class);
		mountPage("/widgets/selectbox/", SelectBoxPage.class);
		mountPage("/widgets/fileupload/", FileUploadPage.class);
		mountPage("/widgets/filedownload/", FileDownloadPage.class);
		
		mountPage("/titles/", TitlesPage.class);
		
		mountPage("/buttons/", ButtonsPage.class);
		
		mountPage("/hideable-components/", HideableComponentsPage.class);
		
		mountPage("/links/1/", LinksPage1.class);
		mountPage("/links/2/", LinksPage2.class);
		mountPage("/links/3/", LinksPage3.class);
		
		// Task
		mountPage("/task/", TaskMainPage.class);
		
		// Console
		ConsoleConfiguration consoleConfiguration = ConsoleConfiguration.build("console", propertyService);
		consoleConfiguration.mountPages(this);
	}

	@Override
	protected void mountApplicationResources() {
		mountStaticResourceDirectory("/application", MainTemplate.class);
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
