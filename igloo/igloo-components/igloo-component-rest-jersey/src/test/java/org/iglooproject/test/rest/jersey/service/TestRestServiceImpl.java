package org.iglooproject.test.rest.jersey.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.sun.jersey.api.JResponse;

import org.iglooproject.rest.jersey.service.AbstractRestServiceImpl;
import org.iglooproject.test.rest.jersey.model.TestBean;

@Component
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestRestServiceImpl extends AbstractRestServiceImpl {

	@GET
	@Path("get/{id}/")
	public JResponse<TestBean> addApplicationError(@PathParam("id") Integer id) {
		return JResponse.ok(new TestBean(id)).build();
	}
}