package fr.openwide.core.infinispan.utils.role;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.model.SimpleRole;
import fr.openwide.core.infinispan.service.IRolesProvider;

public class RolesFromStringSetProvider implements IRolesProvider {

	private final List<IRole> roles;

	public RolesFromStringSetProvider(Collection<String> rolesStrings) {
		super();
		List<IRole> roles = Lists.newArrayList();
		for (String role : rolesStrings) {
			roles.add(SimpleRole.from(role));
		}
		this.roles = ImmutableList.<IRole>copyOf(roles);
	}

	@Override
	public List<IRole> getRoles() {
		return roles;
	}

}
