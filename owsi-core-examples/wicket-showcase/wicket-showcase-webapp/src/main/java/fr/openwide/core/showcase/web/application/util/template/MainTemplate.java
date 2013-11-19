package fr.openwide.core.showcase.web.application.util.template;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.ShowcaseApplication;
import fr.openwide.core.showcase.web.application.ShowcaseSession;
import fr.openwide.core.showcase.web.application.links.page.LinksPage1;
import fr.openwide.core.showcase.web.application.others.page.ButtonsPage;
import fr.openwide.core.showcase.web.application.others.page.HideableComponentsPage;
import fr.openwide.core.showcase.web.application.others.page.TitlesPage;
import fr.openwide.core.showcase.web.application.portfolio.page.PortfolioMainPage;
import fr.openwide.core.showcase.web.application.task.page.TaskMainPage;
import fr.openwide.core.showcase.web.application.util.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.showcase.web.application.widgets.page.WidgetsMainPage;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.console.template.ConsoleConfiguration;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown.BootstrapDropdownBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
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
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<NavigationMenuItem> item) {
				NavigationMenuItem navItem = item.getModelObject();
				
				Link<Void> navLink = navItem.link("navLink");
				navLink.add(new Label("navLabel", navItem.getLabelModel()));
				
				item.setVisible(navItem.isAccessible());
				if (navItem.isActive(MainTemplate.this.getFirstMenuPage())) {
					item.add(new ClassAttributeAppender("active"));
				}
				
				item.add(navLink);
			}
		});
		
		// Second level navigation bar
		add(new ListView<NavigationMenuItem>("subNav", getSubNav()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<NavigationMenuItem> item) {
				NavigationMenuItem navItem = item.getModelObject();
				
				Link<Void> navLink = navItem.link("navLink");
				navLink.add(new Label("navLabel", navItem.getLabelModel()));
				
				item.setVisible(navItem.isAccessible());
				if (navItem.isActive(MainTemplate.this.getSecondMenuPage())) {
					item.add(new ClassAttributeAppender("active"));
				}
				
				item.add(navLink);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				List<NavigationMenuItem> navigationMenuItems = getModelObject();
				setVisible(navigationMenuItems != null && !navigationMenuItems.isEmpty());
			}
		});
		
		// User menu
		add(new HideableLabel("userFullName", new LoadableDetachableModel<String>() {
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
		}));
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));
		
		// Bread crumb
		Component breadCrumb;
		if (isBreadCrumbDisplayed()) {
			breadCrumb = createBodyBreadCrumb("breadCrumb");
		} else {
			breadCrumb = new EmptyPanel("breadCrumb");
		}
		add(breadCrumb);
		
		// Console
		add(ConsoleConfiguration.get().getConsoleLink("consoleLink"));
		
		// Tooltip
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		// Dropdown
		add(new BootstrapDropdownBehavior());
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
		return Lists.newArrayList(
				ShowcaseApplication.get().getHomePageLinkDescriptor().navigationMenuItem(new ResourceModel("navigation.home")),
				PortfolioMainPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.portfolio")),
				WidgetsMainPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.widgets")),
				TitlesPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.titles")),
				ButtonsPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.buttons")),
				HideableComponentsPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.hideableComponents")),
				LinksPage1.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.links")),
				TaskMainPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.tasks"))
		);
	}
	
	protected abstract List<NavigationMenuItem> getSubNav();
	
	protected boolean isBreadCrumbDisplayed() {
		return true;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(CssHeaderItem.forReference(StylesLessCssResourceReference.get()));
	}
}
