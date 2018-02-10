package org.iglooproject.test.jpa.externallinkchecker.business.rest.server;

import org.glassfish.jersey.server.ResourceConfig;
import org.iglooproject.test.jpa.externallinkchecker.business.rest.SimpleRestApplication;
import org.iglooproject.test.jpa.externallinkchecker.business.rest.server.config.spring.RestServerTestCoreCommonConfig;
import org.iglooproject.test.web.context.AbstractMockRestServlet;

public class MockServlet extends AbstractMockRestServlet {

	public MockServlet(String schemeAndHost, int port, String contextPath, String servletPath) {
		super(schemeAndHost, port, contextPath, servletPath, RestServerTestCoreCommonConfig.class);
	}

	@Override
	protected ResourceConfig createApplication() {
		return new SimpleRestApplication();
	}

}
