package fr.openwide.core.infinispan.model;

import java.io.Serializable;

public class SimpleRole implements IRole, Serializable {

	private static final long serialVersionUID = -2703012259547691866L;

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

	public static final SimpleRole from(String role) {
		return new SimpleRole(role);
	}

}
