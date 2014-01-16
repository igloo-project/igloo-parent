package fr.openwide.core.basicapp.web.application.common.template;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserPortfolioPage;
import fr.openwide.core.basicapp.web.application.common.component.EnvironmentPanel;
import fr.openwide.core.basicapp.web.application.common.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.collapse.BootstrapCollapseJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown.BootstrapDropdownBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop.ScrollToTopBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

public abstract class MainTemplate extends AbstractWebPageTemplate {

	private static final long serialVersionUID = -1312989780696228848L;

	@SpringBean
	private BasicApplicationConfigurer configurer;

	public MainTemplate(PageParameters parameters) {
		super(parameters);
		
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
			breadCrumb = new EmptyPanel("breadCrumb");
		}
		add(breadCrumb);
		
		// Environment
		add(new EnvironmentPanel("environment"));
		
		// Back to home
		add(new BookmarkablePageLink<Void>("backToHomeLink", getApplication().getHomePage()));
		
		// Main navigation bar
		add(new ListView<NavigationMenuItem>("mainNav", getMainNav()) {
			private static final long serialVersionUID = -2257358650754295013L;
			
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
				User user = BasicApplicationSession.get().getUser();
				if (user != null) {
					userFullName = user.getFullName();
				}
				return userFullName;
			}
		}));
		add(new BookmarkablePageLink<Void>("logoutLink", LogoutPage.class));

		// Footer
		add(new Label("version", configurer.getVersion()));
		
		// Tooltip
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
		
		// Dropdown
		add(new BootstrapDropdownBehavior());
		
		// Scroll to top
		add(new WebMarkupContainer("scrollToTop")
				.add(new ScrollToTopBehavior()));
	}

	protected List<NavigationMenuItem> getMainNav() {
		return ImmutableList.of(
				BasicApplicationApplication.get().getHomePageLinkDescriptor().navigationMenuItem(new ResourceModel("navigation.home")),
				AdministrationUserPortfolioPage.linkDescriptor().navigationMenuItem(new ResourceModel("navigation.administration"))
		);
	}

	protected List<NavigationMenuItem> getSubNav() {
		return ImmutableList.of();
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return null;
	}
	
	@Override
	protected Component createBodyBreadCrumb(String wicketId) {
		return new BodyBreadCrumbPanel(wicketId, bodyBreadCrumbPrependedElementsModel, breadCrumbElementsModel)
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