package org.iglooproject.wicket.bootstrap3.console.maintenance.task.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.component.TaskFilterPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.component.TaskManagerInformationPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.component.TaskResultsPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import org.iglooproject.wicket.bootstrap3.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ConsoleMaintenanceTaskListPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -4085517848301335018L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleMaintenanceTaskListPage.class);
	}

	public ConsoleMaintenanceTaskListPage(PageParameters parameters) {
		super(parameters);

		addHeadPageTitleKey("console.maintenance.tasks");

		QueuedTaskHolderDataProvider queuedTaskHolderDataProvider = new QueuedTaskHolderDataProvider();

		TaskResultsPanel resultsPanel = new TaskResultsPanel("resultsPanel", queuedTaskHolderDataProvider, 20);
		add(resultsPanel);

		add(new TaskManagerInformationPanel("taskManagerInformationPanel"));

		add(new TaskFilterPanel("filterPanel", queuedTaskHolderDataProvider, resultsPanel.getDataTable()));
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceTaskListPage.class;
	}
}
