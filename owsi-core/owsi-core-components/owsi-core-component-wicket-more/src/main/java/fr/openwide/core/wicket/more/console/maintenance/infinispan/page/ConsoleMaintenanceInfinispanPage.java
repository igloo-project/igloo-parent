package fr.openwide.core.wicket.more.console.maintenance.infinispan.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import fr.openwide.core.wicket.more.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanClusterPanel;
import fr.openwide.core.wicket.more.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanLocksPanel;
import fr.openwide.core.wicket.more.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanNodesPanel;
import fr.openwide.core.wicket.more.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanRolesPanel;
import fr.openwide.core.wicket.more.console.maintenance.template.ConsoleMaintenanceTemplate;
import fr.openwide.core.wicket.more.console.template.ConsoleTemplate;

public class ConsoleMaintenanceInfinispanPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 2373051508004389589L;

	public ConsoleMaintenanceInfinispanPage(PageParameters parameters) {
		super(parameters);
		
		add(
				new ConsoleMaintenanceInfinispanClusterPanel("cluster"),
				new ConsoleMaintenanceInfinispanRolesPanel("roles"),
				new ConsoleMaintenanceInfinispanLocksPanel("locks"),
				new ConsoleMaintenanceInfinispanNodesPanel("nodes")
		);
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return null;
	}

}
