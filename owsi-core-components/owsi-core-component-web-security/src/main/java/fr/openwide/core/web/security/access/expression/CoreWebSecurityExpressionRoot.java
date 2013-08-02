package fr.openwide.core.web.security.access.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import fr.openwide.core.jpa.security.service.ICorePermissionEvaluator;

public class CoreWebSecurityExpressionRoot extends WebSecurityExpressionRoot {
	
	private ICorePermissionEvaluator corePermissionEvaluator;

	public CoreWebSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
		super(a, fi);
	}
	
	public final boolean hasPermission(Object permission) {
		return corePermissionEvaluator.hasPermission(getAuthentication(), permission);
	}

	public void setCorePermissionEvaluator(ICorePermissionEvaluator permissionEvaluator) {
		setPermissionEvaluator(permissionEvaluator);
		this.corePermissionEvaluator = permissionEvaluator;
	}

}
