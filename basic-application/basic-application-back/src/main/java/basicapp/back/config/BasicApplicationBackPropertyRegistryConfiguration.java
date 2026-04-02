package basicapp.back.config;

import static basicapp.back.property.BasicApplicationBackPropertyIds.BUILD_DATE;
import static basicapp.back.property.BasicApplicationBackPropertyIds.BUILD_SHA;
import static basicapp.back.property.BasicApplicationBackPropertyIds.ENVIRONMENT;
import static basicapp.back.property.BasicApplicationBackPropertyIds.SECURITY_PASSWORD_LENGTH_MAX;
import static basicapp.back.property.BasicApplicationBackPropertyIds.SECURITY_PASSWORD_LENGTH_MIN;
import static basicapp.back.property.BasicApplicationBackPropertyIds.SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS;
import static basicapp.back.property.BasicApplicationBackPropertyIds.SECURITY_PASSWORD_VALIDATOR_ENABLED;

import basicapp.back.util.Environment;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import java.time.Instant;
import java.util.Optional;
import org.iglooproject.functional.Suppliers2;
import org.iglooproject.functional.converter.StringCollectionConverter;
import org.iglooproject.spring.config.IPropertyRegistryConfiguration;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationBackPropertyRegistryConfiguration
    implements IPropertyRegistryConfiguration {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.register(
        BUILD_DATE,
        input -> Instant.ofEpochMilli(Optional.ofNullable(Longs.tryParse(input)).orElse(0L)));
    registry.registerString(BUILD_SHA);

    registry.registerEnum(ENVIRONMENT, Environment.class, Environment.production);

    registry.registerInteger(SECURITY_PASSWORD_LENGTH_MIN, 8);
    registry.registerInteger(SECURITY_PASSWORD_LENGTH_MAX, 64);
    registry.registerBoolean(SECURITY_PASSWORD_VALIDATOR_ENABLED, true);
    registry.register(
        SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS,
        new StringCollectionConverter<>(Converter.identity(), Suppliers2.arrayList()),
        Lists.newArrayList());
  }
}
