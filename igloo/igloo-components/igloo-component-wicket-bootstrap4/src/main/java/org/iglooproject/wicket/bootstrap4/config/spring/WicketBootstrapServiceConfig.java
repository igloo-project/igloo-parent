package org.iglooproject.wicket.bootstrap4.config.spring;

import org.iglooproject.wicket.bootstrap4.WicketBootstrapPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
		basePackageClasses = WicketBootstrapPackage.class,
		excludeFilters = @Filter(Configuration.class)
)
public class WicketBootstrapServiceConfig {

}
