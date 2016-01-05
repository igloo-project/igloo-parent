package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.html.factory.ComponentFactories;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;

public class CoreActionColumnPanel<T> extends Panel {

	private static final long serialVersionUID = -1236107673112549105L;

	public CoreActionColumnPanel(String id, final IModel<T> rowModel,
			List<? extends IOneParameterComponentFactory<?, IModel<T>>> factories) {
		super(id);

		RepeatingView actions = new RepeatingView("actions");
		ComponentFactories.addAll(actions, factories, rowModel);
		add(actions);
	}

}
