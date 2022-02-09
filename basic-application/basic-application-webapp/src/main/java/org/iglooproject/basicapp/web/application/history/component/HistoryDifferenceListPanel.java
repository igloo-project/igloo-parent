package org.iglooproject.basicapp.web.application.history.component;

import java.util.List;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.web.application.history.component.factory.IHistoryComponentFactory;
import org.iglooproject.basicapp.web.application.history.renderer.HistoryDifferencePathRenderer;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.model.GenericEntityModel;

public class HistoryDifferenceListPanel extends GenericPanel<List<HistoryDifference>> {

	private static final long serialVersionUID = 1188689543635870482L;

	private final IHistoryComponentFactory historyComponentFactory;

	public HistoryDifferenceListPanel(String id, IModel<List<HistoryDifference>> model,
			IHistoryComponentFactory historyComponentFactory) {
		super(id, model);
		this.historyComponentFactory = historyComponentFactory;
		add(Condition.collectionModelNotEmpty(getModel()).thenShowInternal());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new CollectionView<HistoryDifference>("item", getModel(), GenericEntityModel.<HistoryDifference>factory()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(Item<HistoryDifference> item) {
				item.add(
					new CoreLabel("path", HistoryDifferencePathRenderer.get().asModel(item.getModel())),
					historyComponentFactory.create("difference", item.getModel())
				);
			}
		});
	}

}