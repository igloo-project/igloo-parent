package fr.openwide.core.web.security.access.expression.method;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import fr.openwide.core.jpa.security.service.ICorePermissionEvaluator;
import fr.openwide.core.web.security.access.expression.CoreWebSecurityExpressionRoot;

public class CoreWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {

	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	
	private ICorePermissionEvaluator corePermissionEvaluator;

	@Override
	protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
			FilterInvocation fi) {
		CoreWebSecurityExpressionRoot root = new CoreWebSecurityExpressionRoot(authentication, fi);
		root.setCorePermissionEvaluator(getCorePermissionEvaluator());
		root.setTrustResolver(trustResolver);
		root.setRoleHierarchy(getRoleHierarchy());
		return root;
	}
	
	protected ICorePermissionEvaluator getCorePermissionEvaluator() {
		return corePermissionEvaluator;
	}

	public void setCorePermissionEvaluator(ICorePermissionEvaluator corePermissionEvaluator) {
		super.setPermissionEvaluator(corePermissionEvaluator);
		this.corePermissionEvaluator = corePermissionEvaluator;
	}

}
