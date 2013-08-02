package fr.openwide.core.jpa.security.access.expression.method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import fr.openwide.core.jpa.security.access.expression.CoreMethodSecurityExpressionRoot;
import fr.openwide.core.jpa.security.service.ICorePermissionEvaluator;

public class CoreMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

	private ICorePermissionEvaluator corePermissionEvaluator;

	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
			MethodInvocation invocation) {
		CoreMethodSecurityExpressionRoot root = new CoreMethodSecurityExpressionRoot(authentication);
		root.setCorePermissionEvaluator(getCorePermissionEvaluator());

		return root;
	}
	
	protected ICorePermissionEvaluator getCorePermissionEvaluator() {
		return corePermissionEvaluator;
	}

	public void setCorePermissionEvaluator(ICorePermissionEvaluator corePermissionEvaluator) {
		super.setPermissionEvaluator(corePermissionEvaluator);
		this.corePermissionEvaluator = corePermissionEvaluator;
	}
	
	@Override
	public void setReturnObject(Object returnObject, EvaluationContext ctx) {
		((CoreMethodSecurityExpressionRoot) ctx.getRootObject().getValue()).setReturnObject(returnObject);
	}

}