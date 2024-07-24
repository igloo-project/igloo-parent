package org.iglooproject.jpa.security.autoconfigure;

import org.iglooproject.jpa.security.business.JpaSecurityBusinessPackage;
import org.iglooproject.spring.autoconfigure.AbstractComponentScanConfigurationWorkaround;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityComponentScanConfiguration
    extends AbstractComponentScanConfigurationWorkaround {
  @Override
  protected Class<?>[] getComponentScanPackages() {
    return new Class<?>[] {JpaSecurityBusinessPackage.class};
  }
}
