package fr.openwide.core.rest.jersey;

import java.util.Set;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.core.PackagesResourceConfig;

public abstract class AbstractRestApplication extends PackagesResourceConfig {
	
	protected AbstractRestApplication() {
		super(AbstractRestApplication.class.getPackage().getName());
	}
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = super.getSingletons();
		singletons.add(new JacksonJsonProvider());
		
		return singletons;
	}
}