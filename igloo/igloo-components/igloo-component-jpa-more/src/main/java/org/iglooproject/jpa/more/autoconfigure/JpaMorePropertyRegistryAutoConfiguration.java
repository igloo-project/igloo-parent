package org.iglooproject.jpa.more.autoconfigure;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.DATABASE_INITIALIZED;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.IMAGE_MAGICK_CONVERT_BINARY_PATH;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;

import java.io.File;

import org.iglooproject.spring.autoconfigure.PropertyIdsAutoConfiguration;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfiguration(after = PropertyIdsAutoConfiguration.class)
@ConditionalOnBean(IConfigurablePropertyService.class)
public class JpaMorePropertyRegistryAutoConfiguration implements IPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerBoolean(DATABASE_INITIALIZED, false);
		registry.registerBoolean(MAINTENANCE, false);
		
		registry.register( // NOSONAR findbugs:DMI_HARDCODED_ABSOLUTE_FILENAME
				IMAGE_MAGICK_CONVERT_BINARY_PATH,
				input -> {
					if (!StringUtils.hasText(input)) {
						return null;
					}
					return new File(input);
				},
				new File("/usr/bin/convert")
		);
	}

}
