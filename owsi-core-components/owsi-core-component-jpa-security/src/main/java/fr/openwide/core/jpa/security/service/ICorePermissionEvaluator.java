package fr.openwide.core.jpa.security.service;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public interface ICorePermissionEvaluator extends PermissionEvaluator {

	/**
	 * Permet d'indiquer qu'un utilisateur doit avoir toutes les permissions et bypasser tous les checks de permissions.
	 */
	public abstract boolean isSuperUser(Authentication authentication);

}