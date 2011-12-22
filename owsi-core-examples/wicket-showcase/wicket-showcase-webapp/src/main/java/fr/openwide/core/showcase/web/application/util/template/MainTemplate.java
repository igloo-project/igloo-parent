package fr.openwide.core.showcase.web.application.util.template;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.web.application.ShowcaseSession;
import fr.openwide.core.showcase.web.application.navigation.page.HomePage;
import fr.openwide.core.showcase.web.application.portfolio.page.PortfolioMainPage;
import fr.openwide.core.showcase.web.application.util.template.styles.StyleLessCssResourceReference;
import fr.openwide.core.showcase.web.application.widgets.page.WidgetsMainPage;
import fr.openwide.core.wicket.markup.html.basic.HideableLabel;
import fr.openwide.core.wicket.more.markup.html.feedback.GlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.AbstractWebPageTemplate;
import fr.openwide.core.wicket.more.markup.html.template.component.BreadCrumbPanel;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

public abstract class MainTemplate extends AbstractWebPageTemplate {
	private static final long serialVersionUID = -2487769225221281241L;
	
	private static final String[] ROUNDED_CORNERS_CSS_CLASSES = new String[] {
		// TODO
	};
	
	public MainTemplate(PageParameters parameters) {
		super(parameters);
		
		enableDefaultTipsyTooltips();
		enableCss3Pie(ROUNDED_CORNERS_CSS_CLASSES);
		
		add(new Label("headPageTitle", getHeadPageTitleModel()));
		
		add(new BookmarkablePageLink<Void>("logoBackToHomeLink", getApplication().getHomePage()));
		
		// Menu niveau 1
		addMenuElement(getFirstMenuPage(), "home", HomePage.class);
		addMenuElement(getFirstMenuPage(), "portfolio", PortfolioMainPage.class);
		addMenuElement(getFirstMenuPage(), "widgets", WidgetsMainPage.class);
		
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
		
		add(new BreadCrumbPanel("breadCrumb", getBreadCrumbElementsModel()));
		
		add(new GlobalFeedbackPanel("feedbackPanel"));
	}
	
	@Override
	protected String getRootPageTitleLabelKey() {
		return "common.rootPageTitle";
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.renderCSSReference(StyleLessCssResourceReference.get());
	}
}
