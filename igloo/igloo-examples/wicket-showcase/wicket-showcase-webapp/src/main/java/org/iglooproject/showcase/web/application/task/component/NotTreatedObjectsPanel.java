package org.iglooproject.showcase.web.application.task.component;

import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import org.iglooproject.showcase.core.business.task.model.ShowcaseBatchReportBean;
import org.iglooproject.showcase.web.application.task.model.NotTreatedObjectNameModel;
import org.iglooproject.showcase.web.application.task.model.NotTreatedObjectsIdsModel;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.markup.html.panel.GenericPanel;
import org.iglooproject.wicket.more.markup.repeater.collection.CollectionView;
import org.iglooproject.wicket.more.util.model.Models;

public class NotTreatedObjectsPanel extends GenericPanel<ShowcaseBatchReportBean> {
	
	private static final long serialVersionUID = -1124957096738143838L;
	
	private final IModel<Set<Long>> idsModel;
	
	public NotTreatedObjectsPanel(String id, final IModel<ShowcaseBatchReportBean> reportBeanModel) {
		super(id, reportBeanModel);
		
		idsModel = new NotTreatedObjectsIdsModel(reportBeanModel);
		
		add(new CollectionView<Long>("notTreatedObjects", idsModel, Models.<Long>serializableModelFactory()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(Item<Long> item) {
				IModel<Long> idModel = item.getModel();
				item.add(
						new Label("id", idModel),
						new CoreLabel("name", new NotTreatedObjectNameModel(reportBeanModel, idModel))
				);
			}
		});
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		idsModel.detach();
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		Set<Long> ids = idsModel.getObject();
		setVisible(ids != null && !ids.isEmpty());
	}
}
