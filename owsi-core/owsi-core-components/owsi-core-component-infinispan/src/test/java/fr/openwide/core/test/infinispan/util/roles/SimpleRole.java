package fr.openwide.core.test.infinispan.util.roles;

import java.io.Serializable;

import fr.openwide.core.infinispan.model.IRole;

public class SimpleRole implements IRole, Serializable {

	private static final long serialVersionUID = -2703012259547691866L;

	public static final SimpleRole ROLE_1 = new SimpleRole("ROLE_1");
	public static final SimpleRole ROLE_2 = new SimpleRole("ROLE_2");
	public static final SimpleRole ROLE_3 = new SimpleRole("ROLE_3");
	public static final SimpleRole ROLE_4 = new SimpleRole("ROLE_4");
	public static final SimpleRole ROLE_5 = new SimpleRole("ROLE_5");
	public static final SimpleRole ROLE_6 = new SimpleRole("ROLE_6");
	public static final SimpleRole ROLE_7 = new SimpleRole("ROLE_7");
	public static final SimpleRole ROLE_8 = new SimpleRole("ROLE_8");

	private final String key;

	private SimpleRole(String key) {
		super();
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return String.format("Role<%s>", key);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleRole) {
			return key.equals(((IRole) obj).getKey());
		} else {
			return false;
		}
	}

	public static final SimpleRole[] values() {
		return ROLES;
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
