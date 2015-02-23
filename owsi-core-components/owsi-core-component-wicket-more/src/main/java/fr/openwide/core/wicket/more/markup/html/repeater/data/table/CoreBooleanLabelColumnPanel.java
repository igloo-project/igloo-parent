package fr.openwide.core.wicket.more.markup.html.repeater.data.table;

import org.apache.wicket.model.IModel;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.image.BooleanIcon;

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
