package fr.openwide.core.wicket.more.console.maintenance.infinispan.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.ImmutableSet;

import fr.openwide.core.jpa.more.property.JpaMoreInfinispanPropertyIds;
import fr.openwide.core.spring.property.model.PropertyId;
import fr.openwide.core.wicket.more.console.common.component.PropertyIdListPanel;
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
				new ConsoleMaintenanceInfinispanNodesPanel("nodes"),
				new PropertyIdListPanel("propertyIds", 
											ImmutableSet.<PropertyId<?>>builder()
													.add(
																JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED, 
																JpaMoreInfinispanPropertyIds.INFINISPAN_CLUSTER_NAME, 
																JpaMoreInfinispanPropertyIds.INFINISPAN_NODE_NAME,
																JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES
													)
													.build()
				)
		);
	}

	@Override
	protected Class<? extends ConsoleTemplate> getMenuItemPageClass() {
		return null;
	}

}
