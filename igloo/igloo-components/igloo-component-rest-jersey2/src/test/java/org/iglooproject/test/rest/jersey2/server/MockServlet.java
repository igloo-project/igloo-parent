package org.iglooproject.test.rest.jersey2.server;

import java.util.Collections;
import org.glassfish.jersey.server.ResourceConfig;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.test.rest.jersey2.server.config.spring.RestServerTestCoreCommonConfig;
import org.iglooproject.test.webjpa.context.AbstractMockJpaRestServlet;

public class MockServlet extends AbstractMockJpaRestServlet {

  public MockServlet(String schemeAndHost, int port, String contextPath, String servletPath) {
    super(
        schemeAndHost,
        port,
        contextPath,
        servletPath,
        RestServerTestCoreCommonConfig.class,
        Collections.singleton(ExtendedApplicationContextInitializer.class));
  }

  @Override
  protected ResourceConfig createApplication() {
    return new SimpleRestApplication();
  }
}
