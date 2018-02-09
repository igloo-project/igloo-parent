package org.iglooproject.test.web.context;

import org.glassfish.grizzly.servlet.WebappContext;
import org.iglooproject.commons.util.logging.SLF4JLoggingListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public abstract class MockSpringServlet extends MockServlet {

	private final Class<?> javaConfigClass;

	public MockSpringServlet(String schemeAndHost, int port, String contextPath, Class<?> javaConfigClass) {
		super(schemeAndHost, port, contextPath);
		this.javaConfigClass = javaConfigClass;
	}

	@Override
	protected void configureContext(WebappContext context) {
		super.configureContext(context);
		
		context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
		if (javaConfigClass != null) {
			context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, javaConfigClass.getName());
		}
		
		context.addListener(SLF4JLoggingListener.class);
		context.addListener(ContextLoaderListener.class);
	}

}
