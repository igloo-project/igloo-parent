package fr.openwide.core.wicket.more.markup.repeater.table.column;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.condition.Condition;

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
