package org.iglooproject.basicapp.web.application.history.component;

import java.util.List;
import java.util.Objects;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.core.business.history.model.HistoryLog;
import org.iglooproject.basicapp.core.security.model.BasicApplicationAuthorityConstants;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import org.iglooproject.functional.SerializablePredicate2;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.condition.Condition;
import org.iglooproject.wicket.more.model.BindingModel;

import com.google.common.collect.ImmutableList;

public class HistoryLogDetailColumnPanel extends GenericPanel<HistoryLog> {

	private static final long serialVersionUID = 1188689543635870482L;

	public HistoryLogDetailColumnPanel(String id, IModel<HistoryLog> model,
			IHistoryComponentFactory historyComponentFactory,
			final SerializablePredicate2<? super HistoryDifference> filter) {
		super(id, model);
		
		IModel<List<HistoryDifference>> historyDifferenceListModel = new IModel<List<HistoryDifference>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<HistoryDifference> getObject() {
				List<HistoryDifference> original = getModelObject().getDifferences();
				Objects.requireNonNull(original);
				if (filter == null) {
					return original;
				} else {
					return original.stream().filter(filter).collect(ImmutableList.toImmutableList());
				}
			}
		};
		
		add(
			new CoreLabel("id", BindingModel.of(model, Bindings.historyLog().id()))
				.add(Condition.role(BasicApplicationAuthorityConstants.ROLE_ADMIN).thenShow()),
			new CoreLabel("eventType", BindingModel.of(model, Bindings.historyLog().eventType())),
			new CoreLabel("comment", BindingModel.of(model, Bindings.historyLog().comment()))
				.multiline()
				.hideIfEmpty(),
			new HistoryDifferenceListPanel("differences", historyDifferenceListModel, historyComponentFactory)
		);
	}

}
