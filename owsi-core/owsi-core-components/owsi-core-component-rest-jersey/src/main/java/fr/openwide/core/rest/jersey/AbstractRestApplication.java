package fr.openwide.core.rest.jersey;

import java.util.Set;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.core.PackagesResourceConfig;

public abstract class AbstractRestApplication extends PackagesResourceConfig {
	
	protected AbstractRestApplication(Class<? extends AbstractRestApplication> applicationClass) {
		super(applicationClass.getPackage().getName());
	}
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = super.getSingletons();
		singletons.add(new JacksonJsonProvider());
		
		return singletons;
	}
}