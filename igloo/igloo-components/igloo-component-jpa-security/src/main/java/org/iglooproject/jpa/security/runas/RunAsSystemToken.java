package org.iglooproject.jpa.security.runas;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.util.UserConstants;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Implémentation de {@link Authentication} permettant de faire tourner des tâches systèmes sous une
 * authentification propre et privilégiée.
 */
public class RunAsSystemToken extends RunAsUserToken {
  private static final long serialVersionUID = -5388947732896720161L;

  /** Liste des autorités de l'authentication */
  protected static final List<? extends GrantedAuthority> SYSTEM_AUTHORITIES =
      Lists.newArrayList(new SimpleGrantedAuthority(CoreAuthorityConstants.ROLE_SYSTEM));

  /**
   * Construction d'un nouveau jeton système
   *
   * @param runAsKey clé à utiliser. Pour que l'authentification soit réussie, il faut que cette clé
   *     corresponde à celle configurée sur le bean {@link RunAsImplAuthenticationProvider}
   * @param principal identifiant lié à l'{@link Authentication}
   * @param credentials mot de passe lié à l'{@link Authentication}
   */
  public RunAsSystemToken(
      String runAsKey,
      String principal,
      Collection<? extends GrantedAuthority> additionalAuthorities) {
    super(
        runAsKey,
        principal,
        UserConstants.NO_CREDENTIALS,
        Sets.newHashSet(Iterables.concat(SYSTEM_AUTHORITIES, additionalAuthorities)),
        Authentication.class);
    setAuthenticated(true);
  }

  @Override
  public Object getDetails() {
    return null;
  }
}
