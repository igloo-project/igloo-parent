package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.bootstrap.label.component.BootstrapBadge;

public abstract class CoreBootstrapBadgeLinkColumnPanel<T, S extends ISort<?>, C> extends GenericPanel<T> {

	private static final long serialVersionUID = -7049907356373179441L;

	public CoreBootstrapBadgeLinkColumnPanel(String id, IModel<T> rowModel) {
		super(id, rowModel);
		
		MarkupContainer link = getLink("link", rowModel);
		add(
				link.add(getBootstrapBadge("badge", rowModel)),
				getBootstrapBadge("badge", rowModel).add(Condition.componentVisible(link).thenHide()),
				getSideLink("sideLink", rowModel)
		);
	}

	public abstract BootstrapBadge<C> getBootstrapBadge(String wicketId, IModel<T> rowModel);

	public abstract MarkupContainer getLink(String wicketId, IModel<T> rowModel);
	
	public abstract MarkupContainer getSideLink(String wicketId, IModel<T> rowModel);
}
