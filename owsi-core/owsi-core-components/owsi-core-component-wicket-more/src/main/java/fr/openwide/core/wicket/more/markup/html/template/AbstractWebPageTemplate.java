package fr.openwide.core.wicket.more.markup.html.template;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.behavior.ClassAttributeAppender;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class AbstractWebPageTemplate extends CoreWebPage {

	private static final long serialVersionUID = -5598937641577320345L;

	private static final String DEFAULT_HEAD_PAGE_TITLE_SEPARATOR = " › ";
	
	private static final String DEFAULT_HEAD_PAGE_TITLE_SEPARATOR_REVERSE = " ‹ ";
	
	protected List<BreadCrumbElement> headPageTitleElements = Lists.newArrayList();
	
	protected List<BreadCrumbElement> breadCrumbElements = Lists.newArrayList();
	
	protected boolean headPageTitleReversed = false;
	
	protected String headPageTitleSeparator = DEFAULT_HEAD_PAGE_TITLE_SEPARATOR;
	
	protected String headPageTitleSeparatorReverse = DEFAULT_HEAD_PAGE_TITLE_SEPARATOR_REVERSE;
	
	public AbstractWebPageTemplate(PageParameters parameters) {
		super(parameters);
	}
	
	protected void addMenuElement(Class<? extends Page> selectedPageClass, String name, Class<? extends Page> pageClass) {
		addMenuElement(this, selectedPageClass, name, pageClass, null, true);
	}
	
	protected void addMenuElement(Class<? extends Page> selectedPageClass, String name, Class<? extends Page> pageClass,
			boolean isVisible) {
		addMenuElement(this, selectedPageClass, name, pageClass, null, isVisible);
	}
	
	protected void addMenuElement(Class<? extends Page> selectedPageClass, String name, Class<? extends Page> pageClass,
			PageParameters parameters) {
		addMenuElement(this, selectedPageClass, name, pageClass, parameters, true);
	}
	
	protected void addMenuElement(Class<? extends Page> selectedPageClass, String name, Class<? extends Page> pageClass,
			PageParameters parameters, boolean isVisible) {
		addMenuElement(this, selectedPageClass, name, pageClass, parameters, isVisible);
	}
	
	protected void addMenuElement(MarkupContainer menuContainer,
			Class<? extends Page> selectedPageClass,
			String name,
			Class<? extends Page> pageClass,
			PageParameters parameters,
			boolean isVisible) {
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(name + "MenuLink", pageClass, parameters);
		link.setVisible(isVisible && isPageAccessible(pageClass));
		
		MarkupContainer container = new WebMarkupContainer(name + "MenuLinkContainer");
		if (pageClass.equals(selectedPageClass)) {
			container.add(new ClassAttributeAppender("active"));
		}
		container.add(link);
		
		menuContainer.add(container);
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
	
	protected void addHeadPageTitleElement(BreadCrumbElement breadCrumbElement) {
		this.headPageTitleElements.add(breadCrumbElement);
	}
	
	protected void setHeadPageTitleReversed(boolean headPageTitleReversed) {
		this.headPageTitleReversed = headPageTitleReversed;
	}
	
	protected void setHeadPageTitleSeparator(String headPageTitleSeparator) {
		this.headPageTitleSeparator = headPageTitleSeparator;
	}
	
	protected void setHeadPageTitleSeparatorReverse(String headPageTitleSeparatorReverse) {
		this.headPageTitleSeparatorReverse = headPageTitleSeparatorReverse;
	}
	
	/**
	 * This method is public so that Wicket's PropertyModel can find it.
	 */
	public String getHeadPageTitle() {
		StringBuilder sb = new StringBuilder();
		
		if (!headPageTitleReversed) {
			sb.append(getLocalizer().getString(getRootPageTitleLabelKey(), this));
		}
		
		if (headPageTitleElements.size() > 0) {
			List<BreadCrumbElement> orderedHeadPageTitleElements;
			String separator;
			
			if (headPageTitleReversed) {
				orderedHeadPageTitleElements = ImmutableList.copyOf(headPageTitleElements).reverse();
				separator = headPageTitleSeparatorReverse;
			} else {
				orderedHeadPageTitleElements = ImmutableList.copyOf(headPageTitleElements);
				separator = headPageTitleSeparator;
			}
			for (BreadCrumbElement pageTitleElement : orderedHeadPageTitleElements) {
				IModel<String> titleElementModel = pageTitleElement.getLabelModel();
				if (titleElementModel instanceof IComponentAssignedModel) {
					titleElementModel = wrap(titleElementModel);
				}
				if (sb.length() > 0) {
					sb.append(separator);
				}
				sb.append(titleElementModel.getObject());
			}
		} else {
			boolean oneElementBreadcrumb = (breadCrumbElements.size() == 1);
			
			List<BreadCrumbElement> orderedBreadCrumbElements;
			String separator;
			
			if (headPageTitleReversed) {
				orderedBreadCrumbElements = ImmutableList.copyOf(breadCrumbElements).reverse();
				separator = headPageTitleSeparatorReverse;
			} else {
				orderedBreadCrumbElements = ImmutableList.copyOf(breadCrumbElements);
				separator = headPageTitleSeparator;
			}
			
			for (BreadCrumbElement breadCrumbElement : orderedBreadCrumbElements) {
				if (oneElementBreadcrumb || !getApplication().getHomePage().equals(breadCrumbElement.getPageClass())) {
					IModel<String> breadcrumbElementModel = breadCrumbElement.getLabelModel();
					if (breadcrumbElementModel instanceof IComponentAssignedModel) {
						breadcrumbElementModel = wrap(breadcrumbElementModel);
					}
					if (sb.length() > 0) {
						sb.append(separator);
					}
					sb.append(breadcrumbElementModel.getObject());
				}
			}
		}
		
		if (headPageTitleReversed) {
			if (sb.length() > 0) {
				sb.append(headPageTitleSeparatorReverse);
			}
			sb.append(getLocalizer().getString(getRootPageTitleLabelKey(), this));
		}
		
		return sb.toString();
	}

}