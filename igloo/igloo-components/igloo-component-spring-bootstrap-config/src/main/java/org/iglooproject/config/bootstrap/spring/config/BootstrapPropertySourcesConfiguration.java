package org.iglooproject.config.bootstrap.spring.config;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.config.bootstrap.spring.env.CompositePropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <b>Not part of public API ; do not use.</b>
 *
 * <p>This class uses {@link CompositePropertySourceFactory} to allow to load properties from a
 * resolved placeholder *${igloo.configurationLocations}* that is a list of resources paths.
 */
@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.BOOTSTRAP,
    value = "composite:${igloo.configurationLocations}",
    factory = CompositePropertySourceFactory.class,
    ignoreResourceNotFound = true,
    encoding = "UTF-8")
public class BootstrapPropertySourcesConfiguration {}
