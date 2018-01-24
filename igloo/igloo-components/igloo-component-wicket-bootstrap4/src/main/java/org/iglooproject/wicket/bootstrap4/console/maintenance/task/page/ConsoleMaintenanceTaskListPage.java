package org.iglooproject.wicket.bootstrap4.console.maintenance.task.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap4.console.maintenance.task.component.TaskFilterPanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.task.component.TaskManagerInformationPanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.task.component.TaskResultsPanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.more.console.maintenance.task.model.QueuedTaskHolderDataProvider;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class ConsoleMaintenanceTaskListPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -4085517848301335018L;

	public static final IPageLinkDescriptor linkDescriptor() {
		return LinkDescriptorBuilder.start()
				.page(ConsoleMaintenanceTaskListPage.class);
	}

	public ConsoleMaintenanceTaskListPage(PageParameters parameters) {
		super(parameters);
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.tasks")));
		
		QueuedTaskHolderDataProvider queuedTaskHolderDataProvider = new QueuedTaskHolderDataProvider();
		
		TaskResultsPanel resultsPanel = new TaskResultsPanel("resultsPanel", queuedTaskHolderDataProvider, 20);
		add(resultsPanel);
		
		add(new TaskManagerInformationPanel("taskManagerInformationPanel"));
		
		add(new TaskFilterPanel("filterPanel", queuedTaskHolderDataProvider, resultsPanel.getDataTable()));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceTaskListPage.class;
	}

}
