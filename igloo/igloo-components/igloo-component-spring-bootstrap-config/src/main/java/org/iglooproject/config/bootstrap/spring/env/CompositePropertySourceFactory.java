package org.iglooproject.config.bootstrap.spring.env;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * <p><b>Not part of public API ; do not use.</b></p>
 * 
 * <p>This class allows to use a placeholder for a @{@link PropertySource} annotation value field that resolves to
 * a list of resources. It requires some setup be used:
 * <ul>
 * <li>A custom {@link ProtocolResolver} {@link CompositeProtocolResolver}, registered on the {@link ResourceLoader}
 * of {@link ConfigurableApplicationContext}.</li>
 * <li>@{@link org.springframework.context.annotation.PropertySource#factory()} must be set with
 * {@link CompositePropertySourceFactory}.</li>
 * <li>@{@link org.springframework.context.annotation.PropertySource#value()} must start with composite:, followed
 * by a comma-separated list of resources paths ; each resource path is resolved.</li>
 * </ul>
 * </p>
 * <pre class="code">
 * &#064;PropertySource(name = "name",
 *                 value = "composite:classpath:/configuration.properties,file:/etc/application/conf.properties",
 *                 factory = CompositePropertySourceFactory.class,
 *                 ignoreResourceNotFound = true)
 * </pre>
 */
public class CompositePropertySourceFactory implements PropertySourceFactory {

	private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = new DefaultPropertySourceFactory();

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		if (resource.getResource() instanceof CompositeResource) {
			CompositePropertySource compositePropertySource = new CompositePropertySource(name);
			for (Resource r : ((CompositeResource) resource.getResource()).getResources()) {
				try {
					PropertySource<?> wrappedPropertySource = DEFAULT_PROPERTY_SOURCE_FACTORY.createPropertySource(name,
							new EncodedResource(r, resource.getEncoding()));
					wrappedPropertySource = (wrappedPropertySource instanceof ResourcePropertySource ?
							((ResourcePropertySource) wrappedPropertySource).withResourceName() : wrappedPropertySource);
					compositePropertySource.addFirstPropertySource(wrappedPropertySource);
				} catch (IllegalArgumentException | FileNotFoundException | UnknownHostException ex) {
					// catch based on ConfigurationClassParser behavior
					
				}
			}
			return compositePropertySource;
		} else {
			throw new IllegalStateException(String.format("%s supports only %s resources",
					getClass().getSimpleName(), CompositeResource.class.getSimpleName()));
		}
	}

}
