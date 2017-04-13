package fr.openwide.core.test.infinispan.util.roles;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.infinispan.model.IRole;
import fr.openwide.core.infinispan.model.SimpleRole;
import fr.openwide.core.infinispan.service.IRolesProvider;

public class SimpleRolesProvider implements IRolesProvider {

	public static final SimpleRole ROLE_1 = SimpleRole.from("ROLE_1");
	public static final SimpleRole ROLE_2 = SimpleRole.from("ROLE_2");
	public static final SimpleRole ROLE_3 = SimpleRole.from("ROLE_3");
	public static final SimpleRole ROLE_4 = SimpleRole.from("ROLE_4");
	public static final SimpleRole ROLE_5 = SimpleRole.from("ROLE_5");
	public static final SimpleRole ROLE_6 = SimpleRole.from("ROLE_6");
	public static final SimpleRole ROLE_7 = SimpleRole.from("ROLE_7");
	public static final SimpleRole ROLE_8 = SimpleRole.from("ROLE_8");

	@Override
	public List<IRole> getRoles() {
		return ImmutableList.<IRole>copyOf(ROLES);
	}

	private static final SimpleRole[] ROLES = new SimpleRole[] {
		ROLE_1,
		ROLE_2,
		ROLE_3,
		ROLE_4,
		ROLE_5,
		ROLE_6,
		ROLE_7,
		ROLE_8
	};

}
