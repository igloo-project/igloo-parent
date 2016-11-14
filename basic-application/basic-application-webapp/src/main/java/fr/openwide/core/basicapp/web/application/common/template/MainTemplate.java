package fr.openwide.core.basicapp.web.application.common.template;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static fr.openwide.core.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;
import static fr.openwide.core.spring.property.SpringPropertyIds.OWSI_CORE_VERSION;
import static fr.openwide.core.spring.property.SpringPropertyIds.VERSION;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.security.service.ISecurityManagementService;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserGroupPortfolioPage;
import fr.openwide.core.basicapp.web.application.common.component.EnvironmentPanel;
import fr.openwide.core.basicapp.web.application.common.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.AdministrationUserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.profile.page.ProfilePage;
import fr.openwide.core.basicapp.web.application.referencedata.page.ReferenceDataPage;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown.BootstrapDropdownBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstraphoverdropdown.BootstrapHoverDropdownBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.model.ApplicationPropertyModel;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.security.page.LoginSuccessPage;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

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
		
		// Page title
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		// Bread crumb
		Component breadCrumb;
		if (isBreadCrumbDisplayed()) {
			breadCrumb = createBodyBreadCrumb("breadCrumb");
		} else {
			breadCrumb = new InvisiblePanel("breadCrumb");
		}
		add(breadCrumb);
		
		// Environment
		add(new EnvironmentPanel("environment"));
		
		// Main navigation bar
		add(new ListView<NavigationMenuItem>("mainNav", getMainNav()) {
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
				
				if (navItem.isActive(MainTemplate.this.getFirstMenuPage())) {
					item.add(new ClassAttributeAppender("active"));
				}
				
				item.add(navLink);
				
				List<NavigationMenuItem> subMenuItems = navItem.getSubMenuItems();
				if (!subMenuItems.isEmpty()) {
					item.add(new ClassAttributeAppender("dropdown"));
					navLink.add(new ClassAttributeAppender("dropdown-toggle"));
					navLink.add(new AttributeModifier("data-toggle", "dropdown"));
					navLink.add(new AttributeModifier("data-hover", "dropdown"));
				}
				
				item.add(new ListView<NavigationMenuItem>("subNav", subMenuItems) {
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
							item.add(new ClassAttributeAppender("active"));
						}
						
						item.add(navLink);
					}
					
					@Override
					protected void onDetach() {
						super.onDetach();
						for (NavigationMenuItem item : getModelObject()) {
							item.detach();
						}
					}
				});
			}
			
			@Override
			protected void onDetach() {
				super.onDetach();
				for (NavigationMenuItem item : getModelObject()) {
					item.detach();
				}
			}
		});
		
		// User menu
		add(
				new Label(
						"originalAuthentication",
						new StringResourceModel("console.authentication.originalAuthentication.help")
								.setParameters(BasicApplicationSession.get().getOriginalAuthentication() != null 
											? BasicApplicationSession.get().getOriginalAuthentication().getName()
											: null)
						.getObject()
				) {
					
					private static final long serialVersionUID = -5789260462449121142L;
					
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
							BasicApplicationSession.get().success(getString("console.authentication.back.success"));
						} catch (Exception e) {
							LOGGER.error("Erreur lors de la reconnexion de l'utilisateur", e);
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
		
		// Footer
		add(
				new Label("version", propertyService.get(VERSION))
						.add(new AttributeModifier("title", new StringResourceModel("common.version.full")
								.setParameters(ApplicationPropertyModel.of(VERSION), ApplicationPropertyModel.of(OWSI_CORE_VERSION))
						))
		);
		
		// Tooltip
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		// Dropdown
		add(new BootstrapDropdownBehavior());
		add(new BootstrapHoverDropdownBehavior());
		
		// Scroll to top
		add(new WebMarkupContainer("scrollToTop").add(new ScrollToTopBehavior()));
	}

	protected List<NavigationMenuItem> getMainNav() {
		return ImmutableList.of(
				BasicApplicationApplication.get().getHomePageLinkDescriptor().navigationMenuItem(new ResourceModel("navigation.home"))
						.setCssClassesModel(Model.of("home")),
				ReferenceDataPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.referenceData"))
						.setCssClassesModel(Model.of("referenceData")),
				AdministrationUserTypeDescriptor.BASIC_USER.portfolio().navigationMenuItem(new ResourceModel("navigation.administration"))
						.setCssClassesModel(Model.of("administration"))
						.setSubMenuItems(ImmutableList.of(
								AdministrationUserTypeDescriptor.BASIC_USER.portfolio().navigationMenuItem(new ResourceModel("navigation.administration.user.basic")),
								AdministrationUserTypeDescriptor.TECHNICAL_USER.portfolio().navigationMenuItem(new ResourceModel("navigation.administration.user.technical")),
								AdministrationUserGroupPortfolioPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.administration.usergroup"))
						))
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
	
	protected boolean isBreadCrumbDisplayed() {
		return true;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(CssHeaderItem.forReference(StylesLessCssResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapCollapseJavaScriptResourceReference.get()));
	}
	
	@Override
	public String getVariation() {
		return BOOTSTRAP3_VARIATION;
	}
	
	protected BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setContainer("body");
		return bootstrapTooltip;
	}
}