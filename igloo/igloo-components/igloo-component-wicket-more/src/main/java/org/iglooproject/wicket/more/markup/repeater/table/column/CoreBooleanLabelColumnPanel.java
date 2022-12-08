package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.more.markup.html.image.BooleanIcon;

import igloo.wicket.markup.html.panel.GenericPanel;

public abstract class CoreBooleanLabelColumnPanel<S extends ISort<?>> extends GenericPanel<Boolean> {

	private static final long serialVersionUID = 2599140137273335611L;

	public CoreBooleanLabelColumnPanel(String id, IModel<Boolean> rowModel) {
		super(id, rowModel);
		
		add(
				decorate(new BooleanIcon("value", rowModel))
		);
	}

	protected abstract BooleanIcon decorate(BooleanIcon booleanIcon);
}
