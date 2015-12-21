package fr.openwide.core.basicapp.web.application.history.component;

import java.util.List;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.core.business.history.model.HistoryLog;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.more.model.BindingModel;

public class HistoryLogDetailColumnPanel extends GenericPanel<HistoryLog> {

	private static final long serialVersionUID = 1188689543635870482L;
	
	public HistoryLogDetailColumnPanel(String id, IModel<HistoryLog> model,
			IHistoryComponentFactory historyComponentFactory, final Predicate<? super HistoryDifference> filter) {
		super(id, model);
		
		IModel<List<HistoryDifference>> historyDifferenceListModel = new AbstractReadOnlyModel<List<HistoryDifference>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<HistoryDifference> getObject() {
				List<HistoryDifference> original = getModelObject().getDifferences();
				if (filter == null) {
					return original;
				} else {
					return Lists.newArrayList(Iterables.filter(
							original,
							filter
					));
				}
			}
		};
		
		add(
				new CoreLabel("action", BindingModel.of(model, Bindings.historyLog().eventType())),
				new HistoryDifferenceListPanel("differences", historyDifferenceListModel, historyComponentFactory)
		);
	}
	
}
