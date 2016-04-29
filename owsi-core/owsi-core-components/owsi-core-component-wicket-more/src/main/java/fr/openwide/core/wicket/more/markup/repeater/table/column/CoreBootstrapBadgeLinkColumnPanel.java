package fr.openwide.core.wicket.more.markup.repeater.table.column;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.component.BootstrapBadge;

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
