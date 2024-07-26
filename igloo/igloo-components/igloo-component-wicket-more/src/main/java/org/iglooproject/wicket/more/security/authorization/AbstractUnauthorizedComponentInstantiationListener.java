package org.iglooproject.wicket.more.security.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;

/**
 * Définition du comportement de l'application dans le cas de l'accès à une page protégée.
 *
 * <p>Le comportement adopté est celui de Wicket, sauf dans le cas où on n'est pas authentifié :
 * dans ce cas, on enregistre l'url et on dirige l'utilisateur vers le process d'identification.
 *
 * <p>La façon de s'authentifier est gérée par les classes concrètes filles.
 */
public abstract class AbstractUnauthorizedComponentInstantiationListener
    implements IUnauthorizedComponentInstantiationListener {

  private static final String QUERY_STRING_SEPARATOR = "?";

  protected String getCurrentPageUrl(Page page) {
    Request request = page.getRequest();
    if (request instanceof ServletWebRequest) {
      ServletWebRequest servletWebRequest = (ServletWebRequest) request;
      StringBuffer currentUrl = servletWebRequest.getContainerRequest().getRequestURL();
      String queryString = servletWebRequest.getContainerRequest().getQueryString();
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
