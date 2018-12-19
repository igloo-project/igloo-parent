package test.web.spring;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class JettyTestExecutionListener implements TestExecutionListener {

	private Server server;

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		if (server == null) {
			launchJettyServer();
		}
	}

	/**
	 * Indispensable pour cleaner le contexte et permettre aux tests WicketTester de s'exécuter par la suite
	 */
	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

	private void launchJettyServer() throws Exception {
		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setHost("localhost");
		connector.setPort(8090);
		server.setConnectors(new Connector [] {connector});
		
		WebAppContext webapp = new WebAppContext();
		webapp.setResourceBase("src/main/java");
		webapp.setContextPath("/");
		webapp.setDefaultsDescriptor("src/test/java/test/web/selenium/web.xml");
		
		server.setHandler(webapp);
		
		// Nécessaire pour lancer le serveur en mode test
		System.setProperty("igloo.profile", "test");
		
		server.start();
	}

}
