package org.iglooproject.infinispan.utils.role;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.iglooproject.infinispan.model.IRole;
import org.iglooproject.infinispan.model.SimpleRole;
import org.iglooproject.infinispan.service.IRolesProvider;

public class RolesFromStringSetProvider implements IRolesProvider {

	private final List<IRole> roles;

	private final List<IRole> rolesRebalance;

	public RolesFromStringSetProvider(Collection<String> rolesStrings, Collection<String> rolesRebalance) {
		super();
		{
			List<IRole> roles = Lists.newArrayList();
			for (String role : rolesStrings) {
				roles.add(SimpleRole.from(role));
			}
			this.roles = ImmutableList.<IRole>copyOf(roles);
		}
		{
			if (rolesRebalance != null) {
				List<IRole> roles = Lists.newArrayList();
				for (String role : rolesRebalance) {
					roles.add(SimpleRole.from(role));
				}
				this.rolesRebalance = ImmutableList.<IRole>copyOf(roles);
			} else {
				this.rolesRebalance = roles;
			}
		}
	}

	@Override
	public List<IRole> getRoles() {
		return roles;
	}

	@Override
	public List<IRole> getRebalanceRoles() {
		return rolesRebalance;
	}

}
