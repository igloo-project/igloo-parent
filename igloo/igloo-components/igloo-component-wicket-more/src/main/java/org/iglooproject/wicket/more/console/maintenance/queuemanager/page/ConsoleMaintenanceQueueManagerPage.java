package org.iglooproject.wicket.more.console.maintenance.queuemanager.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.iglooproject.wicket.more.console.maintenance.queuemanager.component.ConsoleMaintenanceQueueManagerNodePanel;
import org.iglooproject.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.more.console.template.ConsoleTemplate;

public class ConsoleMaintenanceQueueManagerPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 4288903243206618631L;

	public ConsoleMaintenanceQueueManagerPage(PageParameters parameters) {
		super(parameters);

		add(
				new ConsoleMaintenanceQueueManagerNodePanel("nodes")
		);
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return null;
	}

}
