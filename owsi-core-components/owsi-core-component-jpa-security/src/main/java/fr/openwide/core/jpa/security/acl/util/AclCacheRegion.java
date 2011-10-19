package fr.openwide.core.jpa.security.acl.util;

public enum AclCacheRegion {

	ACLS("core.acls");
	
	private String name;
	
	private AclCacheRegion(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
