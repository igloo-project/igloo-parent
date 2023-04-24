package org.iglooproject.wicket.more;

import java.util.Collection;
import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.service.IGenericUserService;
import org.iglooproject.jpa.security.model.NamedPermission;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.wicket.more.model.threadsafe.SessionThreadSafeGenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.google.common.collect.Lists;

public abstract class AbstractCoreSession<U extends GenericUser<U, ?>> extends AuthenticatedWebSession {

	private static final long serialVersionUID = 2591467597835056981L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCoreSession.class);
	
	@SpringBean(name = "userService")
	protected IGenericUserService<U> userService;
	
	@SpringBean(name = "authenticationService")
	protected IAuthenticationService authenticationService;
	
	@SpringBean(name = "authenticationManager")
	protected AuthenticationManager authenticationManager;
	
	@SpringBean(name = "propertyService")
	protected IPropertyService propertyService;
	
	private final IModel<U> userModel = new SessionThreadSafeGenericEntityModel<>();
	
	private final IModel<Locale> localeModel = new IModel<Locale>() {
		private static final long serialVersionUID = -4356509005738585888L;
		
		@Override
		public Locale getObject() {
			return AbstractCoreSession.this.getLocale();
		}
		
		@Override
		public void setObject(Locale object) {
			AbstractCoreSession.this.setLocale(object);
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
	
	public AbstractCoreSession(Request request) {
		super(request);
		
		Injector.get().inject(this);
		
		// Override browser locale with mapped locale
		// setLocale process locale to map to one available locale
		setLocale(getLocale());
	}
	
	public static AbstractCoreSession<?> get() {
		return (AbstractCoreSession<?>) Session.get();
	}

	/**
	 * Attempts to authenticate a user that has provided the given username and
	 * password.
	 * 
	 * @param username
	 *            current username
	 * @param password
	 *            current password
	 * @return <code>true</code> if authentication succeeds, throws an exception if not
	 * 
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
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return authentication;
	}
	
	protected void doInitializeSession() {
		U user = userService.getByUsername(authenticationService.getUsername());
		
		if (user == null) {
			throw new IllegalStateException("Unable to find the signed in user.");
		}
		
		userModel.setObject(user);
		
		try {
			if (user.getLastLoginDate() == null) {
				onFirstLogin(user);
			}
			
			userService.updateLastLoginDate(user);
			
			Locale locale = user.getLocale();
			if (locale != null) {
				setLocale(user.getLocale());
			} else {
				// si la personne ne possède pas de locale
				// alors on enregistre celle mise en place
				// automatiquement par le navigateur.
				userService.updateLocale(user, getLocale());
			}
		} catch (RuntimeException | ServiceException | SecurityServiceException e) {
			LOGGER.error(String.format("Unable to update the user information on sign in: %1$s", user), e);
		}
		
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
	
	protected void onFirstLogin(U user) {
	}
	
	public IModel<U> getUserModel() {
		return userModel;
	}

	/**
	 * @return the currently logged in user, or null when no user is logged in
	 */
	public String getUsername() {
		String username = null;
		if (isSignedIn()) {
			username = userModel.getObject().getUsername();
		}
		return username;
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

	/**
	 * Sign out the user and triggers a redirection to the current page.
	 */
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
	
	/**
	 * <p>Override to provide locale mapping to available application locales.</p>
	 * @return 
	 */
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

	/**
	 * Utilisé pour garder l'authentification de l'utilisateur lorsqu'il se connecte en tant qu'un autre utilisateur.
	 */
	private Authentication originalAuthentication = null;

	public Authentication getOriginalAuthentication() {
		return originalAuthentication;
	}

	public boolean hasSignInAsPermissions(U utilisateurConnecte, U utilisateurCible) {
		return authenticationService.hasPermission(NamedPermission.ADMIN_SIGN_IN_AS);
	}

	/**
	 * @see AbstractCoreSession#authenticate(String, String)
	 */
	public void signInAs(String username) throws UsernameNotFoundException {
		//TODO igloo-boot
		throw new IllegalStateException("igloo-boot");
	}

	public void signInAsMe() throws BadCredentialsException, SecurityException {
		if (originalAuthentication == null) {
			throw new BadCredentialsException("Pas d'authentification originelle");
		}
		
		SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
		doInitializeSession();
		bind();
		signIn(true);
		originalAuthentication = null;
	}

}
