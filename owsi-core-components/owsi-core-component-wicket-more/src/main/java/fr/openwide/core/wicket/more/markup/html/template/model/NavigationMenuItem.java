package fr.openwide.core.wicket.more.markup.html.template.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.markup.html.link.BlankLink;

public class NavigationMenuItem implements IDetachable {

	private static final long serialVersionUID = -833923931725195545L;
	
	private IModel<String> labelModel;
	
	private IModel<String> cssClassesModel = Model.of("");
	
	private IPageLinkGenerator pageLinkGenerator;
	
	private final List<NavigationMenuItem> subMenuItems = Lists.newArrayList();
	
	protected NavigationMenuItem(NavigationMenuItem wrapped) {
		setLabelModel(wrapped.getLabelModel());
		setPageLinkGenerator(wrapped.getPageLinkGenerator());
		setSubMenuItems(wrapped.getSubMenuItems());
	}
	
	public NavigationMenuItem(IModel<String> labelModel) {
		this.labelModel = labelModel;
	}
	
	public NavigationMenuItem(IModel<String> labelModel, IPageLinkGenerator pageLinkGenerator,
			Collection<NavigationMenuItem> subMenuItems) {
		this.labelModel = labelModel;
		this.pageLinkGenerator = pageLinkGenerator;
		this.subMenuItems.addAll(subMenuItems);
	}
	
	public NavigationMenuItem(IModel<String> labelModel, IPageLinkGenerator pageLinkGenerator) {
		this(labelModel, pageLinkGenerator, ImmutableList.<NavigationMenuItem>of());
	}
	
	@Override
	public void detach() {
		if (labelModel != null) {
			labelModel.detach();
		}
		if (pageLinkGenerator != null) {
			pageLinkGenerator.detach();
		}
		
		for (NavigationMenuItem subMenuItem : subMenuItems) {
			subMenuItem.detach();
		}
	}

	public AbstractLink link(String wicketId) {
		if (pageLinkGenerator != null) {
			return pageLinkGenerator.link(wicketId);
		} else {
			return new BlankLink(wicketId);
		}
	}
	
	public boolean isActive(Class<? extends Page> selectedPage) {
		if (pageLinkGenerator != null) {
			return pageLinkGenerator.isActive(selectedPage);
		} else {
			return false;
		}
	}
	
	public boolean isAccessible() {
		if (pageLinkGenerator != null) {
			return pageLinkGenerator.isAccessible();
		} else {
			return true;
		}
	}
	
	public IModel<String> getLabelModel() {
		return labelModel;
	}
	
	public NavigationMenuItem setLabelModel(IModel<String> labelModel) {
		this.labelModel = labelModel;
		return this;
	}
	
	protected IPageLinkGenerator getPageLinkGenerator() {
		return pageLinkGenerator;
	}

	protected NavigationMenuItem setPageLinkGenerator(IPageLinkGenerator pageLinkGenerator) {
		this.pageLinkGenerator = pageLinkGenerator;
		return this;
	}
	
	public List<NavigationMenuItem> getSubMenuItems() {
		return Collections.unmodifiableList(subMenuItems);
	}
	
	public NavigationMenuItem addSubMenuItem(NavigationMenuItem subMenuItem) {
		this.subMenuItems.add(subMenuItem);
		return this;
	}
	
	public NavigationMenuItem setSubMenuItems(Collection<NavigationMenuItem> subMenuItems) {
		Collection<NavigationMenuItem> menuItems = ImmutableList.copyOf(subMenuItems); // Handle collection views
		this.subMenuItems.clear();
		this.subMenuItems.addAll(menuItems);
		return this;
	}

	public IModel<String> getCssClassesModel() {
		return cssClassesModel;
	}

	public NavigationMenuItem setCssClassesModel(IModel<String> cssClassesModel) {
		this.cssClassesModel = cssClassesModel;
		return this;
	}

}
