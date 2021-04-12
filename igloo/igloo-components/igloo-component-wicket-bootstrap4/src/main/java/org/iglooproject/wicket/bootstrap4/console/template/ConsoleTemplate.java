package org.iglooproject.wicket.bootstrap4.console.template;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE;

import java.util.List;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.more.business.upgrade.service.IAbstractDataUpgradeService;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.bootstrap4.console.maintenance.search.page.ConsoleMaintenanceSearchPage;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.dropdown.BootstrapDropDownJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip.BootstrapTooltip;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItem;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuSection;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.basic.EnclosureContainer;
import org.iglooproject.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.dropdown.BootstrapDropdownBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.security.page.LogoutPage;

public abstract class ConsoleTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = -477123413708677528L;

	@SpringBean
	protected IPropertyService propertyService;

	@SpringBean(required = false)
	protected IAbstractDataUpgradeService dataUpgradeService;

	public ConsoleTemplate(PageParameters parameters) {
		super(parameters);
		
		add(
			new TransparentWebMarkupContainer("htmlElement")
				.add(AttributeAppender.append("lang", AbstractCoreSession.get().getLocale().getLanguage()))
		);
		
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		EnclosureContainer navbarNavContainer = new EnclosureContainer("navbarNavContainer");
		add(navbarNavContainer.anyChildVisible());
		
		navbarNavContainer.add(
			getApplicationHomePageLinkDescriptor()
				.link("applicationHomePageLink")
		);
		
		List<ConsoleMenuSection> menuItems = ConsoleConfiguration.get().getMenuSections();
		
		navbarNavContainer.add(
			new ListView<ConsoleMenuSection>("navbarNavItems", menuItems) {
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void populateItem(ListItem<ConsoleMenuSection> item) {
					ConsoleMenuSection navItem = item.getModelObject();
					AbstractLink navLink = new BookmarkablePageLink<Void>("navLink", navItem.getPageClass());
					
					item.add(navLink);
					
					navLink
						.add(
							new CoreLabel("label", new ResourceModel(navItem.getDisplayStringKey()))
						);
					
					EnclosureContainer navbarNavSubContainer = new EnclosureContainer("navbarNavSubContainer");
					item.add(
						navbarNavSubContainer
							.anyChildVisible()
							.setOutputMarkupId(true)
					);
					
					Condition isSubVisibleCondition = Condition.componentVisible(navbarNavSubContainer);
					
					IModel<Boolean> isSubActiveModel = Model.of(false);
					Condition isSubActiveCondition = Condition.isTrue(isSubActiveModel);
					
					List<ConsoleMenuItem> subMenuItems = navItem.getMenuItems();
					
					navbarNavSubContainer
						.add(
							new ListView<ConsoleMenuItem>("navbarNavSubItems", subMenuItems) {
								private static final long serialVersionUID = -2257358650754295013L;
								
								@Override
								protected void populateItem(ListItem<ConsoleMenuItem> item) {
									ConsoleMenuItem navItem = item.getModelObject();
									AbstractLink navLink = new BookmarkablePageLink<Void>("navLink", navItem.getPageClass());
									
									item.add(navLink);
									
									navLink.add(
										new CoreLabel("label", new ResourceModel(navItem.getDisplayStringKey()))
									);
									
									if (Objects.equals(navItem.getPageClass(), ConsoleTemplate.this.getSecondMenuPage())) {
										navLink.add(new ClassAttributeAppender("active"));
										isSubActiveModel.setObject(true);
									}
								}
							}
								.setVisibilityAllowed(!subMenuItems.isEmpty())
						);
					
					navLink.add(
						new ClassAttributeAppender(
							isSubVisibleCondition
								.then(Model.of("dropdown-toggle"))
								.otherwise(Model.of(""))
						)
					);
					
					navLink.add(
						new Behavior() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onComponentTag(Component component, ComponentTag tag) {
								tag.put("data-toggle", "dropdown");
								tag.put("data-hover", "dropdown");
								tag.put("href", "#");
								super.onComponentTag(component, tag);
							}
							@Override
							public boolean isEnabled(Component component) {
								return isSubVisibleCondition.applies();
							}
						}
					);
					
					item.add(
						new ClassAttributeAppender(
							isSubVisibleCondition
								.then(Model.of("dropdown"))
								.otherwise(Model.of(""))
						)
					);
					
					item.add(
						new ClassAttributeAppender(
							Condition.or(
								isSubVisibleCondition.and(isSubActiveCondition),
								Condition.isTrue(() -> Objects.equals(navItem.getPageClass(), ConsoleTemplate.this.getFirstMenuPage()))
							)
								.then(Model.of("active"))
								.otherwise(Model.of())
						)
					);
				}
			}
				.setVisibilityAllowed(!menuItems.isEmpty())
		);
		
		add(new CoreLabel("userFullName", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected String load() {
				String userFullName = null;
				GenericUser<?, ?> user = AbstractCoreSession.get().getUser();
				if (user != null) {
					userFullName = user.getFullName();
				}
				return userFullName;
			}
		}).hideIfEmpty());
		
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));
		
		add(ConsoleConfiguration.get().getConsoleHeaderEnvironmentComponentFactory().create("headerEnvironment"));
		
		add(ConsoleConfiguration.get().getConsoleHeaderAdditionalContentComponentFactory().create("headerAdditionalContent"));
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("common.application"), getApplicationHomePageLinkDescriptor()));
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("common.console"), ConsoleMaintenanceSearchPage.linkDescriptor()));
		
		add(
			createBodyBreadCrumb("breadCrumb")
				.add(displayBreadcrumb().thenShow())
		);
		
		add(
			new CoreLabel("applicationVersion", new StringResourceModel("common.version.application", ApplicationPropertyModel.of(SpringPropertyIds.VERSION))),
			new CoreLabel("iglooVersion", new StringResourceModel("common.version.igloo", ApplicationPropertyModel.of(SpringPropertyIds.IGLOO_VERSION)))
		);
		
		add(new AnimatedGlobalFeedbackPanel("feedback", propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_VALUE), propertyService.get(CONSOLE_GLOBAL_FEEDBACK_AUTOHIDE_DELAY_UNIT)));
		
		add(new WebMarkupContainer("scrollToTop").add(new ScrollToTopBehavior()));
		
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		add(new BootstrapDropdownBehavior());
	}

	protected IPageLinkDescriptor getApplicationHomePageLinkDescriptor() {
		return LinkDescriptorBuilder.start().page(getApplication().getHomePage());
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

	protected BootstrapTooltip getBootstrapTooltip() {
		return new BootstrapTooltip()
			.selector("[title],[data-original-title]")
			.animation(true)
			.container("body");
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		for (ResourceReference cssResourceReference : ConsoleConfiguration.get().getCssResourcesReferences()) {
			response.render(CssHeaderItem.forReference(cssResourceReference));
		}
		response.render(JavaScriptHeaderItem.forReference(BootstrapCollapseJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapDropDownJavaScriptResourceReference.get()));
	}

	@Override
	public String getVariation() {
		return AbstractWebPageTemplate.BOOTSTRAP4_VARIATION;
	}

}
