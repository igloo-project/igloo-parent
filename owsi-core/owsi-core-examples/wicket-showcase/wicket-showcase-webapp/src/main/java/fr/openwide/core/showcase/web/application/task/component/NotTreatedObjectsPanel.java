package fr.openwide.core.showcase.web.application.task.component;

import java.util.Set;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import fr.openwide.core.showcase.core.business.task.model.ShowcaseBatchReportBean;
import fr.openwide.core.showcase.web.application.task.model.NotTreatedObjectNameModel;
import fr.openwide.core.showcase.web.application.task.model.NotTreatedObjectsIdsModel;
import fr.openwide.core.wicket.markup.html.basic.CoreLabel;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;
import fr.openwide.core.wicket.more.markup.html.collection.SerializedItemSetView;

public class NotTreatedObjectsPanel extends GenericPanel<ShowcaseBatchReportBean> {
	
	private static final long serialVersionUID = -1124957096738143838L;
	
	private final IModel<Set<Long>> idsModel;
	
	public NotTreatedObjectsPanel(String id, final IModel<ShowcaseBatchReportBean> reportBeanModel) {
		super(id, reportBeanModel);
		
		idsModel = new NotTreatedObjectsIdsModel(reportBeanModel);
		
		add(new SerializedItemSetView<Long>("notTreatedObjects", idsModel) {
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
