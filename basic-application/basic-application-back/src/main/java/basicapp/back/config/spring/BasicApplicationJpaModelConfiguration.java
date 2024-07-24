package basicapp.back.config.spring;

import basicapp.back.business.BasicApplicationCoreCommonBusinessPackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = BasicApplicationCoreCommonBusinessPackage.class)
public class BasicApplicationJpaModelConfiguration {}
