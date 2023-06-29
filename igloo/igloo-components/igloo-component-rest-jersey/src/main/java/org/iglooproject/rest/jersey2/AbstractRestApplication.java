package org.iglooproject.rest.jersey2;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.scanning.PackageNamesScanner;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

public abstract class AbstractRestApplication extends ResourceConfig {
	
	protected AbstractRestApplication(Class<? extends AbstractRestApplication> applicationClass) {
		super(applicationClass);
		
		register(JacksonJsonProvider.class);
		
		registerFinder(new PackageNamesScanner(new String[] { applicationClass.getPackage().getName() }, true));
	}
}