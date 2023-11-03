package org.iglooproject.wicket.more.markup.repeater.table.column;

import org.apache.wicket.model.IModel;

public abstract class CoreActionColumnSimpleElementPanel<T> extends CoreActionColumnElementPanel<T> {

	private static final long serialVersionUID = 1L;

	public CoreActionColumnSimpleElementPanel(String id, final IModel<T> rowModel) {
		super(id, rowModel);
	}

}
