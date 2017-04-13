package fr.openwide.core.test.infinispan.util.roles;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.service.IRolesProvider;

public class SimpleRolesProvider implements IRolesProvider {

	@Override
	public List<IRole> getRoles() {
		return ImmutableList.<IRole>copyOf(SimpleRole.values());
	}

}
