package org.iglooproject.wicket.bootstrap4.console.maintenance.upgrade.page;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap4.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap4.console.maintenance.upgrade.component.DataUpgradePanel;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;

public class ConsoleMaintenanceDonneesPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -6149952103369498125L;

	public ConsoleMaintenanceDonneesPage(PageParameters parameters) {
		super(parameters);
		
		if (dataUpgradeService == null) {
			throw new RestartResponseException(getFirstMenuPage());
		}
		
		addBreadCrumbElement(new BreadCrumbElement(new ResourceModel("console.maintenance.donnees")));
		
		add(new DataUpgradePanel("dataUpgradesPanel"));
	}

	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return ConsoleMaintenanceDonneesPage.class;
	}

}
