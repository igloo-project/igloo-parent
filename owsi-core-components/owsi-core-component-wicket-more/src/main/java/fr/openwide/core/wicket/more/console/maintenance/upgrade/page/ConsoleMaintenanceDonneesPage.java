package fr.openwide.core.wicket.more.console.maintenance.upgrade.page;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.maintenance.upgrade.component.DataUpgradePanel;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;

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
