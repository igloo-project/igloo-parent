package fr.openwide.core.wicket.more.application;

import java.lang.ref.WeakReference;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PermissionFactory;

import fr.openwide.core.hibernate.security.service.AuthenticationService;
import fr.openwide.core.wicket.more.security.authorization.CoreAuthorizationStrategy;
import fr.openwide.core.wicket.more.security.authorization.StandardUnauthorizedComponentInstantiationListener;
import fr.openwide.core.wicket.more.security.page.LogoutPage;

public abstract class CoreWicketAuthenticatedApplication extends
		CoreWicketApplication implements IRoleCheckingStrategy {
	
	/**
	 * Subclass of authenticated web session to instantiate
	 */
	private final WeakReference<Class<? extends AuthenticatedWebSession>> webSessionClassRef;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PermissionFactory permissionFactory;
	
	public static CoreWicketAuthenticatedApplication get() {
		final Application application = Application.get();
		if (application instanceof CoreWicketAuthenticatedApplication) {
			return (CoreWicketAuthenticatedApplication) application;
		}
		throw new WicketRuntimeException("There is no CoreWicketAuthenticatedApplication attached to current thread " +
				Thread.currentThread().getName());
	}

	public CoreWicketAuthenticatedApplication() {
		super();
		
		// Get web session class to instantiate
		webSessionClassRef = new WeakReference<Class<? extends AuthenticatedWebSession>>(
			getWebSessionClass());
	}
	
	@Override
	public void init() {
		super.init();
		
		getSecuritySettings().setAuthorizationStrategy(newAuthorizationStrategy());
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(
				newUnauthorizedComponentInstantiationListener());
	}
	
	@Override
	protected void mountCommonPages() {
		super.mountCommonPages();
		
		mountBookmarkablePage("/logout/", LogoutPage.class);
	}
	
	@Override
	public final boolean hasAnyRole(final Roles roles) {
		final Roles sessionRoles = AuthenticatedWebSession.get().getRoles();
		return sessionRoles != null && sessionRoles.hasAnyRole(roles);
	}
	
	@Override
	public Session newSession(final Request request, final Response response) {
		try {
			return webSessionClassRef.get().getDeclaredConstructor(Request.class).newInstance(
				request);
		} catch (Exception e) {
			throw new WicketRuntimeException("Unable to instantiate web session " +
				webSessionClassRef.get(), e);
		}
	}

	protected CompoundAuthorizationStrategy newAuthorizationStrategy() {
		return new CoreAuthorizationStrategy(this, authenticationService, permissionFactory);
	}
	
	protected IUnauthorizedComponentInstantiationListener newUnauthorizedComponentInstantiationListener() {
		return new StandardUnauthorizedComponentInstantiationListener();
	}
	
	protected abstract Class<? extends AuthenticatedWebSession> getWebSessionClass();
	
	public abstract Class<? extends WebPage> getSignInPageClass();

}
