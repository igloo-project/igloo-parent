package org.iglooproject.test.web.context;

import java.net.URI;
import java.util.Collection;
import javax.servlet.ServletRegistration;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * This servlet configuration enable an {@link Application} (described by {@link
 * #createApplication()}).
 */
public abstract class AbstractMockRestServlet extends AbstractMockSpringServlet {

  private final String servletPath;

  public AbstractMockRestServlet(
      String schemeAndHost,
      int port,
      String contextPath,
      String servletPath,
      Class<?> javaConfigClass,
      Collection<Class<?>> initializers) {
    super(schemeAndHost, port, contextPath, javaConfigClass, initializers);
    this.servletPath = servletPath;
  }

  protected abstract ResourceConfig createApplication();

  @Override
  protected void addServlets(WebappContext context) {
    ServletContainer restFilter = new ServletContainer(createApplication());

    ServletRegistration restFilterRegistration = context.addServlet("restFilter", restFilter);
    // wilcard matching is mandatory
    String url = UriBuilder.fromUri("/").path(getServletPath()).path("*").build().toString();
    restFilterRegistration.addMapping(url);
  }

  public final String getServletPath() {
    return servletPath;
  }

  public final URI getRestUri() {
    return UriBuilder.fromUri(getSchemeAndHost())
        .port(getPort())
        .path(getContextPath())
        .path(getServletPath())
        .build();
  }
}
