package org.igloo.monitoring.service;

import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HealthService implements IHealthService {

  Map<String, IHealthMetricService> services = new HashMap<>();

  /** {@inheritDoc} */
  @Override
  public HealthLookup getHealthLookup(String metric) {
    return Optional.ofNullable(services.get(metric))
        .map(IHealthMetricService::getHealthLookup)
        .orElseThrow();
  }

  /** {@inheritDoc} */
  @Override
  public HealthService addService(String name, IHealthMetricService service) {
    services.put(name, service);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public IHealthMetricService getService(String name) {
    return Optional.ofNullable(services.get(name)).orElseThrow();
  }

  public static HealthService of(
      @Nonnull String name, @Nonnull IHealthMetricService service, Object... args) {
    if (args != null && args.length % 2 != 0) {
      throw new IllegalStateException("Args must be a key/value list (even length)");
    }
    HealthService h = new HealthService();
    h.addService(name, service);
    if (args != null) {
      for (int i = 0; i + 2 <= args.length; i = i + 2) {
        Object[] object = Arrays.copyOfRange(args, i, i + 2);
        if (object[0] instanceof String && object[1] instanceof IHealthMetricService) {
          h.addService((String) object[0], (IHealthMetricService) object[1]);
        }
      }
    }
    return h;
  }
}
