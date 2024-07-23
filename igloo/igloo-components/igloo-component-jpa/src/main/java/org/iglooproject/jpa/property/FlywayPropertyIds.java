package org.iglooproject.jpa.property;

import java.util.Objects;
import java.util.Set;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.model.ImmutablePropertyIdTemplate;

public class FlywayPropertyIds extends AbstractPropertyIds {

  private FlywayPropertyIds() {}

  public static final ImmutablePropertyId<Set<String>> FLYWAY_PLACEHOLDERS_PROPERTIES =
      immutable("spring.flyway.placeholders");
  public static final ImmutablePropertyIdTemplate<String> FLYWAY_PLACEHOLDERS_PROPERTY =
      immutableTemplate("spring.flyway.placeholders.%1s");

  public static final ImmutablePropertyId<String> property(String flywayProperty) {
    Objects.requireNonNull(flywayProperty);
    return FLYWAY_PLACEHOLDERS_PROPERTY.create(flywayProperty);
  }
}
