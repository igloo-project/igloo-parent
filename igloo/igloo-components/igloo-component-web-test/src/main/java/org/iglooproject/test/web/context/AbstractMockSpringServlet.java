package org.iglooproject.test.web.context;

import java.util.Collection;
import java.util.stream.Collectors;

import org.glassfish.grizzly.servlet.WebappContext;
import org.iglooproject.slf4j.jul.bridge.SLF4JLoggingListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.google.common.base.Joiner;

public abstract class AbstractMockSpringServlet extends AbstractMockServlet {

	private final Class<?> javaConfigClass;

	private final Collection<Class<?>> initializers;

	public AbstractMockSpringServlet(String schemeAndHost, int port, String contextPath, Class<?> javaConfigClass,
			Collection<Class<?>> initializers) {
		super(schemeAndHost, port, contextPath);
		this.javaConfigClass = javaConfigClass;
		this.initializers = initializers;
	}

	@Override
	protected void configureContext(WebappContext context) {
		super.configureContext(context);
		
		context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
		if (javaConfigClass != null) {
			context.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, javaConfigClass.getName());
		}
		context.setInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM,
				Joiner.on(",").join(initializers.stream().map(Class::getName).collect(Collectors.toList())));
		
		context.addListener(SLF4JLoggingListener.class);
		context.addListener(ContextLoaderListener.class);
	}

}
