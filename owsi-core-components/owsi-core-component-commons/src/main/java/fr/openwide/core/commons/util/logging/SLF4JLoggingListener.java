package fr.openwide.core.commons.util.logging;

import java.util.logging.Handler;
import java.util.logging.LogManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * This listener inits the {@link SLF4JBridgeHandler} and enables java.util.logging (jul) calls to
 * be handled by slf4j choosen backend.
 * 
 * Add this to web.xml to use it :
 * &lt;listener&gt;
 *    &lt;listener-class&gt;fr.openwide.sqm.remote.web.util.SLF4JLoggingListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 *
 */
public class SLF4JLoggingListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SLF4JLoggingListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Jersey uses java.util.logging - bridge to slf4
		java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			rootLogger.removeHandler(handlers[i]);
		}
		SLF4JBridgeHandler.install();
		
		LOGGER.info("jul-to-slf4j installed");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
