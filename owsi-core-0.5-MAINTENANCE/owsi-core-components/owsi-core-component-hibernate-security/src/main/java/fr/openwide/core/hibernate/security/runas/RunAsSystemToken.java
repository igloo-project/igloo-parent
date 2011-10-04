package fr.openwide.core.hibernate.security.runas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import fr.openwide.core.hibernate.security.business.authority.util.CoreAuthorityConstants;

/**
 * Implémentation de {@link Authentication} permettant de faire tourner des tâches
 * systèmes sous une authentification propre et privilégiée.
 */
public class RunAsSystemToken extends RunAsUserToken {
	private static final long serialVersionUID = -5388947732896720161L;
	
	/**
	 * Liste des autorités de l'authentication
	 */
	protected static final List<GrantedAuthority> SYSTEM_AUTHORITIES = new ArrayList<GrantedAuthority>();
	
	static {
		SYSTEM_AUTHORITIES.add(new GrantedAuthorityImpl(CoreAuthorityConstants.ROLE_SYSTEM));
	}
	
	/**
	 * Construction d'un nouveau jeton système
	 * 
	 * @param key clé à utiliser. Pour que l'authentification soit réussie, il faut que cette clé
	 *   corresponde à celle configurée sur le bean {@link RunAsImplAuthenticationProvider}
	 * @param principal identifiant lié à l'{@link Authentication}
	 * @param credentials mot de passe lié à l'{@link Authentication}
	 */
	public RunAsSystemToken(String key, Object principal, Object credentials) {
		super(key, principal, credentials, SYSTEM_AUTHORITIES, Authentication.class);
		setAuthenticated(true);
	}
	
	@Override
	public Object getDetails() {
		return null;
	}

}
