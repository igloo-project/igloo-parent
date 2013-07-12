package fr.openwide.core.jpa.security.model;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.security.acls.model.Permission;

public class CoreNamedPermission implements Permission {

	private static final long serialVersionUID = 4702776652617722298L;

	public static final CoreNamedPermission READ = new CoreNamedPermission(CorePermissionConstants.READ);
	public static final CoreNamedPermission WRITE = new CoreNamedPermission(CorePermissionConstants.WRITE);
	public static final CoreNamedPermission CREATE = new CoreNamedPermission(CorePermissionConstants.CREATE);
	public static final CoreNamedPermission DELETE = new CoreNamedPermission(CorePermissionConstants.DELETE);
	public static final CoreNamedPermission ADMINISTRATION = new CoreNamedPermission(CorePermissionConstants.ADMINISTRATION);
	public static final CoreNamedPermission ALLOWED = new CoreNamedPermission(CorePermissionConstants.ALLOWED);
	public static final CoreNamedPermission DENIED = new CoreNamedPermission(CorePermissionConstants.DENIED);

	protected String name;

	protected CoreNamedPermission(String name) {
		this.name = name;
	}

	@Override
	public int getMask() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPattern() {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return name;
	}

	@Override
	public final String toString() {
		return name;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object instanceof CoreNamedPermission) {
			CoreNamedPermission permission = (CoreNamedPermission) object;
			return ObjectUtils.equals(name, permission.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.hashCode(name);
	}

}
