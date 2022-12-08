package igloo.console.maintenance.task.component;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.wicket.more.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import org.iglooproject.wicket.more.markup.html.form.LabelPlaceholderBehavior;

import igloo.wicket.markup.html.form.PageableSearchForm;

public class ConsoleMaintenanceTaskSearchPanel extends Panel {

	private static final long serialVersionUID = -3803340118726908397L;

	public ConsoleMaintenanceTaskSearchPanel(String id, IPageable pageable, QueuedTaskHolderDataProvider dataProvider) {
		super(id);
		
		add(
			new PageableSearchForm<>("form", pageable)
				.add(
					new TextField<String>("name", dataProvider.getNameModel())
						.setLabel(new ResourceModel("console.maintenance.task.common.name"))
						.add(new LabelPlaceholderBehavior()),
					new TaskTypeDropDownMultipleChoice("taskTypes", dataProvider.getTaskTypesModel(), Suppliers2.arrayList())
						.setLabel(new ResourceModel("console.maintenance.task.common.taskType"))
						.add(new LabelPlaceholderBehavior()),
					new TaskStatusDropDownMultipleChoice("statuses", dataProvider.getStatusesModel(), Suppliers2.arrayList())
						.setLabel(new ResourceModel("console.maintenance.task.common.status"))
						.add(new LabelPlaceholderBehavior()),
					new TaskResultDropDownMultipleChoice("results", dataProvider.getResultsModel(), Suppliers2.arrayList())
						.setLabel(new ResourceModel("console.maintenance.task.common.result"))
						.add(new LabelPlaceholderBehavior()),
					new TaskQueueIdDropDownMultipleChoice("queueIds", dataProvider.getQueueIdsModel(), Suppliers2.arrayList())
						.setLabel(new ResourceModel("console.maintenance.task.common.queue"))
						.add(new LabelPlaceholderBehavior())
				)
		);
	}

}
