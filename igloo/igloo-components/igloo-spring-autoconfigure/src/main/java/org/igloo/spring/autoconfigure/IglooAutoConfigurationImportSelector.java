package org.igloo.spring.autoconfigure;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotationMetadata;

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

	/**
	 * Copied from {@link AutoConfigurationImportSelector} to replace {@link #getExcludeAutoConfigurationsProperty()}
	 * private call, so we can use a custom property to exclude autoconfigurations.
	 * 
	 * Return any exclusions that limit the candidate configurations.
	 * @param metadata the source metadata
	 * @param attributes the {@link #getAttributes(AnnotationMetadata) annotation
	 * attributes}
	 * @return exclusions or an empty set
	 */
	@Override
	protected Set<String> getExclusions(AnnotationMetadata metadata,
			AnnotationAttributes attributes) {
		Set<String> excluded = new LinkedHashSet<>();
		excluded.addAll(asList(attributes, "exclude"));
		excluded.addAll(Arrays.asList(attributes.getStringArray("excludeName")));
		excluded.addAll(getExcludeAutoConfigurationsProperty());
		return excluded;
	}

	private List<String> getExcludeAutoConfigurationsProperty() {
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
