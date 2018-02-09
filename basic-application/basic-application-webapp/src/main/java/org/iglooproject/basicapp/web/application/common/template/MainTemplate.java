package org.iglooproject.basicapp.web.application.common.template;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;
import static org.iglooproject.spring.property.SpringPropertyIds.IGLOO_VERSION;
import static org.iglooproject.spring.property.SpringPropertyIds.VERSION;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.core.security.service.ISecurityManagementService;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.administration.page.AdministrationUserGroupListPage;
import org.iglooproject.basicapp.web.application.common.component.ApplicationEnvironmentPanel;
import org.iglooproject.basicapp.web.application.common.template.resources.styles.StylesScssResourceReference;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.AdministrationUserTypeDescriptor;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import org.iglooproject.basicapp.web.application.profile.page.ProfilePage;
import org.iglooproject.basicapp.web.application.referencedata.page.ReferenceDataPage;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap4.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.dropdown.BootstrapDropdownBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip.BootstrapTooltip;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstraphoverdropdown.BootstrapHoverDropdownBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.model.BindingModel;
import org.iglooproject.wicket.more.security.page.LoginSuccessPage;
import org.iglooproject.wicket.more.security.page.LogoutPage;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public abstract class MainTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = -1312989780696228848L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainTemplate.class);

	@SpringBean
	private IPropertyService propertyService;

	@SpringBean
	private ISecurityManagementService securityManagementService;

	@SpringBean
	private IAuthenticationService authenticationService;

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
		
		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel"));
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		add(new ListView<NavigationMenuItem>("mainNav", getMainNav()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<NavigationMenuItem> item) {
				NavigationMenuItem navItem = item.getModelObject();
				
				if (navItem.isActive(MainTemplate.this.getFirstMenuPage())) {
					item.add(new ClassAttributeAppender("active"));
				}
				
				AbstractLink navLink = navItem.linkHidingIfInvalid("navLink");
				
				item.add(
						navLink
								.add(
										new EnclosureContainer("icon")
												.condition(Condition.modelNotNull(navItem.getIconClassesModel()))
												.add(new ClassAttributeAppender(navItem.getIconClassesModel())),
										new CoreLabel("label", navItem.getLabelModel())
								)
								.add(new ClassAttributeAppender(navItem.getCssClassesModel()))
				);
				
				List<NavigationMenuItem> subMenuItems = navItem.getSubMenuItems();
				
				if (!subMenuItems.isEmpty()) {
					item.add(new ClassAttributeAppender("dropdown"));
					navLink.add(new ClassAttributeAppender("dropdown-toggle"));
					navLink.add(new AttributeModifier("data-toggle", "dropdown"));
					navLink.add(new AttributeModifier("data-hover", "dropdown"));
				}
				
				item.add(
						new WebMarkupContainer("subNavContainer")
								.add(
										new ListView<NavigationMenuItem>("subNav", subMenuItems) {
											private static final long serialVersionUID = -2257358650754295013L;
											
											@Override
											protected void populateItem(ListItem<NavigationMenuItem> item) {
												NavigationMenuItem navItem = item.getModelObject();
												
												AbstractLink navLink = navItem.linkHidingIfInvalid("navLink");
												navLink.add(new ClassAttributeAppender(navItem.getCssClassesModel()));
												navLink.add(
														new Label("label", navItem.getLabelModel()),
														new EnclosureContainer("icon").condition(Condition.modelNotNull(navItem.getIconClassesModel()))
																.add(new ClassAttributeAppender(navItem.getIconClassesModel()))
												);
												
												if (navItem.isActive(MainTemplate.this.getSecondMenuPage())) {
													navLink.add(new ClassAttributeAppender("active"));
												}
												
												item.add(navLink);
											}
											
											@Override
											protected void onDetach() {
												super.onDetach();
												Detachables.detach(getModelObject());
											}
										}
								)
								.setVisibilityAllowed(!subMenuItems.isEmpty())
				);
			}
			
			@Override
			protected void onDetach() {
				super.onDetach();
				Detachables.detach(getModelObject());
			}
		});
		
		add(
				new CoreLabel(
						"originalAuthentication",
						new StringResourceModel("authentication.originalAuthentication.help")
								.setParameters(BasicApplicationSession.get().getOriginalAuthentication() != null 
											? BasicApplicationSession.get().getOriginalAuthentication().getName()
											: null)
				) {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(BasicApplicationSession.get().getOriginalAuthentication() != null);
					}
				},
				
				ProfilePage.linkDescriptor()
						.link("profileLink")
						.add(
								new CoreLabel("userFullName", BindingModel.of(BasicApplicationSession.get().getUserModel(), Bindings.user().fullName()))
										.hideIfEmpty()
						),
				
				new AjaxLink<Void>("reconnexionLink") {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							BasicApplicationSession.get().signInAsMe();
							BasicApplicationSession.get().success(getString("authentication.back.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de la reconnexion de l'utilisateur.", e);
							Session.get().error(getString("signIn.error.unknown"));
						}
						throw LoginSuccessPage.linkDescriptor().newRestartResponseException();
					}
					
					@Override
					protected void onConfigure() {
						super.onConfigure();
						setVisible(BasicApplicationSession.get().getOriginalAuthentication() != null);
					}
				},
				
				new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class)
		);
		
		add(new ApplicationEnvironmentPanel("environment"));
		
		add(
				createBodyBreadCrumb("breadCrumb")
						.add(displayBreadcrumb().thenShow())
		);
		
		add(
				new CoreLabel("version", propertyService.get(VERSION))
						.add(new AttributeModifier("title", new StringResourceModel("common.version.full")
								.setParameters(ApplicationPropertyModel.of(VERSION), ApplicationPropertyModel.of(IGLOO_VERSION))
						))
		);
		
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		add(new BootstrapDropdownBehavior());
		add(new BootstrapHoverDropdownBehavior());
		
		add(new WebMarkupContainer("scrollToTop").add(new ScrollToTopBehavior()));
	}

	protected List<NavigationMenuItem> getMainNav() {
		return ImmutableList.of(
				BasicApplicationApplication.get().getHomePageLinkDescriptor().navigationMenuItem(new ResourceModel("navigation.home"))
						.setCssClassesModel(Model.of("home")),
				ReferenceDataPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.referenceData"))
						.setCssClassesModel(Model.of("reference-data")),
				AdministrationUserTypeDescriptor.BASIC_USER.list().navigationMenuItem(new ResourceModel("navigation.administration"))
						.setCssClassesModel(Model.of("administration"))
						.setSubMenuItems(ImmutableList.of(
								AdministrationUserTypeDescriptor.BASIC_USER.list().navigationMenuItem(new ResourceModel("navigation.administration.user.basic")),
								AdministrationUserTypeDescriptor.TECHNICAL_USER.list().navigationMenuItem(new ResourceModel("navigation.administration.user.technical")),
								AdministrationUserGroupListPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.administration.userGroup"))
						)),
				ConsoleMaintenanceSearchPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.console"))
						.setCssClassesModel(Model.of("console"))
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
		
		response.render(CssHeaderItem.forReference(StylesScssResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapCollapseJavaScriptResourceReference.get()));
	}
	
	protected BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setContainer("body");
		return bootstrapTooltip;
	}
}