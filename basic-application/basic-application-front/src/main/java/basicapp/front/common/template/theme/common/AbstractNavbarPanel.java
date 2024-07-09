package basicapp.front.common.template.theme.common;

import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.markup.html.template.model.NavigationMenuItem;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.EnclosureContainer;

public abstract class AbstractNavbarPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public AbstractNavbarPanel(String id) {
		super(id);
	}

	protected void addNavItem(
		String navItemWicketId,
		IPageLinkDescriptor pageLinkGenerator,
		Class<? extends Page> clazz,
		Function<IPageLinkGenerator, Component> function
	) {
		EnclosureContainer navItem = new EnclosureContainer(navItemWicketId);
		Component component = function.apply(pageLinkGenerator);
		
		addActiveClass(pageLinkGenerator, clazz, navItem);
		
		add(
			navItem
				.anyChildVisible()
				.add(component)
		);
	}

	protected void addActiveClass(
		ListItem<NavigationMenuItem> item,
		Class<? extends Page> clazz,
		Component component
	) {
		addActiveClass(
			item.getModelObject()::isActive,
			clazz,
			component
		);
	}

	protected void addActiveClass(
		IPageLinkGenerator pageLinkGenerator,
		Class<? extends Page> clazz,
		Component component
	) {
		addActiveClass(
			pageLinkGenerator::isActive,
			clazz,
			component
		);
	}

	private void addActiveClass(
		Predicate<Class<? extends Page>> predicate,
		Class<? extends Page> clazz,
		Component component
	) {
		if (predicate.test(clazz)) {
			component.add(new ClassAttributeAppender("active"));
		}
	}

}
