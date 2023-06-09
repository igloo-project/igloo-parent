package org.iglooproject.basicapp.core.config.spring;

import org.iglooproject.basicapp.core.business.BasicApplicationCoreCommonBusinessPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = BasicApplicationCoreCommonBusinessPackage.class)
public class BasicApplicationJpaConfiguration {

}
