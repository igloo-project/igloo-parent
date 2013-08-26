package fr.openwide.core.wicket.more.markup.html.template.model;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;

public class NavigationMenuItem implements IDetachable {

	private static final long serialVersionUID = -833923931725195545L;
	
	private IModel<String> labelModel;
	
	private IPageLinkDescriptor pageLinkDescriptor;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	public NavigationMenuItem(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	public NavigationMenuItem(IModel<String> labelModel, IPageLinkDescriptor pageLinkDescriptor) {
		this.labelModel = labelModel;
		this.pageLinkDescriptor = pageLinkDescriptor;
	}
	
	@Deprecated
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass) {
		this(labelModel, pageClass, null);
	}
	
	@Deprecated
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this.labelModel = labelModel;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
	}
	
	@Override
	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
		if (pageLinkDescriptor != null) {
			pageLinkDescriptor.detach();
		}
	}
	
	public IModel<String> getLabelModel() {
		return labelModel;
	}
	
	public void setLabelModel(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}

	public Link<Void> link(String wicketId) {
		if (pageLinkDescriptor != null) {
			return pageLinkDescriptor.link(wicketId);
		} else {
			return new BookmarkablePageLink<Void>(wicketId, pageClass, pageParameters);
		}
	}
	
	public boolean isActive(Class<? extends Page> selectedPage) {
		return pageClass.equals(selectedPage);
	}
	
	public boolean isAccessible() {
		return Session.get().getAuthorizationStrategy().isInstantiationAuthorized(pageClass);
	}

}
