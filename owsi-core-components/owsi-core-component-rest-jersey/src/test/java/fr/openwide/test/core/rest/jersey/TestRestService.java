package fr.openwide.test.core.rest.jersey;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.sun.grizzly.http.SelectorThread;

import fr.openwide.core.rest.jersey.RestApplication;
import fr.openwide.core.rest.jersey.test.util.RestTestUtils;
import fr.openwide.test.core.rest.jersey.client.TestRestClient;
import fr.openwide.test.core.rest.jersey.model.TestBean;

@ContextConfiguration(locations = { "classpath:spring/test-rest-context.xml" }, inheritLocations=false)
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class TestRestService {

	public static final String TEST_SERVER_URI = "http://localhost/";
	public static final int TEST_SERVER_PORT = 9997;
	public static final String TEST_CONTEXT_PATH = "/sqm-remote-api";
	public static final String TEST_SERVLET_PATH = "/rest";
	
	private static SelectorThread selectorThread;
	
	@BeforeClass
	public static void initClass() throws IllegalArgumentException, IOException {
		selectorThread = RestTestUtils.getSelectorThread(RestApplication.class, TEST_SERVER_URI, TEST_SERVER_PORT,
				TEST_CONTEXT_PATH, TEST_SERVLET_PATH, new String[] { "classpath:spring/test-rest-context.xml" },
				false);
	}
	
	@Test
	public void testRest() {
		TestRestClient client = new TestRestClient(TEST_SERVER_URI, TEST_SERVER_PORT, TEST_CONTEXT_PATH, TEST_SERVLET_PATH);
		
		TestBean bean;
		
		bean = client.getTestBean(1);
		Assert.assertEquals(Integer.valueOf(1), bean.getId());
		
		bean = client.getTestBean(16);
		Assert.assertEquals(Integer.valueOf(16), bean.getId());
	}
	
	@AfterClass
	public static void closeClass() {
		selectorThread.stopEndpoint();
	}
}
