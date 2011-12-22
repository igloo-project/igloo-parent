package fr.openwide.core.rest.jersey;

import java.util.Set;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.core.PackagesResourceConfig;

public final class RestApplication extends PackagesResourceConfig {
	
	public RestApplication() {
		super(RestApplication.class.getPackage().getName());
	}
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = super.getSingletons();
		singletons.add(new JacksonJsonProvider());
		
		return singletons;
	}
}