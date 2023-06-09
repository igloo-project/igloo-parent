package org.iglooproject.jpa.more.autoconfigure;

import org.iglooproject.jpa.more.business.CoreJpaMoreBusinessPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = CoreJpaMoreBusinessPackage.class)
public class JpaMoreModelAutoConfiguration {
}
