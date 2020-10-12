package org.igloo.spring.autoconfigure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

public class IglooAutoConfigurationImportSelector extends AutoConfigurationImportSelector {

	public static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE = "igloo.autoconfigure.exclude";

	@Override
	protected Class<?> getAnnotationClass() {
		return EnableIglooAutoConfiguration.class;
	}

	@Override
	protected Class<?> getSpringFactoriesLoaderFactoryClass() {
		return EnableIglooAutoConfiguration.class;
	}

	@Override
	protected List<String> getExcludeAutoConfigurationsProperty() {
		if (getEnvironment() instanceof ConfigurableEnvironment) {
			Binder binder = Binder.get(getEnvironment());
			return binder.bind(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class)
					.map(Arrays::asList).orElse(Collections.emptyList());
		}
		String[] excludes = getEnvironment()
				.getProperty(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class);
		return (excludes != null) ? Arrays.asList(excludes) : Collections.emptyList();
	}

}
