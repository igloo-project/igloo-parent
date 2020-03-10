package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.markup.html.basic.PlaceholderContainer;

public abstract class CoreActionColumnElementPanel<T> extends Panel {

	private static final long serialVersionUID = -1236107673112549105L;

	public CoreActionColumnElementPanel(String id, final IModel<T> rowModel) {
		super(id);
		
		Component link = getLink("link", rowModel);
		add(link);
		add(getPlaceholder("placeholder", rowModel).condition(Condition.componentVisible(link)));
		
		add(
			Condition.anyChildVisible(this)
				.thenShow()
		);
	}

	protected abstract Component getLink(String string, IModel<T> rowModel);

	protected abstract PlaceholderContainer getPlaceholder(String string, IModel<T> rowModel);

}
