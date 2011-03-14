package fr.openwide.core.wicket.more.markup.html.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.core.javascript.JsUtils;

import fr.openwide.core.wicket.markup.html.util.css3pie.Css3PieHeaderContributor;
import fr.openwide.core.wicket.more.AbstractCoreSession;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.Tipsy;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy.TipsyBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class AbstractWebPageTemplate extends WebPage {
	
	private static final String META_TITLE_SEPARATOR = " › ";
	
	private List<String> pageTitleElements = new ArrayList<String>();
	
	private List<BreadCrumbElement> breadCrumbElements = new ArrayList<BreadCrumbElement>();
	
	public AbstractWebPageTemplate(PageParameters parameters) {
		super(parameters);
	}
	
	protected void addMenuElement(Class<? extends Page> selectedPageClass, String name, Class<? extends Page> pageClass) {
		addMenuElement(selectedPageClass, name, pageClass, null);
	}
	
	protected void addMenuElement(Class<? extends Page> selectedPageClass, String name, Class<? extends Page> pageClass, PageParameters parameters) {
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(name + "MenuLinkContainer", pageClass, parameters);
		link.setVisible(isPageAccessible(pageClass));
		
		MarkupContainer container = new WebMarkupContainer(name + "MenuLink");
		if (pageClass.equals(selectedPageClass)) {
			link.add(new AttributeAppender("class", true, new Model<String>("selected"), " "));
		}
		link.add(container);
		
		add(link);
	}
	
	protected boolean isPageAccessible(Class<? extends Page> pageClass) {
		return AbstractCoreSession.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass);
	}
	
	protected abstract Class<? extends WebPage> getFirstMenuPage();
	
	protected abstract Class<? extends WebPage> getSecondMenuPage();
	
	protected void addBreadCrumbElement(BreadCrumbElement breadCrumbElement) {
		breadCrumbElements.add(breadCrumbElement);
	}
	
	protected IModel<List<BreadCrumbElement>> getBreadCrumbElementsModel() {
		return new PropertyModel<List<BreadCrumbElement>>(this, "breadCrumbElements");
	}
	
	public List<BreadCrumbElement> getBreadCrumbElements() {
		return this.breadCrumbElements;
	}
	
	protected abstract String getRootPageTitleLabelKey();
	
	protected IModel<String> getHeadPageTitleModel() {
		return new PropertyModel<String>(this, "headPageTitle");
	}
	
	protected void addPageTitleElement(String key) {
		this.pageTitleElements.add(key);
	}
	
	public String getHeadPageTitle() {
		StringBuilder sb = new StringBuilder(getLocalizer().getString(getRootPageTitleLabelKey(), this));
		if (pageTitleElements.size() > 0) {
			for (String pageTitleElement : pageTitleElements) {
				sb.append(META_TITLE_SEPARATOR);
				sb.append(getLocalizer().getString(pageTitleElement, this, pageTitleElement));
			}
		} else {
			for (BreadCrumbElement breadCrumbElement : breadCrumbElements) {
				sb.append(META_TITLE_SEPARATOR);
				sb.append(getLocalizer().getString(breadCrumbElement.getLabelKey(), this, breadCrumbElement.getLabelKey()));
			}
		}
		
		return sb.toString();
	}
	
	protected void enableTipsyTooltips() {
		final Tipsy tipsy = new Tipsy();
		tipsy.setFade(true);
		tipsy.setLive(true);
		tipsy.setTitle(JsUtils.quotes("data-tooltip"));
		
		add(new TipsyBehavior("[data-tooltip]", tipsy));
	}
	
	protected void enableCss3Pie(String[] styles) {
		add(Css3PieHeaderContributor.forStyles(styles));
	}
}