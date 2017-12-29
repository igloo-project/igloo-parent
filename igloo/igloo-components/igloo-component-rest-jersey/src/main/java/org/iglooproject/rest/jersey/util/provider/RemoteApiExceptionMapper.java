package org.iglooproject.rest.jersey.util.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import org.iglooproject.rest.jersey.util.exception.RemoteApiException;

@Component
@Provider
public class RemoteApiExceptionMapper implements ExceptionMapper<RemoteApiException> {

	@Override
	public Response toResponse(RemoteApiException exception) {
		return Response.status(Status.BAD_REQUEST).entity(exception).build();
	}

}