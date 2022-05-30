package org.iglooproject.wicket.more.markup.html.template.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.commons.util.collections.CollectionUtils;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.api.util.Detachables;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.markup.html.link.BlankLink;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class NavigationMenuItem implements IDetachable {

	private static final long serialVersionUID = -833923931725195545L;

	private IModel<String> labelModel;

	private IPageLinkGenerator pageLinkGenerator;

	private IModel<String> cssClassesModel = Model.of("");

	private IModel<String> iconClassesModel = Model.of("");

	private IModel<Boolean> subMenuForceOpenModel = Condition.alwaysFalse();

	private final List<NavigationMenuItem> subMenuItems = Lists.newArrayList();

	public NavigationMenuItem(IModel<String> labelModel) {
		this(labelModel, null);
	}

	public NavigationMenuItem(
		IModel<String> labelModel,
		IPageLinkGenerator pageLinkGenerator
	) {
		this(labelModel, pageLinkGenerator, ImmutableList.of());
	}

	public NavigationMenuItem(
		IModel<String> labelModel,
		IPageLinkGenerator pageLinkGenerator,
		Collection<NavigationMenuItem> subMenuItems
	) {
		label(labelModel);
		pageLinkGenerator(pageLinkGenerator);
		subMenuItems(subMenuItems);
	}

	public AbstractLink link(String wicketId) {
		if (pageLinkGenerator != null) {
			return pageLinkGenerator.link(wicketId);
		} else {
			return new BlankLink(wicketId);
		}
	}

	public AbstractLink linkHidingIfInvalid(String wicketId) {
		if (pageLinkGenerator != null) {
			return pageLinkGenerator.link(wicketId).hideIfInvalid();
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

	public IModel<String> getLabelModel() {
		return labelModel;
	}

	public NavigationMenuItem label(String label) {
		return label(Model.of(label));
	}

	public NavigationMenuItem label(IModel<String> labelModel) {
		this.labelModel = labelModel;
		return this;
	}

	public IPageLinkGenerator getPageLinkGenerator() {
		return pageLinkGenerator;
	}

	public NavigationMenuItem pageLinkGenerator(IPageLinkGenerator pageLinkGenerator) {
		this.pageLinkGenerator = pageLinkGenerator;
		return this;
	}

	public IModel<String> getCssClassesModel() {
		return cssClassesModel;
	}

	public NavigationMenuItem cssClasses(String cssClasses) {
		return cssClasses(Model.of(cssClasses));
	}

	public NavigationMenuItem cssClasses(IModel<String> cssClassesModel) {
		this.cssClassesModel = cssClassesModel;
		return this;
	}

	public IModel<String> getIconClassesModel() {
		return iconClassesModel;
	}

	public NavigationMenuItem iconClasses(String iconClasses) {
		return iconClasses(Model.of(iconClasses));
	}

	public NavigationMenuItem iconClasses(IModel<String> iconClassesModel) {
		this.iconClassesModel = iconClassesModel;
		return this;
	}

	public IModel<Boolean> getSubMenuForceOpenModel() {
		return subMenuForceOpenModel;
	}

	public NavigationMenuItem subMenuForceOpen() {
		return subMenuForceOpen(true);
	}

	public NavigationMenuItem subMenuForceOpen(Boolean subMenuForceOpen) {
		return subMenuForceOpen(Model.of(subMenuForceOpen));
	}

	public NavigationMenuItem subMenuForceOpen(IModel<Boolean> subMenuForceOpenModel) {
		this.subMenuForceOpenModel = subMenuForceOpenModel;
		return this;
	}

	public List<NavigationMenuItem> getSubMenuItems() {
		return Collections.unmodifiableList(subMenuItems);
	}

	public NavigationMenuItem subMenuItems(NavigationMenuItem firstSubMenuItem, NavigationMenuItem ... otherSubMenuItems) {
		return subMenuItems(Lists.asList(firstSubMenuItem, otherSubMenuItems));
	}

	public NavigationMenuItem subMenuItems(Collection<NavigationMenuItem> subMenuItems) {
		CollectionUtils.replaceAll(this.subMenuItems, subMenuItems);
		return this;
	}

	@Override
	public void detach() {
		Detachables.detach(
			labelModel,
			cssClassesModel,
			iconClassesModel,
			pageLinkGenerator,
			subMenuForceOpenModel
		);
		Detachables.detach(subMenuItems);
	}

}
