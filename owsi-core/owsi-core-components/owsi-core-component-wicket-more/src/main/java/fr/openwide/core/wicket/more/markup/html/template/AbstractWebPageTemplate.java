package fr.openwide.core.wicket.more.markup.html.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.IWiQueryPlugin;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;
import fr.openwide.core.wicket.more.markup.html.template.model.NavigationMenuItem;

public abstract class AbstractWebPageTemplate extends CoreWebPage implements IWiQueryPlugin {

	private static final long serialVersionUID = -5598937641577320345L;

	private static final String META_TITLE_SEPARATOR = " › ";
	
	private List<String> pageTitleElements = new ArrayList<String>();
	
	private List<BreadCrumbElement> breadCrumbElements = new ArrayList<BreadCrumbElement>();
	
	public AbstractWebPageTemplate(PageParameters parameters) {
		super(parameters);
	}
	
	protected abstract Class<? extends WebPage> getFirstMenuPage();
	
	protected abstract Class<? extends WebPage> getSecondMenuPage();
	
	protected abstract List<NavigationMenuItem> getMainNav();
	
	protected abstract List<NavigationMenuItem> getSubNav();
	
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
			boolean oneElementBreadcrumb = (breadCrumbElements.size() == 1);
			
			for (BreadCrumbElement breadCrumbElement : breadCrumbElements) {
				if (oneElementBreadcrumb || !getApplication().getHomePage().equals(breadCrumbElement.getPageClass())) {
					sb.append(META_TITLE_SEPARATOR);
					sb.append(breadCrumbElement.getLabel());
				}
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public JsStatement statement() {
		return null;
	}
}