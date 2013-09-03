package fr.openwide.core.wicket.more.markup.html.template.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;

public class NavigationMenuItem implements IDetachable {

	private static final long serialVersionUID = -833923931725195545L;
	
	private IModel<String> labelModel;
	
	private IPageLinkDescriptor pageLinkDescriptor;
	
	private Class<? extends Page> pageClass;
	
	private PageParameters pageParameters;
	
	private final List<NavigationMenuItem> subMenuItems = Lists.newArrayList();
	
	public NavigationMenuItem(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters,
			IPageLinkDescriptor pageLinkDescriptor, Collection<NavigationMenuItem> subMenuItems) {
		this.labelModel = labelModel;
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
		this.pageLinkDescriptor = pageLinkDescriptor;
		this.subMenuItems.addAll(subMenuItems);
	}
	
	public NavigationMenuItem(IModel<String> labelModel,
			Class<? extends Page> pageClass, PageParameters pageParameters,
			IPageLinkDescriptor pageLinkDescriptor) {
		this(labelModel, pageClass, pageParameters, pageLinkDescriptor,
				Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
	}
	
	@Deprecated
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass) {
		this(labelModel, pageClass, null, null, Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
	}
	
	@Deprecated
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters) {
		this(labelModel, pageClass, pageParameters, null, Lists.<NavigationMenuItem>newArrayListWithExpectedSize(0));
	}
	
	@Deprecated
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass,
			Collection<NavigationMenuItem> subMenuItems) {
		this(labelModel, pageClass, null, null, subMenuItems);
	}
	
	@Deprecated
	public NavigationMenuItem(IModel<String> labelModel, Class<? extends Page> pageClass, PageParameters pageParameters,
			Collection<NavigationMenuItem> subMenuItems) {
		this(labelModel, pageClass, pageParameters, null, subMenuItems);
	}
	
	@Override
	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
		if (pageLinkDescriptor != null) {
			pageLinkDescriptor.detach();
		}
		
		for (NavigationMenuItem subMenuItem : subMenuItems) {
			subMenuItem.detach();
		}
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
	
	public IModel<String> getLabelModel() {
		return labelModel;
	}
	
	public void setLabelModel(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	protected IPageLinkDescriptor getPageLinkDescriptor() {
		return pageLinkDescriptor;
	}

	protected void setPageLinkDescriptor(IPageLinkDescriptor pageLinkDescriptor) {
		this.pageLinkDescriptor = pageLinkDescriptor;
	}

	protected Class<? extends Page> getPageClass() {
		return pageClass;
	}

	protected void setPageClass(Class<? extends Page> pageClass) {
		this.pageClass = pageClass;
	}

	protected PageParameters getPageParameters() {
		return pageParameters;
	}

	protected void setPageParameters(PageParameters pageParameters) {
		this.pageParameters = pageParameters;
	}
	
	public List<NavigationMenuItem> getSubMenuItems() {
		return Collections.unmodifiableList(subMenuItems);
	}
	
	public void addSubMenuItem(NavigationMenuItem subMenuItem) {
		this.subMenuItems.add(subMenuItem);
	}
	
	public void setSubMenuItems(Collection<NavigationMenuItem> subMenuItems) {
		Collection<NavigationMenuItem> menuItems = ImmutableList.copyOf(subMenuItems); // Handle collection views
		this.subMenuItems.clear();
		this.subMenuItems.addAll(menuItems);
	}

}
