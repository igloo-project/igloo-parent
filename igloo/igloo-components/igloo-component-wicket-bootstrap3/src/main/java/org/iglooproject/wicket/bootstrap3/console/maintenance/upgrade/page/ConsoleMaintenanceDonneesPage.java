package org.iglooproject.wicket.bootstrap3.console.maintenance.upgrade.page;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.bootstrap3.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap3.console.maintenance.upgrade.component.DataUpgradePanel;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;

public class ConsoleMaintenanceDonneesPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = -6149952103369498125L;

	public ConsoleMaintenanceDonneesPage(PageParameters parameters) {
		super(parameters);
		
		if (dataUpgradeService == null) {
			throw new RestartResponseException(getMenuSectionPageClass());
		}
		
		addHeadPageTitleKey("console.maintenance.donnees");
		
		add(new DataUpgradePanel("dataUpgradesPanel"));
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return ConsoleMaintenanceDonneesPage.class;
	}
}
