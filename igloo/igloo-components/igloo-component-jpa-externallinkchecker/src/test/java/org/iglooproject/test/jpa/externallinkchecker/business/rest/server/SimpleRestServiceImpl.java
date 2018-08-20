package org.iglooproject.test.jpa.externallinkchecker.business.rest.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.springframework.stereotype.Component;

import org.iglooproject.rest.jersey2.service.AbstractRestServiceImpl;

@Component
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class SimpleRestServiceImpl extends AbstractRestServiceImpl {

	private static final String SIMPLE_OK_PATH = "/ok";
	@HEAD
	@Path(SIMPLE_OK_PATH)
	public Response ok() {
		return Response.ok().build();
	}

	@HEAD
	@Path("ok/location-gîte-ardeche.fr/index.html")
	public Response okParse1() {
		return Response.ok().build();
	}

	@HEAD
	@Path("ok/V2/Partenaires/00043/Images/Chalet 95/dheilly003.JPG")
	public Response okParse2() {
		return Response.ok().build();
	}

	@GET
	@Path("ok/reservit/avail-info.php")
	public Response okParse3(
			@QueryParam("hotelid") String hotelid,
			@QueryParam("userid") String userid,
			@QueryParam("__utma") String __utma,
			@QueryParam("__utmc") String __utmc,
			@QueryParam("__utmz") String __utmz
			
	) {
		return Response.ok().build();
	}

	@GET
	@Path("ok/translate_c")
	public Response okParse4(
			@QueryParam("client") String client,
			@QueryParam("depth") String depth,
			@QueryParam("hl") String hl,
			@QueryParam("langpair") String langpair,
			@QueryParam("rurl") String rurl,
			@QueryParam("u") String u
	) {
		return Response.ok().build();
	}

	@GET
	@Path("/ok/get")
	public Response okGet() {
		return Response.ok().build();
	}

	private static final String _301toOK_PATH = "/301toOK";
	@GET
	@Path(_301toOK_PATH)
	public Response _301toOk(@Context HttpServletRequest req) {
		String requestURI = req.getRequestURI();
		Matcher matcher = Pattern.compile(_301toOK_PATH + "/?$").matcher(requestURI);
		String redirectURI = matcher.replaceAll(SIMPLE_OK_PATH);
		return Response.status(Status.MOVED_PERMANENTLY)
				.location(UriBuilder.fromUri(redirectURI).build())
				.build();
	}

	@GET
	@Path("ko/not-found")
	public Response koNotFound() {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

}