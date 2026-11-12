package org.iglooproject.wicket.more;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import igloo.security.CoreUserDetails;
import igloo.security.UserDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.ISessionUser;
import org.iglooproject.jpa.security.service.AuthenticationUtil;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Authenticated Wicket session, independent of the kind of user (entity, DTO...).
 *
 * <p>The signed in user model is provided by the subclass (see {@link
 * #AbstractCoreAuthenticatedSession(Request, IModel)}): {@link AbstractCoreSession} uses an entity
 * model, an application that does not expose its entities to the front may provide a DTO model.
 */
public abstract class AbstractCoreAuthenticatedSession<U extends ISessionUser>
    extends AuthenticatedWebSession {

  private static final long serialVersionUID = -3215086340829627617L;

  @SpringBean(name = "authenticationService")
  protected IAuthenticationService authenticationService;

  @SpringBean(name = "authenticationManager")
  protected AuthenticationManager authenticationManager;

  @SpringBean(name = "propertyService")
  protected IPropertyService propertyService;

  /**
   * Like {@link SwitchUserFilter} from spring-security, we initialize our own {@link
   * SecurityContextRepository} (instance from spring-security filter stack is not available).
   */
  private final SecurityContextRepository securityContextRepository =
      new HttpSessionSecurityContextRepository();

  private final IModel<U> userModel;

  private final IModel<Locale> localeModel =
      new IModel<>() {
        private static final long serialVersionUID = -4356509005738585888L;

        @Override
        public Locale getObject() {
          return AbstractCoreAuthenticatedSession.this.getLocale();
        }

        @Override
        public void setObject(Locale object) {
          AbstractCoreAuthenticatedSession.this.setLocale(object);
        }

        @Override
        public void detach() {
          // Nothing to do
        }
      };

  private Roles roles = new Roles();

  private boolean rolesInitialized = false;

  private Collection<? extends Permission> permissions = Lists.newArrayList();

  private boolean permissionsInitialized = false;

  private boolean isSuperUser = false;

  private boolean isSuperUserInitialized = false;

  /**
   * @param request current request
   * @param userModel model used to hold the signed in user ; it must be able to survive session
   *     serialization (ex: reload the user from its id)
   */
  protected AbstractCoreAuthenticatedSession(Request request, IModel<U> userModel) {
    super(request);

    this.userModel = userModel;

    Injector.get().inject(this);

    // Override browser locale with mapped locale
    // setLocale process locale to map to one available locale
    setLocale(getLocale());
  }

  public static AbstractCoreAuthenticatedSession<?> get() {
    return (AbstractCoreAuthenticatedSession<?>) Session.get();
  }

  /**
   * Attempts to authenticate a user that has provided the given username and password.
   *
   * @param username current username
   * @param password current password
   * @return <code>true</code> if authentication succeeds, throws an exception if not
   * @throws BadCredentialsException if password doesn't match with username
   * @throws UsernameNotFoundException if username was not found
   * @throws DisabledException if user was found but disabled
   */
  @Override
  public boolean authenticate(String username, String password)
      throws BadCredentialsException, UsernameNotFoundException, DisabledException {
    doAuthenticate(username, password);

    doInitializeSession();

    return true;
  }

  protected Authentication doAuthenticate(String username, String password) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
    U user =
        Optional.ofNullable(AuthenticationUtil.getUserDetails(authentication))
            .map(UserDetails::getUsername)
            .map(this::loadUserByUsername)
            .orElse(null);
    onSuccessfulAuthentication(authentication, user);
    setSpringSecurityContext(authentication);
    return authentication;
  }

  protected void doInitializeSession() {
    U user = loadUserByUsername(authenticationService.getUsername());

    if (user == null) {
      throw new IllegalStateException("Unable to find the signed in user.");
    }

    userModel.setObject(user);

    onSignIn(user);

    Collection<? extends GrantedAuthority> authorities = authenticationService.getAuthorities();
    roles = new Roles();
    for (GrantedAuthority authority : authorities) {
      roles.add(authority.getAuthority());
    }
    rolesInitialized = true;

    permissions = authenticationService.getPermissions();
    permissionsInitialized = true;

    isSuperUser = authenticationService.isSuperUser();
    isSuperUserInitialized = true;
  }

  /**
   * @param username username of the user to load
   * @return the application user linked to this username, or null if not found
   */
  protected abstract U loadUserByUsername(String username);

  protected abstract void onSignIn(U user);

  public IModel<U> getUserModel() {
    return userModel;
  }

  /**
   * @return the currently logged in user, or null when no user is logged in
   */
  public String getUsername() {
    U user = getUser();
    return user != null ? user.getUsername() : null;
  }

  public U getUser() {
    U person = null;

    if (isSignedIn()) {
      person = userModel.getObject();
    }

    return person;
  }

  /**
   * Returns the current user roles.
   *
   * @return current user roles
   */
  @Override
  public Roles getRoles() {
    if (!rolesInitialized) {
      Collection<? extends GrantedAuthority> authorities = authenticationService.getAuthorities();
      for (GrantedAuthority authority : authorities) {
        roles.add(authority.getAuthority());
      }
      rolesInitialized = true;
    }
    return roles;
  }

  public boolean hasRole(String authority) {
    return getRoles().contains(authority);
  }

  public boolean hasRoleAdmin() {
    return hasRole(CoreAuthorityConstants.ROLE_ADMIN);
  }

  public boolean hasRoleAuthenticated() {
    return hasRole(CoreAuthorityConstants.ROLE_AUTHENTICATED);
  }

  public boolean hasRoleSystem() {
    return hasRole(CoreAuthorityConstants.ROLE_SYSTEM);
  }

  public boolean hasRoleAnonymous() {
    return hasRole(CoreAuthorityConstants.ROLE_ANONYMOUS);
  }

  protected Collection<? extends Permission> getPermissions() {
    if (!permissionsInitialized) {
      permissions = authenticationService.getPermissions();
      permissionsInitialized = true;
    }
    return permissions;
  }

  public boolean hasPermission(Permission permission) {
    if (isSuperUser()) {
      return true;
    }

    return getPermissions().contains(permission);
  }

  protected boolean isSuperUser() {
    if (!isSuperUserInitialized) {
      isSuperUser = authenticationService.isSuperUser();
      isSuperUserInitialized = true;
    }
    return isSuperUser;
  }

  /** Sign out the user and triggers a redirection to the current page. */
  @Override
  public void invalidate() {
    clearUserAuthentication();
    super.invalidate();
  }

  protected void clearUserAuthentication() {
    userModel.setObject(null);
    roles = new Roles();
    rolesInitialized = false;
    permissions = Lists.newArrayList();
    permissionsInitialized = false;

    authenticationService.signOut();
  }

  /** Override to provide locale mapping to available application locales. */
  @Override
  public Session setLocale(Locale locale) {
    return super.setLocale(propertyService.toAvailableLocale(locale));
  }

  public IModel<Locale> getLocaleModel() {
    return localeModel;
  }

  @Override
  public void detach() {
    super.detach();

    userModel.detach();
    localeModel.detach();
  }

  @Override
  public void internalDetach() {
    super.internalDetach();

    userModel.detach();
    localeModel.detach();
  }

  @SpringBean(name = "userDetailsService")
  private UserDetailsService userDetailsService;

  /** Used to keep the user's original authentication when they sign in as another user. */
  private Authentication originalAuthentication = null;

  public Authentication getOriginalAuthentication() {
    return originalAuthentication;
  }

  public boolean hasSignInAsPermissions(U utilisateurConnecte, U utilisateurCible) {
    return authenticationService.hasAdminRole();
  }

  /**
   * Inspired from spring-security {@link SwitchUserFilter}.
   *
   * @see AbstractCoreAuthenticatedSession#authenticate(String, String)
   */
  public void signInAs(String username) throws UsernameNotFoundException {
    U user = loadUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    if (!hasSignInAsPermissions(getUser(), user)) {
      throw new SecurityException("L'utilisateur n'a pas les permissions nécessaires");
    }
    UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
    Authentication previousAuthentication =
        Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication());
    if (previousAuthentication instanceof AnonymousAuthenticationToken) {
      throw new IllegalStateException("Anonymous authentication cannot invoke sign-in-as.");
    }
    GrantedAuthority switchAuthority =
        new SwitchUserGrantedAuthority("ROLE_PREVIOUS_AUTHENTICATION", previousAuthentication);
    Collection<GrantedAuthority> authorities =
        ImmutableList.<GrantedAuthority>builder()
            .addAll(userDetails.getAuthorities())
            .add(switchAuthority)
            .build();
    UserDetails targetUser =
        new CoreUserDetails(
            userDetails.getUsername(), "NO-PASSWORD", authorities, userDetails.getPermissions());
    // create the new authentication token
    UsernamePasswordAuthenticationToken targetUserRequest =
        UsernamePasswordAuthenticationToken.authenticated(
            targetUser, targetUser.getPassword(), targetUser.getAuthorities());
    targetUserRequest.setDetails(targetUser);
    setSpringSecurityContext(targetUserRequest);
    originalAuthentication = previousAuthentication;

    doInitializeSession();
    bind();
    signIn(true);
  }

  /**
   * Refresh the current authentication. Needed if UserDetailsService roles and permissions must be
   * reloaded.
   *
   * <p>Update both Spring Security and Wicket contexts.
   */
  public void refreshAuthentication() {
    Collection<? extends GrantedAuthority> originalAuthorities =
        Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
            .getAuthorities();
    UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(getUsername());
    UserDetails targetUser =
        new CoreUserDetails(
            userDetails.getUsername(),
            "NO-PASSWORD",
            userDetails.getAuthorities(),
            userDetails.getPermissions());

    // FactorGrantedAuthority-aware behavior:
    // Some mechanism (like OIDC provider) relies on FactorGrantedAuthority to work correctly. If
    // there currently is a FactorGrantedAuthority in current Authentication, we keep it in the
    // newly generated Authentication.
    // Authority is added only on Authentication, not on userDetails (like it is done at login time)
    //
    // (if missing, spring-security JWTGenerator used by OIDC provider for SSO fails as it cannot
    // populate authenticationTime field)
    Set<GrantedAuthority> authorities = Sets.newHashSet(userDetails.getAuthorities());
    originalAuthorities.stream()
        .filter(FactorGrantedAuthority.class::isInstance)
        .findFirst()
        .ifPresent(authorities::add);

    // create the new authentication token
    UsernamePasswordAuthenticationToken targetUserRequest =
        UsernamePasswordAuthenticationToken.authenticated(
            targetUser,
            targetUser.getPassword(),
            authorities // use merged authorities - cf FactorGrantedAuthority
            );
    targetUserRequest.setDetails(targetUser);
    setSpringSecurityContext(targetUserRequest);

    doInitializeSession();
    bind();
    signIn(true);
  }

  public void signInAsMe() throws BadCredentialsException, SecurityException {
    if (originalAuthentication == null) {
      throw new BadCredentialsException("Pas d'authentification originelle");
    }

    setSpringSecurityContext(originalAuthentication);
    doInitializeSession();
    bind();
    signIn(true);
    originalAuthentication = null;
  }

  private void setSpringSecurityContext(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
    securityContextRepository.saveContext(
        SecurityContextHolder.getContext(),
        (HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest(),
        (HttpServletResponse) RequestCycle.get().getResponse().getContainerResponse());
  }

  /**
   * This method may be overriden to restrict user login when authentication is done, but user is
   * not allowed to login either. A use-case is to restrict user login based on user-type based on
   * current domain (intranet user cannot login on extranet, extranet user cannot login on
   * intranet).
   *
   * <p>This method is called before spring context is set. Use {@link #onSignIn(ISessionUser)}
   * event if you want to perform some task after spring context is set.
   *
   * <p>Login may be interrupted by throwing an {@link AuthenticationException} children.
   * Application must ensure that appropriate user feedback when this exception is thrown.
   *
   * @param authentication successful {@link Authentication} object. Must not be null.
   * @param user application user linked to this authentication. May be null for exceptional cases
   *     where authentication is performed with extra authentication stacks not user-based (API
   *     configuration-based authentication, ...)
   */
  protected void onSuccessfulAuthentication(
      @Nonnull Authentication authentication, @Nullable U user) {
    // Override in applications
  }
}
