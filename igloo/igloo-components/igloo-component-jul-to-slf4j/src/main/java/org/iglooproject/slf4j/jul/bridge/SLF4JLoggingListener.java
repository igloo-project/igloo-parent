package org.iglooproject.slf4j.jul.bridge;

import java.util.logging.Level;
import java.util.logging.LogManager;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

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
		// from https://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge
		// Jersey uses java.util.logging - bridge to slf4
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		// Note that you need to override global logging level
		// if you want to log finer logs (slf4j setting is not enough).
		java.util.logging.Logger.getGlobal().setLevel(Level.INFO);
		
		LOGGER.info("jul-to-slf4j installed");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
