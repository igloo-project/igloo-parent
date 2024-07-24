package igloo.bootstrap;

import java.util.Objects;

public final class BootstrapSettings {

  private final IBootstrapProvider bootstrap4Provider;

  private final IBootstrapProvider bootstrap5Provider;

  private final BootstrapVersion defaultVersion;

  public BootstrapSettings(
      BootstrapVersion defaultVersion,
      IBootstrapProvider bootstrap4Provider,
      IBootstrapProvider bootstrap5Provider) {
    Objects.requireNonNull(defaultVersion, "Default bootstrap version cannot be null");
    this.defaultVersion = defaultVersion;
    this.bootstrap4Provider = bootstrap4Provider;
    this.bootstrap5Provider = bootstrap5Provider;
  }

  public IBootstrapProvider getBootstrap5Provider() {
    return bootstrap5Provider;
  }

  public IBootstrapProvider getBootstrap4Provider() {
    return bootstrap4Provider;
  }

  public BootstrapVersion getDefaultVersion() {
    return defaultVersion;
  }
}
