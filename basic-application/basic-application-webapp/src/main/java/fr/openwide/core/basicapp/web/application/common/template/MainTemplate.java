package fr.openwide.core.basicapp.web.application.common.template;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.basicapp.web.application.navigation.page.HomePage;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.console.template.ConsoleConfiguration;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.component.BreadCrumbPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

public abstract class MainTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = -1312989780696228848L;

	private List<BreadCrumbElement> pageTitleElements = Lists.newArrayList();

	public MainTemplate(PageParameters parameters) {
		super(parameters);
		
		MarkupContainer htmlRootElement = new TransparentWebMarkupContainer("htmlRootElement");
		htmlRootElement.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage()));
		add(htmlRootElement);
		
		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel"));
		
		add(new Label("headPageTitle", getHeadPageTitleModel()));
		
		// Bread crumb
		Component breadCrumb;
		if (isBreadCrumbDisplayed()) {
			breadCrumb = new BreadCrumbPanel("breadCrumb", getBreadCrumbElementsModel());
		} else {
			breadCrumb = new EmptyPanel("breadCrumb");
		}
		add(breadCrumb);
		
		// Main navigation bar
		add(new ListView<NavigationMenuItem>("mainNav", getMainNav()) {
			private static final long serialVersionUID = -2257358650754295013L;
			
			@Override
			protected void populateItem(ListItem<NavigationMenuItem> item) {
				NavigationMenuItem navItem = item.getModelObject();
				Class<? extends Page> navItemPageClass = navItem.getPageClass();
				
				BookmarkablePageLink<Void> navLink = new BookmarkablePageLink<Void>("navLink", navItemPageClass,
						navItem.getPageParameters());
				navLink.add(new Label("navLabel", navItem.getLabelModel()));
				
				item.setVisible(isPageAccessible(navItemPageClass));
				if (navItemPageClass.equals(MainTemplate.this.getFirstMenuPage())) {
					item.add(new ClassAttributeAppender("active"));
				}
				
				item.add(navLink);
			}
		});
		
		// User menu
		add(new HideableLabel("userFullName", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				String userFullName = null;
				User user = BasicApplicationSession.get().getUser();
				if (user != null) {
					userFullName = user.getFullName();
				}
				return userFullName;
			}
		}));
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));
		
		// Console
		add(ConsoleConfiguration.get().getConsoleLink("consoleLink"));
		
		// Tooltip
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
	}

	protected List<NavigationMenuItem> getMainNav() {
		return Lists.newArrayList(new NavigationMenuItem(new ResourceModel("navigation.home"), HomePage.class));
	}

	protected List<NavigationMenuItem> getSubNav() {
		return Lists.newArrayList();
	}

	protected void addHeadPageTitleElement(BreadCrumbElement pageTitleElement) {
		pageTitleElements.add(0, pageTitleElement);
	}

	@Override
	protected String getRootPageTitleLabelKey() {
		return "common.rootPageTitle";
	}

	public static BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setPlacement(BootstrapTooltip.Placement.BOTTOM);
		return bootstrapTooltip;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.renderCSSReference(StylesLessCssResourceReference.get());
	}

	protected boolean isBreadCrumbDisplayed() {
		return false;
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
}