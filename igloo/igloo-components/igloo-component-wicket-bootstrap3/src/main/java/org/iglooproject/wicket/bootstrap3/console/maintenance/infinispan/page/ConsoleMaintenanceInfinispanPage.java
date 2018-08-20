package org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.page;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.jpa.more.property.JpaMoreInfinispanPropertyIds;
import org.iglooproject.spring.property.model.PropertyId;
import org.iglooproject.wicket.bootstrap3.console.common.component.PropertyIdListPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanClusterPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanLocksPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanNodesPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanRolesPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.infinispan.component.ConsoleMaintenanceInfinispanRolesRequestsPanel;
import org.iglooproject.wicket.bootstrap3.console.maintenance.template.ConsoleMaintenanceTemplate;
import org.iglooproject.wicket.bootstrap3.console.template.ConsoleTemplate;

import com.google.common.collect.ImmutableSet;

public class ConsoleMaintenanceInfinispanPage extends ConsoleMaintenanceTemplate {

	private static final long serialVersionUID = 2373051508004389589L;

	public ConsoleMaintenanceInfinispanPage(PageParameters parameters) {
		super(parameters);
		
		add(
				new ConsoleMaintenanceInfinispanClusterPanel("cluster"),
				new ConsoleMaintenanceInfinispanRolesPanel("roles"),
				new ConsoleMaintenanceInfinispanLocksPanel("locks"),
				new ConsoleMaintenanceInfinispanNodesPanel("nodes"),
				new ConsoleMaintenanceInfinispanRolesRequestsPanel("rolesRequests"),
				new PropertyIdListPanel("propertyIds", 
											ImmutableSet.<PropertyId<?>>builder()
													.add(
																JpaMoreInfinispanPropertyIds.INFINISPAN_ENABLED, 
																JpaMoreInfinispanPropertyIds.INFINISPAN_CLUSTER_NAME, 
																JpaMoreInfinispanPropertyIds.INFINISPAN_NODE_NAME,
																JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES,
																JpaMoreInfinispanPropertyIds.INFINISPAN_ROLES_REBALANCE
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
