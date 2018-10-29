package org.iglooproject.basicapp.web.application.common.template;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.APPLICATION_THEME;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.security.model.BasicApplicationAuthorityConstants;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationAnnouncementListPage;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupListPage;
import org.iglooproject.basicapp.web.application.common.component.AnnouncementsPanel;
import org.iglooproject.basicapp.web.application.common.component.ApplicationEnvironmentPanel;
import org.iglooproject.basicapp.web.application.common.template.theme.BasicApplicationApplicationTheme;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.AdministrationUserTypeDescriptor;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.referencedata.page.ReferenceDataPage;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap4.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip.BootstrapTooltip;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.dropdown.BootstrapDropdownBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;

import com.google.common.collect.ImmutableList;

public abstract class MainTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = -1312989780696228848L;

	@SpringBean
	private IPropertyService propertyService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	@SpringBean
	private IAuthenticationService authenticationService;

	private final IModel<BasicApplicationApplicationTheme> applicationThemeModel = ApplicationPropertyModel.of(APPLICATION_THEME);

	public MainTemplate(PageParameters parameters) {
		super(parameters);
		
		if (Boolean.TRUE.equals(propertyService.get(MAINTENANCE)) && !authenticationService.hasAdminRole()) {
			throw new RedirectToUrlException(propertyService.get(MAINTENANCE_URL));
		}
		
		if (securityManagementService.isPasswordExpired(BasicApplicationSession.get().getUser())) {
			throw UserTypeDescriptor.get(BasicApplicationSession.get().getUser())
					.securityTypeDescriptor()
					.passwordExpirationPageLinkDescriptor()
					.newRestartResponseException();
		}
		
		add(new TransparentWebMarkupContainer("htmlRootElement")
				.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage())));
		
		add(new TransparentWebMarkupContainer("bodyContainer")
				.add(new ClassAttributeAppender(BasicApplicationSession.get().getEnvironmentModel())));
		
		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel"));
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		add(new ApplicationEnvironmentPanel("environment"));
		
		add(new AnnouncementsPanel("announcements"));
		
		add(
				createBodyBreadCrumb("breadCrumb")
						.add(displayBreadcrumb().thenShow())
		);
		
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		add(new BootstrapDropdownBehavior());
		
		getApplicationTheme().specificContent(
				this,
				(SerializableSupplier2<List<NavigationMenuItem>>) this::getMainNav,
				(SerializableSupplier2<Class<? extends WebPage>>) this::getFirstMenuPage,
				(SerializableSupplier2<Class<? extends WebPage>>) this::getSecondMenuPage
		);
	}

	protected List<NavigationMenuItem> getMainNav() {
		return ImmutableList.of(
				BasicApplicationApplication.get().getHomePageLinkDescriptor()
					.navigationMenuItem(new ResourceModel("navigation.home"))
					.setIconClassesModel(Model.of("fa fa-fw fa-home")),
				ReferenceDataPage.linkDescriptor()
					.navigationMenuItem(new ResourceModel("navigation.referenceData"))
					.setIconClassesModel(Model.of("fa fa-fw fa-list")),
				AdministrationUserTypeDescriptor.BASIC_USER.list()
					.navigationMenuItem(new ResourceModel("navigation.administration"))
					.setIconClassesModel(Model.of("fa fa-fw fa-cogs"))
					.setSubMenuItems(ImmutableList.of(
						AdministrationUserTypeDescriptor.BASIC_USER.list()
							.navigationMenuItem(new ResourceModel("navigation.administration.user.basic"))
							.setIconClassesModel(Model.of("fa fa-fw fa-user-cog")),
						AdministrationUserTypeDescriptor.TECHNICAL_USER.list()
							.navigationMenuItem(new ResourceModel("navigation.administration.user.technical"))
							.setIconClassesModel(Model.of("fa fa-fw fa-user-shield")),
						AdministrationUserGroupListPage.linkDescriptor()
							.navigationMenuItem(new ResourceModel("navigation.administration.userGroup"))
							.setIconClassesModel(Model.of("fa fa-fw fa-users-cog")),
						AdministrationAnnouncementListPage.linkDescriptor()
							.navigationMenuItem(new ResourceModel("navigation.administration.announcement"))
							.setIconClassesModel(Model.of("fa fa-fw fa-bullhorn"))
					)),
				LinkDescriptorBuilder.start()
					.validator(Condition.role(BasicApplicationAuthorityConstants.ROLE_ADMIN))
					.page(ConsoleMaintenanceSearchPage.class)
					.navigationMenuItem(new ResourceModel("navigation.console"))
					.setIconClassesModel(Model.of("fa fa-fw fa-wrench"))
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
	
	@Override
	protected Component createBodyBreadCrumb(String wicketId) {
		// By default, we remove one element from the breadcrumb as it is usually also used to generate the page title.
		// The last element is usually the title of the current page and shouldn't be displayed in the breadcrumb.
		return new BodyBreadCrumbPanel(wicketId, bodyBreadCrumbPrependedElementsModel, breadCrumbElementsModel, 1)
				.setDividerModel(Model.of(""))
				.setTrailingSeparator(true);
	}
	
	protected Condition displayBreadcrumb() {
		return Condition.alwaysTrue();
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		getApplicationTheme().renderHead(response);
	}

	@Override
	public String getVariation() {
		return getApplicationTheme().getMarkupVariation();
	}
	
	protected BootstrapTooltip getBootstrapTooltip() {
		return new BootstrapTooltip()
			.selector("[title],[data-original-title]")
			.animation(true)
			.container("body");
	}

	protected BasicApplicationApplicationTheme getApplicationTheme() {
		return applicationThemeModel.getObject();
	}

}