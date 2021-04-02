package org.iglooproject.wicket.more.application;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.CompoundAuthorizationStrategy;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.wicket.more.CoreDefaultExceptionMapper;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.markup.CoreMarkupFactory;
import org.iglooproject.wicket.more.markup.parser.filter.InlineEnclosureComponentHandler;
import org.iglooproject.wicket.more.security.authorization.CoreAuthorizationStrategy;
import org.iglooproject.wicket.more.security.authorization.StandardUnauthorizedComponentInstantiationListener;
import org.iglooproject.wicket.more.security.page.AccessDeniedPage;
import org.iglooproject.wicket.more.security.page.LogoutPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PermissionFactory;

public abstract class CoreWicketAuthenticatedApplication extends CoreWicketApplication implements IRoleCheckingStrategy {
	
	private SerializableSupplier2<IExceptionMapper> coreExceptionMapperProvider;
	
	/**
	 * Subclass of authenticated web session to instantiate
	 */
	private final WeakReference<Class<? extends AuthenticatedWebSession>> webSessionClassRef;
	
	@Autowired
	private IAuthenticationService authenticationService;
	
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
		webSessionClassRef = new WeakReference<>(getWebSessionClass());
	}
	
	@Override
	public void init() {
		super.init();
		
		getPageSettings().addComponentResolver(new InlineEnclosureComponentHandler());
		
		getMarkupSettings().setMarkupFactory(new CoreMarkupFactory());
		
		getSecuritySettings().setAuthorizationStrategy(newAuthorizationStrategy());
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(newUnauthorizedComponentInstantiationListener());
		
		coreExceptionMapperProvider = new CoreDefaultExceptionMapperProvider();
	}
	
	@Override
	protected void mountCommonPages() {
		super.mountCommonPages();
		
		mountPage("/logout/", LogoutPage.class);
		mountPage("/access-denied/", AccessDeniedPage.class);
	}
	
	@Override
	public final boolean hasAnyRole(final Roles roles) {
		final Roles sessionRoles = AuthenticatedWebSession.get().getRoles();
		return sessionRoles != null && sessionRoles.hasAnyRole(roles);
	}
	
	@Override
	public Session newSession(Request request, Response response) {
		try {
			return webSessionClassRef.get().getDeclaredConstructor(Request.class).newInstance(request); // NOSONAR
		} catch (RuntimeException | InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e) {
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
	
	public final IPageLinkDescriptor getSignInPageLinkDescriptor() {
		return LinkDescriptorBuilder.start().page(getSignInPageClass());
	}
	
	@Override
	public SerializableSupplier2<IExceptionMapper> getExceptionMapperProvider() {
		return coreExceptionMapperProvider;
	}
	
	private static class CoreDefaultExceptionMapperProvider implements SerializableSupplier2<IExceptionMapper> {
		private static final long serialVersionUID = 1L;
		@Override
		public IExceptionMapper get() {
			return new CoreDefaultExceptionMapper();
		}
	}

}
