package org.iglooproject.wicket.more.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import org.iglooproject.wicket.more.WicketMorePackage;

@Configuration
@ComponentScan(
		basePackageClasses = WicketMorePackage.class,
		excludeFilters = @Filter(Configuration.class)
)
public class WicketMoreServiceConfig {

}
