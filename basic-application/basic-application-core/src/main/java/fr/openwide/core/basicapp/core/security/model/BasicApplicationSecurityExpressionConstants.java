package fr.openwide.core.basicapp.core.security.model;

import static fr.openwide.core.commons.util.security.PermissionObject.DEFAULT_PERMISSION_OBJECT_NAME;

public final class BasicApplicationSecurityExpressionConstants {
	
	public static final String READ = "hasPermission(#" + DEFAULT_PERMISSION_OBJECT_NAME + ", '" + BasicApplicationPermissionConstants.READ + "')";
	public static final String CREATE = "hasPermission(#" + DEFAULT_PERMISSION_OBJECT_NAME + ", '" + BasicApplicationPermissionConstants.CREATE + "')";
	public static final String WRITE = "hasPermission(#" + DEFAULT_PERMISSION_OBJECT_NAME + ", '" + BasicApplicationPermissionConstants.WRITE + "')";
	public static final String DELETE = "hasPermission(#" + DEFAULT_PERMISSION_OBJECT_NAME + ", '" + BasicApplicationPermissionConstants.DELETE + "')";

	private BasicApplicationSecurityExpressionConstants() {
	}
	
}
