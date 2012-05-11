package fr.openwide.core.wicket.more;

import java.util.Collection;
import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;
import fr.openwide.core.jpa.security.service.IAuthenticationService;
import fr.openwide.core.spring.config.CoreConfigurer;

public class AbstractCoreSession<P extends AbstractPerson<P>> extends AuthenticatedWebSession {

	private static final long serialVersionUID = 2591467597835056981L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCoreSession.class);
	
	private static final String REDIRECT_URL_ATTRIBUTE_NAME = "redirect";
	
	@SpringBean(name="personService")
	protected IPersonService<P> personService;
	
	@SpringBean(name="authenticationService")
	protected IAuthenticationService authenticationService;
	
	@SpringBean(name="configurer")
	protected CoreConfigurer configurer;
	
	private Long userId;
	
	private Roles roles = new Roles();

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
	 * @return <code>true</code> if authentication succeeds,
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean authenticate(String username, String password) {
		boolean loggedIn = authenticationService.isLoggedIn();
		
		if (loggedIn) {
			P person = personService.getByUserName(authenticationService.getUserName());
			if (person != null) {
				userId = person.getId();
				
				try {
					personService.updateLastLoginDate(person);
					
					Locale locale = person.getLocale();
					if (locale != null) {
						setLocale(person.getLocale());
					} else {
						// si la personne ne possède pas de locale
						// alors on enregistre celle mise en place
						// automatiquement par le navigateur.
						personService.updateLocale(person, getLocale());
					}
				} catch (Exception e) {
					LOGGER.error(String.format("Unable to update the user information on sign in: %1$s", person), e);
				}
			}
			
			if (roles.isEmpty()) {
				Collection<? extends GrantedAuthority> authorities = authenticationService.getAuthorities();
				for (GrantedAuthority authority : authorities) {
					roles.add(authority.getAuthority());
				}
			}
		}
		
		return loggedIn;
	}

	/**
	 * @return the currently logged in user, or null when no user is logged in
	 */
	public String getUserName() {
		String userName = null;
		if (isSignedIn()) {
			userName = authenticationService.getUserName();
		}
		return userName;
	}

	protected P getPerson() {
		P person = null;

		if (isSignedIn() && userId != null) {
			person = personService.getById(userId);
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
		return roles;
	}
	
	protected boolean hasRole(String authority) {
		return roles.contains(authority);
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

	/**
	 * Invalidates the session. After a signout, you should redirect
	 * the browser to the home page.
	 */
	@Override
	public void invalidate() {
		userId = null;
		roles = new Roles();
		removeAttribute(REDIRECT_URL_ATTRIBUTE_NAME);
		
		authenticationService.signOut();
		
		super.invalidate();
	}
	
	/**
	 * Permet d'enregistrer une url de redirection.
	 * 
	 * Ce mécanisme de redirection est utilisé lors du trajet
	 * page privé -> cas -> page /cas
	 * 
	 * @param url
	 */
	public void registerRedirectUrl(String url) {
		// le bind() est obligatoire pour demander à wicket de persister la session
		// si on ne le fait pas, la session possède comme durée de vie le temps de
		// la requête.
		if (isTemporary()) {
			bind();
		}
		
		setAttribute(REDIRECT_URL_ATTRIBUTE_NAME, url);
	}
	
	/**
	 * Permet de récupérer la dernière url de redirection enregistrée
	 * 
	 * @return null si aucune url enregistrée
	 */
	public String getRedirectUrl() {
		String redirectUrl = (String) getAttribute(REDIRECT_URL_ATTRIBUTE_NAME);
		removeAttribute(REDIRECT_URL_ATTRIBUTE_NAME);
		return redirectUrl;
	}
	
	/**
	 * <p>Override to provide locale mapping to available application locales.</p>
	 */
	@Override
	public void setLocale(Locale locale) {
		super.setLocale(configurer.toAvailableLocale(locale));
	}
}
