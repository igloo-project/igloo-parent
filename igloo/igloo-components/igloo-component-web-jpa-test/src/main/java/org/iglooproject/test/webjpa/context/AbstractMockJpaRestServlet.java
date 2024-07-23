package org.iglooproject.test.webjpa.context;

import java.util.Collection;
import javax.ws.rs.core.Application;
import org.glassfish.grizzly.servlet.WebappContext;
import org.iglooproject.test.web.context.AbstractMockRestServlet;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * This servlet configuration both enable an {@link Application} (described by {@link
 * #createApplication()}) and add a {@link OpenEntityManagerInViewFilter}.
 */
public abstract class AbstractMockJpaRestServlet extends AbstractMockRestServlet {

  public AbstractMockJpaRestServlet(
      String schemeAndHost,
      int port,
      String contextPath,
      String servletPath,
      Class<?> javaConfigClass,
      Collection<Class<?>> initializers) {
    super(schemeAndHost, port, contextPath, servletPath, javaConfigClass, initializers);
  }

  @Override
  protected void configureContext(WebappContext context) {
    super.configureContext(context);

    context
        .addFilter("openEntityManagerInViewFilter", new OpenEntityManagerInViewFilter())
        .addMappingForUrlPatterns(null, "/*");
  }
}
