package org.iglooproject.wicket.more.markup.repeater.table.column;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.markup.repeater.FactoryRepeatingView;

import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IOneParameterComponentFactory;

public class CoreActionColumnPanel<T> extends Panel {

	private static final long serialVersionUID = -1236107673112549105L;

	public CoreActionColumnPanel(String id, final IModel<T> rowModel,
			List<? extends IOneParameterComponentFactory<?, IModel<T>>> factories) {
		super(id);
		
		FactoryRepeatingView actions = new FactoryRepeatingView("actions");
		actions.addAll(factories, rowModel);
		
		add(
				new WebMarkupContainer("actionsContainer")
					.add(actions)
					.add(Condition.anyChildVisible(actions).thenShow())
		);
	}

}
