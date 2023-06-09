package org.iglooproject.jpa.more.autoconfigure;

import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.iglooproject.jpa.more.util.CoreJpaMoreUtilPackage;
import org.iglooproject.spring.autoconfigure.AbstractComponentScanConfigurationWorkaround;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaMoreComponentScanConfiguration extends AbstractComponentScanConfigurationWorkaround {
	@Override
	protected Class<?>[] getComponentScanPackages() {
		return new Class<?>[] { CoreJpaMoreBusinessPackage.class, CoreJpaMoreUtilPackage.class };
	}
}
