package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.repeater.FactoryRepeatingView;

public class CoreActionColumnPanel<T> extends Panel {

	private static final long serialVersionUID = -1236107673112549105L;

	public CoreActionColumnPanel(String id, final IModel<T> rowModel,
			List<? extends IOneParameterComponentFactory<?, IModel<T>>> factories) {
		super(id);

		FactoryRepeatingView actions = new FactoryRepeatingView("actions");
		actions.addAll(factories, rowModel);
		add(actions);
	}

}
