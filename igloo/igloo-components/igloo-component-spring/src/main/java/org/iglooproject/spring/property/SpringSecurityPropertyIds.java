package org.iglooproject.spring.property;

import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public final class SpringSecurityPropertyIds extends AbstractPropertyIds {

  private SpringSecurityPropertyIds() {}

  /*
   * Mutable Properties
   */

  // None

  /*
   * Immutable Properties
   */

  public static final ImmutablePropertyId<Integer> PASSWORD_EXPIRATION_DAYS =
      immutable("security.password.expiration.days");
  public static final ImmutablePropertyId<Integer> PASSWORD_HISTORY_COUNT =
      immutable("security.password.history.count");
  public static final ImmutablePropertyId<Integer> PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT =
      immutable("security.password.recovery.request.token.random.count");
  public static final ImmutablePropertyId<Integer> PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES =
      immutable("security.password.recovery.request.expiration.minutes");
}
