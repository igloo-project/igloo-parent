package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.component.CoreLabel;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;

public abstract class CoreLabelLinkColumnPanel<T, S extends ISort<?>> extends GenericPanel<T> {

	private static final long serialVersionUID = 2599140137273335611L;

	public CoreLabelLinkColumnPanel(String id, IModel<T> rowModel) {
		super(id, rowModel);
		
		MarkupContainer link = getLink("link", rowModel);
		add(
				link.add(getLabel("label", rowModel)),
				getLabel("label", rowModel).add(Condition.componentVisible(link).thenHide()),
				getSideLink("sideLink", rowModel)
		);
	}

	public abstract CoreLabel getLabel(String wicketId, IModel<T> rowModel);

	public abstract MarkupContainer getLink(String wicketId, IModel<T> rowModel);
	
	public abstract MarkupContainer getSideLink(String wicketId, IModel<T> rowModel);
}
