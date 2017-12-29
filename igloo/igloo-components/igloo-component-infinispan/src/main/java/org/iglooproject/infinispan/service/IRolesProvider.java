package org.iglooproject.infinispan.service;

import java.util.List;

import org.iglooproject.infinispan.model.IRole;

public interface IRolesProvider {

	/**
	 * @return all known roles (by configuration) ; this list may not include roles added by other nodes.
	 */
	List<IRole> getRoles();

	/**
	 * @return all roles that local node can capture on rebalance
	 */
	List<IRole> getRebalanceRoles();

}
