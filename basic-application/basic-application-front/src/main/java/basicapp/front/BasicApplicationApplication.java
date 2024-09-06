package basicapp.front;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.user.model.BasicUser;
import basicapp.back.business.user.model.TechnicalUser;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.UserGroup;
import basicapp.front.announcement.page.AnnouncementListPage;
import basicapp.front.common.converter.LocalDateConverter;
import basicapp.front.common.converter.LocalDateTimeConverter;
import basicapp.front.common.converter.LocalTimeConverter;
import basicapp.front.common.converter.PostalCodeConverter;
import basicapp.front.common.renderer.AuthorityRenderer;
import basicapp.front.common.renderer.InstantRenderer;
import basicapp.front.common.template.favicon.ApplicationFaviconPackage;
import basicapp.front.common.template.resources.BasicApplicationResourcesPackage;
import basicapp.front.common.template.resources.styles.application.application.applicationaccess.ApplicationAccessScssResourceReference;
import basicapp.front.common.template.resources.styles.application.console.console.ConsoleScssResourceReference;
import basicapp.front.common.template.resources.styles.application.console.consoleaccess.ConsoleAccessScssResourceReference;
import basicapp.front.common.template.resources.styles.notification.email.NotificationEmailScssResourceReference;
import basicapp.front.common.template.resources.styles.notification.head.NotificationHeadScssResourceReference;
import basicapp.front.console.common.component.ConsoleAccessHeaderAdditionalContentPanel;
import basicapp.front.console.common.component.ConsoleHeaderAdditionalContentPanel;
import basicapp.front.console.common.component.ConsoleHeaderEnvironmentPanel;
import basicapp.front.console.notification.demo.page.ConsoleNotificationDemoListPage;
import basicapp.front.history.renderer.HistoryValueRenderer;
import basicapp.front.navigation.page.HomePage;
import basicapp.front.navigation.page.MaintenancePage;
import basicapp.front.profile.page.ProfilePage;
import basicapp.front.referencedata.page.ReferenceDataPage;
import basicapp.front.resources.application.BasicApplicationApplicationResources;
import basicapp.front.resources.business.BasicApplicationBusinessResources;
import basicapp.front.resources.common.BasicApplicationCommonResources;
import basicapp.front.resources.console.BasicApplicationConsoleResources;
import basicapp.front.resources.enums.BasicApplicationEnumsResources;
import basicapp.front.resources.navigation.BasicApplicationNavigationResources;
import basicapp.front.resources.notification.BasicApplicationNotificationResources;
import basicapp.front.security.login.page.SignInPage;
import basicapp.front.security.password.page.SecurityPasswordCreationPage;
import basicapp.front.security.password.page.SecurityPasswordExpirationPage;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestCreationPage;
import basicapp.front.security.password.page.SecurityPasswordRecoveryRequestResetPage;
import basicapp.front.security.password.page.SecurityPasswordResetPage;
import basicapp.front.user.page.BasicUserDetailPage;
import basicapp.front.user.page.BasicUserListPage;
import basicapp.front.user.page.TechnicalUserDetailPage;
import basicapp.front.user.page.TechnicalUserListPage;
import basicapp.front.user.renderer.UserRenderer;
import basicapp.front.usergroup.page.UserGroupDetailPage;
import basicapp.front.usergroup.page.UserGroupListPage;
import basicapp.front.usergroup.renderer.UserGroupRenderer;
import com.google.common.collect.ImmutableList;
import igloo.console.navigation.page.ConsoleAccessDeniedPage;
import igloo.console.navigation.page.ConsoleLoginFailurePage;
import igloo.console.navigation.page.ConsoleLoginSuccessPage;
import igloo.console.navigation.page.ConsoleSignInPage;
import igloo.console.template.ConsoleConfiguration;
import igloo.wicket.convert.EnumClassAwareConverterLocator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.iglooproject.jpa.more.business.history.model.embeddable.HistoryValue;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.application.CoreWicketAuthenticatedApplication;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;
import org.iglooproject.wicket.more.markup.html.pages.monitoring.DatabaseMonitoringPage;
import org.iglooproject.wicket.more.rendering.BooleanRenderer;
import org.iglooproject.wicket.more.rendering.EnumRenderer;
import org.iglooproject.wicket.more.rendering.LocaleRenderer;
import org.iglooproject.wicket.more.security.page.LoginFailurePage;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.util.convert.HibernateProxyAwareConverterLocator;
import org.iglooproject.wicket.more.util.listener.FormInvalidDecoratorListener;
import org.springframework.beans.factory.annotation.Autowired;

