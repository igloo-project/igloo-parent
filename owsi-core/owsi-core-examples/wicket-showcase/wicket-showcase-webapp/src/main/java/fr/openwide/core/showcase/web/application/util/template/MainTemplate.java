package fr.openwide.core.showcase.web.application.util.template;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.ShowcaseApplication;
import fr.openwide.core.showcase.web.application.ShowcaseSession;
import fr.openwide.core.showcase.web.application.links.page.LinksPage1;
import fr.openwide.core.showcase.web.application.links.page.LinksPage2;
import fr.openwide.core.showcase.web.application.links.page.LinksPage3;
import fr.openwide.core.showcase.web.application.others.page.ButtonsPage;
import fr.openwide.core.showcase.web.application.others.page.HideableComponentsPage;
import fr.openwide.core.showcase.web.application.others.page.TitlesPage;
import fr.openwide.core.showcase.web.application.portfolio.page.PortfolioMainPage;
import fr.openwide.core.showcase.web.application.task.page.TaskMainPage;
import fr.openwide.core.showcase.web.application.util.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.showcase.web.application.widgets.page.AutocompletePage;
import fr.openwide.core.showcase.web.application.widgets.page.AutosizePage;
import fr.openwide.core.showcase.web.application.widgets.page.BootstrapJsPage;
import fr.openwide.core.showcase.web.application.widgets.page.CalendarPage;
import fr.openwide.core.showcase.web.application.widgets.page.CarouselPage;
import fr.openwide.core.showcase.web.application.widgets.page.FileDownloadPage;
import fr.openwide.core.showcase.web.application.widgets.page.FileUploadPage;
import fr.openwide.core.showcase.web.application.widgets.page.ListFilterPage;
import fr.openwide.core.showcase.web.application.widgets.page.ModalPage;
import fr.openwide.core.showcase.web.application.widgets.page.SelectBoxPage;
import fr.openwide.core.showcase.web.application.widgets.page.SortableListPage;
import fr.openwide.core.showcase.web.application.widgets.page.StatisticsPage;
import fr.openwide.core.showcase.web.application.widgets.page.WidgetsMainPage;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.console.template.ConsoleConfiguration;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureContainer;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown.BootstrapDropdownBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstraphoverdropdown.BootstrapHoverDropdownBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

public abstract class MainTemplate extends AbstractWebPageTemplate {
	private static final long serialVersionUID = -2487769225221281241L;
	
	public MainTemplate(PageParameters parameters) {
		super(parameters);
		
		// Feedback
		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel"));
		
		// Page title
		addHeadPageTitlePrependedElement(new BreadCrumbElement(new ResourceModel("common.rootPageTitle")));
		add(createHeadPageTitle("headPageTitle"));
		
		// Back to home
//		add(new BookmarkablePageLink<Void>("backToHomeLink", getApplication().getHomePage()));
		
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
		add(new CoreLabel("userFullName", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				String userFullName = null;
				User user = ShowcaseSession.get().getUser();
				if (user != null) {
					userFullName = user.getFullName();
				}
				return userFullName;
			}
		}).hideIfEmpty());
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));
		
		// Bread crumb
		Component breadCrumb;
		if (isBreadCrumbDisplayed()) {
			breadCrumb = createBodyBreadCrumb("breadCrumb");
		} else {
			breadCrumb = new InvisiblePanel("breadCrumb");
		}
		add(breadCrumb);
		
		// Console
		add(ConsoleConfiguration.get().getConsoleLink("consoleLink"));
		
		// Tooltip
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		// Dropdown
		add(new BootstrapDropdownBehavior());
		add(new BootstrapHoverDropdownBehavior());
	}
	
	protected BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setContainer("body");
		return bootstrapTooltip;
	}
	
	protected IModel<String> getApplicationNameModel() {
		return new ResourceModel("common.rootPageTitle");
	}
	
	protected List<NavigationMenuItem> getMainNav() {
		return ImmutableList.of(
				ShowcaseApplication.get().getHomePageLinkDescriptor().navigationMenuItem(new ResourceModel("navigation.home")),
				PortfolioMainPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.portfolio")),
				WidgetsMainPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.widgets"))
						.setSubMenuItems(ImmutableList.of(
								CalendarPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.calendar")),
								AutocompletePage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.autocomplete")),
								ModalPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.modal")),
								ListFilterPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.listFilter")),
								BootstrapJsPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.bootstrapJs")),
								CarouselPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.carousel")),
								StatisticsPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.statistics")),
								AutosizePage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.autosize")),
								SortableListPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.sortable")),
								SelectBoxPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.selectbox")),
								FileUploadPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.fileupload")),
								FileDownloadPage.linkDescriptor().navigationMenuItem(new ResourceModel("widgets.menu.filedownload"))
						)),
				TitlesPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.titles")),
				ButtonsPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.buttons")),
				HideableComponentsPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.hideableComponents")),
				LinksPage1.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.links"))
						.setSubMenuItems(ImmutableList.of(
								LinksPage1.linkDescriptor().navigationMenuItem(new ResourceModel("links.menu.page1")),
								LinksPage2.linkDescriptor().navigationMenuItem(new ResourceModel("links.menu.page2")),
								LinksPage3.linkDescriptor().navigationMenuItem(new ResourceModel("links.menu.page3"))
						)),
				TaskMainPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.tasks"))
		);
	}
	
	@Override
	protected Component createBodyBreadCrumb(String wicketId) {
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
}
