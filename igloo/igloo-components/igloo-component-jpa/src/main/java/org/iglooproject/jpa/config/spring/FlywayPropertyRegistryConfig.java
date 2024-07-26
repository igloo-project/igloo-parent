package org.iglooproject.jpa.config.spring;

import static org.iglooproject.jpa.property.FlywayPropertyIds.FLYWAY_PLACEHOLDERS_PROPERTIES;
import static org.iglooproject.jpa.property.FlywayPropertyIds.FLYWAY_PLACEHOLDERS_PROPERTY;

import com.google.common.base.Converter;
import java.util.Collections;
import java.util.Set;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.functional.converter.StringCollectionConverter;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.COMPONENT,
    value = "classpath:flyway-placeholders.properties",
    encoding = "UTF-8")
public class FlywayPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.register(
        FLYWAY_PLACEHOLDERS_PROPERTIES,
        new StringCollectionConverter<String, Set<String>>(
                Converter.identity(), Suppliers2.hashSet())
            .separator(","),
        Collections.emptySet());
    registry.registerString(FLYWAY_PLACEHOLDERS_PROPERTY);
  }
}