public class BasicApplicationApplication extends CoreWicketAuthenticatedApplication {

  public static final String NAME = "BasicApplicationApplication";

  @Autowired private IPropertyService propertyService;

  public static BasicApplicationApplication get() {
    final Application application = Application.get();
    if (application instanceof BasicApplicationApplication) {
      return (BasicApplicationApplication) application;
    }
    throw new WicketRuntimeException(
        "There is no BasicApplicationApplication attached to current thread "
            + Thread.currentThread().getName());
  }

  @Override
  public void init() {
    super.init();

    // si on n'est pas en développement, on précharge les feuilles de styles pour éviter la ruée et
    // permettre le remplissage du cache
    if (!propertyService.isConfigurationTypeDevelopment()) {
      preloadStyleSheets(
          ConsoleAccessScssResourceReference.get(),
          ConsoleScssResourceReference.get(),
          NotificationEmailScssResourceReference.get(),
          NotificationHeadScssResourceReference.get(),
          ApplicationAccessScssResourceReference.get(),
          basicapp.front.common.template.resources.styles.application.application.applicationbasic
              .StylesScssResourceReference.get(),
          basicapp.front.common.template.resources.styles.application.application
              .applicationadvanced.StylesScssResourceReference.get());
    }

    getResourceSettings()
        .getStringResourceLoaders()
        .addAll(
            0, // Override the keys in existing resource loaders with the following
            ImmutableList.of(
                new ClassStringResourceLoader(BasicApplicationApplicationResources.class),
                new ClassStringResourceLoader(BasicApplicationBusinessResources.class),
                new ClassStringResourceLoader(BasicApplicationCommonResources.class),
                new ClassStringResourceLoader(BasicApplicationConsoleResources.class),
                new ClassStringResourceLoader(BasicApplicationEnumsResources.class),
                new ClassStringResourceLoader(BasicApplicationNavigationResources.class),
                new ClassStringResourceLoader(BasicApplicationNotificationResources.class)));

    FormInvalidDecoratorListener.init(this);
  }

  @Override
  protected IConverterLocator newConverterLocator() {
    ConverterLocator converterLocator = new ConverterLocator();

    converterLocator.set(LocalDate.class, LocalDateConverter.get());
    converterLocator.set(LocalDateTime.class, LocalDateTimeConverter.get());
    converterLocator.set(LocalTime.class, LocalTimeConverter.get());
    converterLocator.set(Instant.class, InstantRenderer.get());

    converterLocator.set(Authority.class, AuthorityRenderer.get());
    converterLocator.set(User.class, UserRenderer.get());
    converterLocator.set(TechnicalUser.class, UserRenderer.get());
    converterLocator.set(BasicUser.class, UserRenderer.get());
    converterLocator.set(UserGroup.class, UserGroupRenderer.get());

    converterLocator.set(Locale.class, LocaleRenderer.get());
    converterLocator.set(Boolean.class, BooleanRenderer.get());

    converterLocator.set(HistoryValue.class, HistoryValueRenderer.get());
    converterLocator.set(HistoryEventType.class, EnumRenderer.get());

    converterLocator.set(PostalCode.class, PostalCodeConverter.get());

    return new EnumClassAwareConverterLocator(
        new HibernateProxyAwareConverterLocator(converterLocator));
  }

