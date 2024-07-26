package org.iglooproject.jpa.config.spring.provider;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class JpaPackageScanProvider {

  private final List<Package> packages;

  public JpaPackageScanProvider(Package... packages) {
    super();

    this.packages = ImmutableList.copyOf(packages);
  }

  public List<Package> getPackages() {
    return packages;
  }
}
