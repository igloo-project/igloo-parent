package org.iglooproject.wicket.bootstrap4.console.maintenance.queuemanager.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap4.console.maintenance.queuemanager.component.ConsoleMaintenanceQueueManagerNodePanel;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;

public class ConsoleMaintenanceQueueManagerPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 4288903243206618631L;

	public ConsoleMaintenanceQueueManagerPage(PageParameters parameters) {
		super(parameters);

		add(
				new ConsoleMaintenanceQueueManagerNodePanel("nodes")
		);
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceQueueManagerPage.class;
	}

}
