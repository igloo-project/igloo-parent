package org.iglooproject.commons.util.security;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface PermissionObject {
	
	String DEFAULT_PERMISSION_OBJECT_NAME = "permissionObject";
	
	String value() default DEFAULT_PERMISSION_OBJECT_NAME;

}
