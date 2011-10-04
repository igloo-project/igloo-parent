package fr.openwide.core.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

/**
 * Définition du comportement de l'application dans le cas de l'accès à une
 * page protégée.
 * 
 * Le comportement adopté est celui de Wicket, sauf dans le cas où on n'est pas
 * authentifié : dans ce cas, on enregistre l'url et on dirige l'utilisateur
 * vers le process d'identification.
 * 
 * La façon de s'authentifier est gérée par les classes concrètes filles.
 */
public abstract class AbstractUnauthorizedComponentInstantiationListener implements
		IUnauthorizedComponentInstantiationListener {
	
	private static final String QUERY_STRING_SEPARATOR = "?";
	
	protected String getCurrentPageUrl(Page page) {
		Request request = page.getRequest();
		if (request instanceof ServletWebRequest) {
			ServletWebRequest servletWebRequest = (ServletWebRequest) request;
			StringBuffer currentUrl = servletWebRequest.getHttpServletRequest().getRequestURL();
			String queryString = servletWebRequest.getHttpServletRequest().getQueryString();
			if (queryString != null) {
				currentUrl.append(QUERY_STRING_SEPARATOR).append(queryString);
			}
			
			return currentUrl.toString();
		} else {
			return null;
		}
	}
	
	protected void onUnauthorizedComponentInstantiation(Component component) {
		throw new UnauthorizedInstantiationException(component.getClass());
	}

}