  @Override
  protected void mountApplicationPages() {

    // Sign in
    mountPage("/login/", getSignInPageClass());
    mountPage("/login/failure/", LoginFailurePage.class);
    mountPage("/login/success/", LoginSuccessPage.class);

    mountPage(
        "/security/password/recovery/request/creation/",
        SecurityPasswordRecoveryRequestCreationPage.class);
    mountPage(
        "/security/password/recovery/request/reset/",
        SecurityPasswordRecoveryRequestResetPage.class);
    mountPage("/security/password/expiration/", SecurityPasswordExpirationPage.class);
    mountParameterizedPage("/security/password/creation/", SecurityPasswordCreationPage.class);
    mountParameterizedPage("/security/password/reset/", SecurityPasswordResetPage.class);

    // Maintenance
    mountPage("/maintenance/", MaintenancePage.class);

    // Profile
    mountPage("/profile/", ProfilePage.class);

    // Reference data
    mountPage("/reference-data/", ReferenceDataPage.class);

    // Administration
    mountPage("/administration/basic-user/", BasicUserListPage.class);
    mountParameterizedPage(
        "/administration/basic-user/${" + CommonParameters.ID + "}/", BasicUserDetailPage.class);
    mountPage("/administration/technical-user/", TechnicalUserListPage.class);
    mountParameterizedPage(
        "/administration/technical-user/${" + CommonParameters.ID + "}/",
        TechnicalUserDetailPage.class);
    mountPage("/administration/user-group/", UserGroupListPage.class);
    mountParameterizedPage(
        "/administration/user-group/${" + CommonParameters.ID + "}/", UserGroupDetailPage.class);
    mountPage("/administration/announcement/", AnnouncementListPage.class);

    // Console sign in
    mountPage("/console/login/", ConsoleSignInPage.class);
    mountPage("/console/login/failure/", ConsoleLoginFailurePage.class);
    mountPage("/console/login/success/", ConsoleLoginSuccessPage.class);
    mountPage("/console/access-denied/", ConsoleAccessDeniedPage.class);

    // Console
    ConsoleConfiguration consoleConfiguration =
        ConsoleConfiguration.build("console", getResourceSettings());
    consoleConfiguration.addCssResourceReference(ConsoleScssResourceReference.get());
    consoleConfiguration.addConsoleAccessCssResourceReference(
        ConsoleAccessScssResourceReference.get());
    consoleConfiguration.setConsoleAccessHeaderAdditionalContentComponentFactory(
        ConsoleAccessHeaderAdditionalContentPanel::new);
    consoleConfiguration.setConsoleHeaderEnvironmentComponentFactory(
        ConsoleHeaderEnvironmentPanel::new);
    consoleConfiguration.setConsoleHeaderAdditionalContentComponentFactory(
        ConsoleHeaderAdditionalContentPanel::new);

    ConsoleMenuSection notificationMenuSection =
        new ConsoleMenuSection(
            "notificationMenuSection",
            "console.navigation.notification",
            "notification",
            ConsoleNotificationDemoListPage.class);
    consoleConfiguration.addMenuSection(notificationMenuSection);

    consoleConfiguration.mountPages(this);

    // Monitoring
    mountPage("/monitoring/db-access/", DatabaseMonitoringPage.class);
  }

  @Override
  protected void mountApplicationResources() {
    mountStaticResourceDirectory("/application", BasicApplicationResourcesPackage.class);

    // See favicon generator https://realfavicongenerator.net/
    mountResource(
        "/android-chrome-192x192.png",
        new PackageResourceReference(
            ApplicationFaviconPackage.class, "android-chrome-192x192.png"));
    mountResource(
        "/android-chrome-256x256.png",
        new PackageResourceReference(
            ApplicationFaviconPackage.class, "android-chrome-256x256.png"));
    mountResource(
        "/apple-touch-icon.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "apple-touch-icon.png"));
    mountResource(
        "/browserconfig.xml",
        new PackageResourceReference(ApplicationFaviconPackage.class, "browserconfig.xml"));
    mountResource(
        "/favicon.ico",
        new PackageResourceReference(ApplicationFaviconPackage.class, "favicon.ico"));
    mountResource(
        "/favicon-16x16.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "favicon-16x16.png"));
    mountResource(
        "/favicon-32x32.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "favicon-32x32.png"));
    mountResource(
        "/mstile-150x150.png",
        new PackageResourceReference(ApplicationFaviconPackage.class, "mstile-150x150.png"));
    mountResource(
        "/safari-pinned-tab.svg",
        new PackageResourceReference(ApplicationFaviconPackage.class, "safari-pinned-tab.svg"));
    mountResource(
        "/site.webmanifest",
        new PackageResourceReference(ApplicationFaviconPackage.class, "site.webmanifest"));
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
