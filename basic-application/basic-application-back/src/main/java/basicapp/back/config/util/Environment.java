package basicapp.back.config.util;

@SuppressWarnings("squid:S00101") // attributes named on purpose, skip class name rule
public enum Environment {
  development,
  integration,
  testing,
  staging,
  production;
}
