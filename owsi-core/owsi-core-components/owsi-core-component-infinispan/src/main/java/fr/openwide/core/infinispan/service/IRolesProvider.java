package fr.openwide.core.infinispan.service;

import java.util.List;

import fr.openwide.core.infinispan.model.IRole;

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
