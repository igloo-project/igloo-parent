package org.iglooproject.wicket.more.markup.html.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;
import org.iglooproject.wicket.more.markup.html.template.component.BodyBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.component.HeadPageTitleBreadCrumbPanel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class AbstractWebPageTemplate extends CoreWebPage {

	private static final long serialVersionUID = -5598937641577320345L;
	
	@Deprecated
	public static final String BOOTSTRAP3_VARIATION = "bs3";
	public static final String BOOTSTRAP4_VARIATION = "bs4";
	
	protected static final String DEFAULT_HEAD_PAGE_TITLE_SEPARATOR = " › ";
	
	protected static final String DEFAULT_HEAD_PAGE_TITLE_SEPARATOR_REVERSE = " ‹ ";
	
	protected final IModel<List<BreadCrumbElement>> headPageTitlePrependedElementsModel = newBreadCrumbListModel();
	protected final IModel<List<BreadCrumbElement>> headPageTitleElementsModel = newBreadCrumbListModel();
	
	protected final IModel<Boolean> headPageTitleReversedModel = Model.of(false);
	protected final IModel<String> headPageTitleSeparatorModel = Model.of(DEFAULT_HEAD_PAGE_TITLE_SEPARATOR);
	protected final IModel<String> headPageTitleSeparatorReverseModel = Model.of(DEFAULT_HEAD_PAGE_TITLE_SEPARATOR_REVERSE);
	
	protected final IModel<List<BreadCrumbElement>> bodyBreadCrumbPrependedElementsModel = newBreadCrumbListModel();
	protected final IModel<List<BreadCrumbElement>> breadCrumbElementsModel = newBreadCrumbListModel();
	
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
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<>(name + "MenuLink", pageClass, parameters);
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
	
	private static IModel<List<BreadCrumbElement>> newBreadCrumbListModel() {
		return new ListModel<>(new ArrayList<BreadCrumbElement>());
	}
	
	/** Add a breadcrumb element to be preprended to HTML head title only (not to HTML body breadcrumb)
	 */
	protected final void addHeadPageTitlePrependedElement(BreadCrumbElement breadCrumbElement) {
		headPageTitlePrependedElementsModel.getObject().add(breadCrumbElement);
	}
	
	/** Add a breadcrumb element to be preprended to HTML body breadcrumb only (not to HTML head title)
	 */
	protected final void addBodyBreadCrumbPrependedElement(BreadCrumbElement breadCrumbElement) {
		bodyBreadCrumbPrependedElementsModel.getObject().add(breadCrumbElement);
	}

	/** Add a breadcrumb element that will be shown in the page title. If none is added, the {@link #breadCrumbElementsModel} will be used.
	 */
	protected final void addHeadPageTitleElement(BreadCrumbElement breadCrumbElement) {
		this.headPageTitleElementsModel.getObject().add(breadCrumbElement);
	}
	
	protected final void addBreadCrumbElement(BreadCrumbElement breadCrumbElement) {
		breadCrumbElementsModel.getObject().add(breadCrumbElement);
	}
	
	protected final void setHeadPageTitleReversed(boolean headPageTitleReversed) {
		this.headPageTitleReversedModel.setObject(headPageTitleReversed);
	}
	
	protected Component createHeadPageTitle(String wicketId) {
		return new HeadPageTitleBreadCrumbPanel(
				wicketId,
				headPageTitlePrependedElementsModel,
				headPageTitleElementsModel, breadCrumbElementsModel,
				headPageTitleSeparatorModel,
				headPageTitleSeparatorReverseModel,
				headPageTitleReversedModel
		);
	}
	
	protected Component createBodyBreadCrumb(String wicketId) {
		return new BodyBreadCrumbPanel(wicketId, bodyBreadCrumbPrependedElementsModel, breadCrumbElementsModel);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		headPageTitlePrependedElementsModel.detach();
		headPageTitleElementsModel.detach();
		headPageTitleReversedModel.detach();
		headPageTitleSeparatorModel.detach();
		headPageTitleSeparatorReverseModel.detach();
		
		bodyBreadCrumbPrependedElementsModel.detach();
		
		breadCrumbElementsModel.detach();
	}
}