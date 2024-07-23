package org.iglooproject.basicapp.core.config.spring;

import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.BUILD_DATE;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.BUILD_SHA;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.ENVIRONMENT;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MAX;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_LENGTH_MIN;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;
import static org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import java.time.Instant;
import java.util.Date;
import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.functional.converter.StringCollectionConverter;
import org.iglooproject.jpa.more.business.parameter.dao.ParameterDaoImpl;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationCoreApplicationPropertyConfig implements IPropertyRegistryConfig {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.register(
        BUILD_DATE,
        input -> {
          Long epochMilli =
              java.util.Optional.ofNullable(Longs.tryParse(input)).orElse((Long) null);
          return Date.from(Instant.ofEpochMilli(epochMilli != null ? epochMilli : 0));
        });
    registry.registerString(BUILD_SHA);

    registry.registerEnum(ENVIRONMENT, Environment.class, Environment.production);

    registry.registerInteger(SECURITY_PASSWORD_LENGTH_MIN, 8);
    registry.registerInteger(SECURITY_PASSWORD_LENGTH_MAX, 64);
    registry.registerBoolean(SECURITY_PASSWORD_VALIDATOR_ENABLED, true);
    registry.register(
        SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS,
        new StringCollectionConverter<>(
            Converter.<String>identity(), Suppliers2.<String>arrayList()),
        Lists.<String>newArrayList());
  }

  @Bean
  public IMutablePropertyDao mutablePropertyDao() {
    return new ParameterDaoImpl();
  }
}
