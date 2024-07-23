package org.iglooproject.test.config.bootstrap.spring.util.locations;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.COMPONENT,
    value = "classpath:configuration-app-default.properties",
    encoding = "UTF-8")
public class SpringWithConfigurationLocationsDefaultConfig {}
