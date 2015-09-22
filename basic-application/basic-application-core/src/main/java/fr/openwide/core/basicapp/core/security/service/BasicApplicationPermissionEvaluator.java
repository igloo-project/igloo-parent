package fr.openwide.core.basicapp.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.service.AbstractCorePermissionEvaluator;
import fr.openwide.core.jpa.security.service.ISecurityService;

public class BasicApplicationPermissionEvaluator extends AbstractCorePermissionEvaluator<User> {

	@Autowired
	private ISecurityService securityService;

	public BasicApplicationPermissionEvaluator() {
	}

	@Override
	protected boolean hasPermission(User user, Object targetDomainObject, Permission permission) {
		// Déléguer ici les permissions aux différents PermissionEvaluator pour chaque objet métier
		// Au besoin, court-circuiter les vérifications pour le super user : if(isSuperUser(user) { return true }
		return false;
	}

}