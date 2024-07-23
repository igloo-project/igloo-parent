package org.iglooproject.wicket.more.config.spring;

import org.iglooproject.wicket.more.WicketMorePackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = WicketMorePackage.class,
    excludeFilters = @Filter(Configuration.class))
public class WicketMoreServiceConfig {}
