package fr.openwide.test.core.rest.jersey.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import fr.openwide.core.rest.jersey.client.AbstractRestClientServiceImpl;
import fr.openwide.test.core.rest.jersey.model.TestBean;

public class TestRestClient extends AbstractRestClientServiceImpl {
	
	public TestRestClient(String urlSchemeHost, int urlPort, String contextPath, String restServletPath) {
		super(urlSchemeHost, urlPort, contextPath, restServletPath);
	}
	
	public TestBean getTestBean(Integer id) {
		Client client = createJerseyClient();
		
		WebResource webResource = client.resource(getServiceUrl("test/get/%1$d/", id));
		
		return webResource.get(TestBean.class);
	}

}
